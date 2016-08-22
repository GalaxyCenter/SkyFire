package apollo.tianya.util;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;

/**
 * Created by Texel on 2016/8/22.
 */
public class MethodsCompat {

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }
}
