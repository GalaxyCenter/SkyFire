package apollo.tianya.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Texel on 2016/6/3.
 */
public class AvatarView extends CircleImageView {

    private String TAG = this.getClass().getName();

    public static final String AVATAR_SIZE_REG = "_[0-9]{1,3}";
    public static final String MIDDLE_SIZE = "_100";
    public static final String LARGE_SIZE = "_200";

    private int id;
    private String mName;

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvatarView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

    }

    public void setUserInfo(int id, String name) {
        this.id = id;
        this.mName = name;
    }

    public void setAvatarUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setImageResource(R.drawable.ic_account_circle_blue_37dp);
            return;
        }

        TianyaApi.displayImage(url, this);
    }

    public static String getSmallAvatar(String source) {
        return source;
    }

    public static String getMiddleAvatar(String source) {
        if (source == null)
            return "";
        return source.replaceAll(AVATAR_SIZE_REG, MIDDLE_SIZE);
    }

    public static String getLargeAvatar(String source) {
        if (source == null)
            return "";
        return source.replaceAll(AVATAR_SIZE_REG, LARGE_SIZE);
    }
}
