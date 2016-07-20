package apollo.tianya.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import apollo.tianya.service.NoticeUtils;
import apollo.tianya.util.TLog;

/**
 * Created by Texel on 2016/7/20.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        TLog.log(TAG, "onReceive ->cn.tianya收到定时获取消息");

        NoticeUtils.requestNotice(context);
    }
}
