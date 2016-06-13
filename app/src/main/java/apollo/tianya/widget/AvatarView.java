package apollo.tianya.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import cz.msebera.android.httpclient.Header;
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
    private Activity mActivity;

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
        mActivity = (Activity) context;
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

        TianyaApi.getImage(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                BitmapFactory.Options option = new BitmapFactory.Options();
                Bitmap bitmap = null;

                bitmap = BitmapFactory.decodeByteArray(responseBody, 0,
                        responseBody.length, option);
                setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "ERROR");
            }
        });
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
