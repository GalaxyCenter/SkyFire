package apollo.tianya.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.ref.WeakReference;

import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.Constants;
import apollo.tianya.broadcast.AlarmReceiver;
import apollo.tianya.util.TLog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/7/19.
 */
public class NoticeService extends Service {

    private final AsyncHttpResponseHandler mGetNoticeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
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
        public void clearNotice(int uid, int type) throws RemoteException {
            mService.get().clearNotice(uid, type);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.INTENT_ACTION_NOTICE.equals(action)) {

            } else if (INTENT_ACTION_BROADCAST.equals(action)) {

            } else if (INTENT_ACTION_SHUTDOWN.equals(action)) {
                stopSelf();
            } else if (INTENT_ACTION_REQUEST.equals(action)) {
                requestNotice();
            }

            TLog.log(TAG, "BroadcastReceiver:" + action);
        }
    };
    private static final String TAG = "NoticeService";

    private static final long INTERVAL = 1000 * 20;
    public static final String INTENT_ACTION_GET = "cn.tianya.service.GET_NOTICE";
    public static final String INTENT_ACTION_CLEAR = "cn.tianya.service.CLEAR_NOTICE";
    public static final String INTENT_ACTION_BROADCAST = "cn.tianya.service.BROADCAST";
    public static final String INTENT_ACTION_SHUTDOWN = "cn.tianya.service.SHUTDOWN";
    public static final String INTENT_ACTION_REQUEST = "cn.tianya.service.REQUEST";
    public static final String INTENT_ACTION_NOTICE = "cn.tianya.action.APPWIDGET_UPDATE";

    public static final String BUNDLE_KEY_TPYE = "bundle_key_type";

    private IBinder mBinder = new ServiceStub(this);
    private AlarmManager mAlarmMgr;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    private void clearNotice(int uid, int type) {
    }

    /**
     * 请求是否有新通知
     */
    private void requestNotice() {
        TLog.log(TAG, "requestNotice");
        TianyaApi.getMessageCount(mGetNoticeHandler);
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
}
