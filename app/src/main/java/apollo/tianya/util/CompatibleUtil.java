package apollo.tianya.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * Created by Texel on 2016/7/7.
 */
public class CompatibleUtil {

    public static int getSoftInputHeight(Activity activity) {
        Rect rect = null;
        int screenHeight = 0;
        int softInputHeight = 0;

        rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        //获取屏幕的高度
        screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        softInputHeight = screenHeight - rect.bottom;

        if (Build.VERSION.SDK_INT >= 18) {
            // When SDK Level >= 18, the softInputHeight will contain the height of softButtonsBar (if has)
            //softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
        }
        return softInputHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        int usableHeight = 0;
        int realHeight = 0;

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

}
