package apollo.tianya.util;

import android.content.Context;
import android.content.Intent;

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

    public static final String BUNDLE_KEY_SECTION_ID = "section_id";
    public static final String BUNDLE_KEY_THREAD_ID = "thread_id";
    public static final String BUNDLE_KEY_PAGE_INDEX = "page_index";
    public static final String BUNDLE_KEY_AUTHOR = "author";

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

        intent.putExtra(BUNDLE_KEY_SECTION_ID, thread.getSectionId());
        intent.putExtra(BUNDLE_KEY_THREAD_ID, thread.getGuid());
        intent.putExtra(BUNDLE_KEY_PAGE_INDEX, 1);
        intent.putExtra(BUNDLE_KEY_AUTHOR, thread.getAuthor());

        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackActivity.SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }
}
