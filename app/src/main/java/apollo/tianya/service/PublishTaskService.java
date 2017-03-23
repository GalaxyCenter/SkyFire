package apollo.tianya.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.R;
import apollo.tianya.api.OperationResponseHandler;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.Thread;

/** 发布内容服务类
 * Created by Texel on 2016/10/26.
 */

public class PublishTaskService extends IntentService {
    private static final String SERVICE_NAME = "PublishTaskService";
    private static final String KEY_POST = "post_";

    public static final String ACTION_PUB_POST = "apollo.tianya.ACTION_PUB_POST";
    public static final String BUNDLE_PUB_POST = "BUNDLE_PUB_POST";
    public static List<String> penddingTasks = new ArrayList<String>();

    class PublicPostResponseHandle extends OperationResponseHandler {
        String key = null;

        public PublicPostResponseHandle(Looper looper, Object... args) {
            super(looper, args);
            key = (String) args[1];
        }

        @Override
        public void onSuccess(int code, String responseBody, Object[] args) throws Exception {
            final Thread thread = (Thread) args[0];
            JSONObject json = null;
            String message = null;
            boolean isSuccess = false;

            try {
                json = new JSONObject(responseBody);
                isSuccess = "1".equals(json.get("success"));
                message = json.getString("message");
            } catch (JSONException ex) {
            }

            if (isSuccess == true) {
                notifySimpleNotifycation(thread.getId(),
                        getString(R.string.post_publish_success),
                        getString(R.string.post_public),
                        getString(R.string.post_publish_success), false, true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancellNotification(thread.getId());
                    }
                }, 3000);
                removePenddingTask(key + thread.getId());
            } else {
                onFailure(100, message, args);
            }
        }

        @Override
        public void onFailure(int code, String errorMessage, Object[] args) {
            Thread thread = (Thread) args[0];
            notifySimpleNotifycation(thread.getId(),
                    getString(R.string.post_publish_faile),
                    getString(R.string.post_public),
                    code == 100 ? errorMessage
                            : getString(R.string.post_publish_faile), false,
                    true);
            removePenddingTask(key + thread.getId());
        }
    }

    public PublishTaskService() {
        this(SERVICE_NAME);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PublishTaskService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (ACTION_PUB_POST.equals(action)) {
            Thread thread = (Thread) intent.getSerializableExtra(BUNDLE_PUB_POST);

            pubThread(thread);
        }
    }

    private void notifySimpleNotifycation(int id, String ticker, String title,
                                          String content, boolean ongoing, boolean autoCancel) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();

        NotificationManagerCompat.from(this).notify(id, notification);
    }

    private void pubThread(Thread thread) {
        addPenddingTask(KEY_POST + thread.getId());
        notifySimpleNotifycation(thread.getId(), getString(R.string.post_publishing),
                getString(R.string.post_public),
                getString(R.string.post_publishing), true, false);

        TianyaApi.createPost(thread.getSectionId(), thread.getGuid(), thread.getTitle(),
                thread.getBody(), new PublicPostResponseHandle(getMainLooper(),
                        thread, KEY_POST));
    }

    private void cancellNotification(int id) {
        NotificationManagerCompat.from(this).cancel(id);
    }

    private synchronized void addPenddingTask(String key) {
        penddingTasks.add(key);
    }

    private synchronized void removePenddingTask(String key) {
        penddingTasks.remove(key);
    }

}
