package apollo.tianya.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.Notice;
import apollo.tianya.broadcast.AlarmReceiver;
import apollo.tianya.ui.MainActivity;
import apollo.tianya.util.TLog;
import apollo.tianya.util.UIHelper;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/7/19.
 */
public class NoticeService extends Service {

    class AsyncNoticeHttpResponseHandler extends AsyncHttpResponseHandler {

        int NoticeType = NOTICE_TYPE_NORMAL;

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String body = new String(responseBody);
            Notice notice = null;

            if (NoticeType == NOTICE_TYPE_NORMAL) {
                notice = TianyaParser.parseNotices(body);
                mNotice.replies = notice.replies;
                mNotice.follows = notice.follows;
                mNotice.comments = notice.comments;
            } else {
                notice = TianyaParser.parseNotices(body);

                mNotice.messages = notice.messages;
                mNotice.notifictions = notice.notifictions;
            }

            NoticeService.this.mNoticeType |= NoticeType;

            TLog.log(TAG, "TYPE:" + NoticeService.this.mNoticeType);

            if (NoticeService.this.mNoticeType == (NOTICE_TYPE_NORMAL | NOTICE_TYPE_NORMALEX) ) {
                UIHelper.sendBroadCast(NoticeService.this, notice);
                if (AppContext.getBool(Constants.KEY_NOTIFICATION_ACCEPT, true)) {
                    notification(notice);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    private final AsyncHttpResponseHandler mClearNoticeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                              Throwable arg3) {}
    };

    private static class ServiceStub extends INoticeService.Stub {
        WeakReference<NoticeService> mService;

        ServiceStub(NoticeService service) {
            mService = new WeakReference<NoticeService>(service);
        }

        @Override
        public void scheduleNotice() throws RemoteException {
            mService.get().startRequestAlarm();
        }

        @Override
        public void requestNotice() throws RemoteException {
            mService.get().requestNotice();
        }

        @Override
        public void clearNotice(int type) throws RemoteException {
            mService.get().clearNotice(type);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.INTENT_ACTION_NOTICE.equals(action)) {
                int count = mNotice.comments + mNotice.replies + mNotice.follows
                        + mNotice.messages + mNotice.notifictions;

                if (count == 0) {
                    NotificationManagerCompat.from(NoticeService.this).cancel(
                            R.string.notice_messages_content);
                }

            } else if (INTENT_ACTION_BROADCAST.equals(action)) {
                if (mNotice != null) {
                    UIHelper.sendBroadCast(NoticeService.this, mNotice);
                }
            } else if (INTENT_ACTION_SHUTDOWN.equals(action)) {
                stopSelf();
            } else if (INTENT_ACTION_REQUEST.equals(action)) {
                requestNotice();
            }

            TLog.log(TAG, "BroadcastReceiver:" + action);
        }
    };
    private static final String TAG = "NoticeService";

    private static final long INTERVAL = 1000 * 120;
    public static final String INTENT_ACTION_GET = "cn.tianya.service.GET_NOTICE";
    public static final String INTENT_ACTION_CLEAR = "cn.tianya.service.CLEAR_NOTICE";
    public static final String INTENT_ACTION_BROADCAST = "cn.tianya.service.BROADCAST";
    public static final String INTENT_ACTION_SHUTDOWN = "cn.tianya.service.SHUTDOWN";
    public static final String INTENT_ACTION_REQUEST = "cn.tianya.service.REQUEST";
    public static final String INTENT_ACTION_NOTICE = "cn.tianya.action.APPWIDGET_UPDATE";

    public static final int NOTICE_TYPE_NORMAL = 1;
    public static final int NOTICE_TYPE_NORMALEX = 1 << 1;

    public static final String BUNDLE_KEY_TPYE = "bundle_key_type";

    private IBinder mBinder = new ServiceStub(this);
    private AlarmManager mAlarmMgr;
    private Notice mNotice;

    private int mLastNotifiyCount;
    private int mNoticeType;

