package apollo.tianya.util;

import android.content.Context;
import android.content.Intent;

import apollo.tianya.ui.LoginActivity;
import apollo.tianya.ui.MainActivity;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.bean.Thread;

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
        context.startActivity(intent);
    }
}
