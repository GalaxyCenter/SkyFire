package apollo.tianya.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.api.remote.TianyaApi;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/6/14.
 */
public class PhotoAdapter extends PagerAdapter {

    static class AsyncPhotoHttpResponseHandler extends AsyncHttpResponseHandler {

        public ImageView image;
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            BitmapFactory.Options option = new BitmapFactory.Options();
            Bitmap bitmap = null;

            bitmap = BitmapFactory.decodeByteArray(responseBody, 0,
                    responseBody.length, option);
            image.setImageBitmap(bitmap);
            bitmap = null;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    private Activity mActivity;
    protected List<String> mItems;

    public PhotoAdapter(Activity activity) {
        mActivity = activity;

        mItems = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((ImageView)object);
    }

    public void addItems(List<String> items) {
        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = mItems.get(position);
        ImageView imageView = new ImageView(mActivity);
        AsyncPhotoHttpResponseHandler handle = new AsyncPhotoHttpResponseHandler();
        container.addView(imageView, position);

        handle.image = imageView;
        TianyaApi.getImage(url, handle);
        return imageView;
    }
}