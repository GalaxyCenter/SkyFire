package apollo.tianya.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import apollo.tianya.AppContext;
import apollo.tianya.R;

/**
 * Created by Texel on 2016/8/1.
 */
public class SpannableUtil {

    public interface ImageLoadedHandle {
        public void onImageLoaded(SpannableString spannable, String url, Bitmap bmp);
    }

    static class SpannableImageLoadingListener extends SimpleImageLoadingListener {

        ImageLoadedHandle handle;
        SpannableString spannable;

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (handle != null)
                handle.onImageLoaded(spannable, imageUri, loadedImage);
        }
    }

    public static BitmapDrawable DRAWABLE_LOADING = (BitmapDrawable) AppContext.getInstance().
                                                    getResources().getDrawable(R.drawable.image_default);

    public static void drawImage(SpannableString spannable, String source,
                                 BitmapDrawable drawable, DisplayImageOptions options,
                                 ImageLoadedHandle handle) {
        Bitmap bmp = null;
        ImageSpan img_span = null;
        List<Map<String, Object>> list = null;
        Map<String,Object> map = null;
        String img_src = null;
        String img_regex = null;
        SpannableImageLoadingListener listener;
        int start = 0;
        int end = 0;

        img_regex = "\\[img\\](.*?)\\[/img\\]";
        list = Regex.getStartAndEndIndex(source, Pattern.compile(img_regex));
        for (int idx=0; idx<list.size(); idx++) {
            map = list.get(idx);
            img_src = (String)map.get("str1");
            listener = new SpannableImageLoadingListener();
            listener.handle = handle;
            listener.spannable = spannable;

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            img_span = new ImageSpan(drawable, img_src, ImageSpan.ALIGN_BOTTOM);
            start = (Integer) map.get("startIndex");
            end = (Integer) map.get("endIndex") + 1;
            spannable.setSpan(img_span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ImageLoader.getInstance().loadImage(img_src, options, listener);
        }

        ImageSpan[] image_spans = spannable.getSpans(0, spannable.length(), ImageSpan.class);
        for (ImageSpan span : image_spans) {
            final String image_src = span.getSource();
            ClickableSpan click_span = null;
            ClickableSpan[] click_spans = null;

            // clear clickspan
            start = spannable.getSpanStart(span);
            end = spannable.getSpanEnd(span);
            click_spans = spannable.getSpans(start, end, ClickableSpan.class);
            for (ClickableSpan c_span : click_spans) {
                spannable.removeSpan(c_span);
            }

            click_span = new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                }
            };
            spannable.setSpan(click_span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
