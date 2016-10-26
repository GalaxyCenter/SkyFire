package apollo.tianya.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Texel on 2016/8/22.
 */
public class MethodsCompat {

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    @TargetApi(7)
    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
        return MediaStore.Images.Thumbnails.getThumbnail(cr,origId,kind, options);
    }
}
