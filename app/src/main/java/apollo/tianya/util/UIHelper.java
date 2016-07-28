package apollo.tianya.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import apollo.tianya.AppContext;
import apollo.tianya.adapter.ViewPageInfo;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.Notice;
import apollo.tianya.ui.LoginActivity;
import apollo.tianya.ui.MainActivity;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.bean.Thread;
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
        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(Constants.BUNDLE_KEY_SECTION_ID, thread.getSectionId());
        intent.putExtra(Constants.BUNDLE_KEY_THREAD_ID, thread.getGuid());
        intent.putExtra(Constants.BUNDLE_KEY_PAGE_INDEX, 1);
        intent.putExtra(Constants.BUNDLE_KEY_AUTHOR, thread.getAuthor());

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
}
