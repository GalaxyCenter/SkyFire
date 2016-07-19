package apollo.tianya.service;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import apollo.tianya.bean.Constants;
import apollo.tianya.util.TLog;

/**
 * Created by Texel on 2016/7/19.
 */
public class NoticeService extends Service {

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

    private static final long INTERVAL = 1000 * 120;
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

    private void clearNotice(int uid, int type) {
    }

    /**
     * 请求是否有新通知
     */
    private void requestNotice() {
    }

    private void startRequestAlarm() {
    }

    private void cancelRequestAlarm() {
    }
}
