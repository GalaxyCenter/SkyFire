package apollo.tianya.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import apollo.tianya.base.BaseApplication;

/**
 * Created by Texel on 2016/7/7.
 */
public class CompatibleUtil {

    private static String TAG = "CompatibleUtil";

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.heightPixels;
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return rect.top;
    }

    public static int getAppHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }

    public static int getSoftInputHeight(Activity activity) {
        int softInputHeight = 0;

        softInputHeight = getScreenHeight(activity) - getStatusBarHeight(activity) - getAppHeight(activity);
        Log.i(TAG, "INPUT_HEIGHT:" + softInputHeight);
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

    public static boolean hasInternet() {
        boolean flag;
        if (((ConnectivityManager) BaseApplication.context().getSystemService(
                "connectivity")).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }
}
