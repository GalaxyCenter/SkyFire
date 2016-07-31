package apollo.tianya.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.api.remote.TianyaApi;

/**
 * Created by Texel on 2016/6/14.
 */
public class PhotoAdapter extends PagerAdapter {

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

    public void removeAllItem() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = mItems.get(position);
        ImageView imageView = new ImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        container.addView(imageView);
        TianyaApi.displayImage(url, imageView);
        return imageView;
    }
}