package apollo.tianya.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageInfo;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.Notice;
import apollo.tianya.bean.Thread;
import apollo.tianya.fragment.ActivityPubFragment;
import apollo.tianya.fragment.PostsFragment;
import apollo.tianya.fragment.ThreadsFragment;
import apollo.tianya.ui.CollapsedDetailActivity;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.ui.ImageActivity;
import apollo.tianya.ui.LoginActivity;
import apollo.tianya.ui.MainActivity;
import apollo.tianya.ui.SimpleBackActivity;

/**
 * 页面帮助类
 *
 * Created by Texel on 2016/5/26.
 */
public class UIHelper {

    /**
     * 显示登录界面
     * @param context
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示帖子详情
     * @param context
     * @param thread
     */
    public static void showPostDetail(Context context, Thread thread) {
        Intent intent = new Intent(context, CollapsedDetailActivity.class);

        intent.putExtra(Constants.BUNDLE_KEY_SECTION_ID, thread.getSectionId());
        intent.putExtra(Constants.BUNDLE_KEY_THREAD_ID, thread.getGuid());
        intent.putExtra(Constants.BUNDLE_KEY_PAGE_INDEX, 1);
        intent.putExtra(Constants.BUNDLE_KEY_FRAGMENT, PostsFragment.class);

        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, ViewPageInfo page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_PAGEINFO, (Parcelable)page);
        context.startActivity(intent);
    }

    /**
     * 发送通知广播
     *
     * @param context
     * @param notice
     */
    public static void sendBroadCast(Context context, Notice notice) {
        if (!((AppContext) context.getApplicationContext()).isLogin()
                || notice == null)
            return;
        Intent intent = new Intent(Constants.INTENT_ACTION_NOTICE);
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.BUNDLE_KEY_NOTICES, notice);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    /**
     * 显示一个URL内容
     * @param context
     * @param url
     */
    public static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;
    }

    public static void showImageActivity(Context context, String image) {
        String[] images = new String[]{ image };

        showImageActivity(context, images);
    }

    public static void showImageActivity(Context context, String[] images) {
        Intent intent = new Intent(context, ImageActivity.class);

        intent.putExtra(Constants.BUNDLE_KEY_IMAGES, images);
        context.startActivity(intent);
    }

    public static void showThreadsActivity(Context context, String sectionId) {
        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(Constants.BUNDLE_KEY_SECTION_ID, sectionId);
        intent.putExtra(Constants.BUNDLE_KEY_FRAGMENT, ThreadsFragment.class);
        context.startActivity(intent);
    }

    public static void clearAppCache(final Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Snackbar.make((ViewGroup)activity.getWindow().getDecorView(), "缓存清除成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make((ViewGroup)activity.getWindow().getDecorView(), "缓存清除失败", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        };
        new java.lang.Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static void showActivityPublish(Context context, String sectionId) {
        String tag = context.getString(R.string.actionbar_title_activity_pub);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_SECTION_ID, sectionId);

        UIHelper.showSimpleBack(context, new ViewPageInfo(tag, tag, ActivityPubFragment.class, bundle));
    }

    public static void showFriends(Context context) {
        String tag = context.getString(R.string.actionbar_title_friends);
        Bundle bundle = new Bundle();

        UIHelper.showSimpleBack(context, new ViewPageInfo(tag, tag, ActivityPubFragment.class, bundle));
    }
}
