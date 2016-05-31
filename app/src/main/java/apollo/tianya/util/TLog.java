package apollo.tianya.util;

import android.util.Log;

/**
 * Created by kuibo on 2016/5/31.
 */
public class TLog {
    public static final String LOG_TAG = "TLog";
    public static boolean DEBUG = true;

    public TLog() {
    }

    public static final void analytics(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void error(String log) {
        if (DEBUG)
            Log.e(LOG_TAG, "" + log);
    }

    public static final void log(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void log(String tag, String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void logv(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void warn(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }
}
