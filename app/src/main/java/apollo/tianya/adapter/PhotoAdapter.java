package apollo.tianya.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Texel on 2016/6/14.
 */
public class PhotoAdapter extends PagerAdapter{

    private Activity mActivity;

    public PhotoAdapter(Activity activity) {

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mActivity);
        //imageView.setImageResource(R.drawable.image1 + position);
        ((ViewPager)container).addView(imageView, position);
        return imageView;
    }
}