    private AsyncNoticeHttpResponseHandler mGetNoticeHandler = null;
    private AsyncNoticeHttpResponseHandler mGetNoticeExHandler = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mGetNoticeHandler = new AsyncNoticeHttpResponseHandler();
        mGetNoticeHandler.NoticeType = NOTICE_TYPE_NORMAL;

        mGetNoticeExHandler = new AsyncNoticeHttpResponseHandler();
        mGetNoticeExHandler.NoticeType = NOTICE_TYPE_NORMALEX;

        mNoticeType = NOTICE_TYPE_NORMAL | NOTICE_TYPE_NORMALEX;

        mNotice = new Notice();

        mAlarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        startRequestAlarm();
        requestNotice();

        IntentFilter filter = new IntentFilter(INTENT_ACTION_BROADCAST);
        filter.addAction(INTENT_ACTION_NOTICE);
        filter.addAction(INTENT_ACTION_SHUTDOWN);
        filter.addAction(INTENT_ACTION_REQUEST);
        registerReceiver(mReceiver, filter);

        TLog.log(TAG, "NoticeService@onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TLog.log(TAG, "NoticeService@onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        cancelRequestAlarm();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 采用轮询方式实现消息推送<br>
     * 每次被调用都去执行一次{@link #AlarmReceiver}onReceive()方法
     *
     * @return
     */
    private PendingIntent getOperationIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        TLog.log(TAG, "getOperationIntent@onCreate");
        return operation;
    }

    private void clearNotice(int type) {
        TianyaApi.clearNotice(type, mClearNoticeHandler);
    }

    /**
     * 请求是否有新通知
     */
    private void requestNotice() {
        TLog.log(TAG, "requestNotice");

        if (mNoticeType != (NOTICE_TYPE_NORMAL | NOTICE_TYPE_NORMALEX))
            return;

        TianyaApi.getNotices(mGetNoticeHandler);
        TianyaApi.getNotices(mGetNoticeExHandler);
        mNoticeType = 0;
    }

    private void startRequestAlarm() {
        cancelRequestAlarm();

        // 从1秒后开始，每隔2分钟执行getOperationIntent()
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000, INTERVAL,
                getOperationIntent());
    }

    private void cancelRequestAlarm() {
        mAlarmMgr.cancel(getOperationIntent());
    }

    private void notification(Notice notice) {
        int count = notice.comments + notice.follows + notice.replies;
        Resources res = getResources();
        String title = null;
        String content = null;
        StringBuffer sb = new StringBuffer();
        Intent intent = null;
        PendingIntent pi = null;
        NotificationCompat.Builder nb = null;
        Notification notification = null;

        if (count == 0) {
            mLastNotifiyCount = 0;
            NotificationManagerCompat.from(this).cancel(R.string.notice_messages_content);
            return;
        }
        if (count == mLastNotifiyCount)
            return;

        mLastNotifiyCount = count;

        if (notice.follows > 0)
            sb.append(res.getString(R.string.notice_follows, notice.follows)).append(" ");

        if (notice.comments > 0)
            sb.append(res.getString(R.string.notice_comments, notice.comments)).append(" ");

        if (notice.replies > 0)
            sb.append(res.getString(R.string.notice_replies, notice.replies)).append(" ");

        if (notice.notifictions > 0)
            sb.append(res.getString(R.string.notice_notifictions, notice.notifictions)).append(" ");

        if (notice.messages > 0)
            sb.append(res.getString(R.string.notice_messages, notice.messages)).append(" ");

        title = res.getString(R.string.notice_messages_content, count);
        content = sb.toString();

        intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.INTENT_KEY_NOTICE, true);
        pi = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        nb = new NotificationCompat.Builder(
                this).setTicker(title).setContentTitle(title)
                .setContentText(content).setAutoCancel(true)
                .setContentIntent(pi).setSmallIcon(R.drawable.image_emoticon);
        notification = nb.build();

        NotificationManagerCompat.from(this).notify(
                R.string.notice_messages_content, notification);
    }
}
