package apollo.tianya.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Texel on 2016/6/1.
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final ViewPager mViewPager;
    private final List<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();

    protected TabLayout mTabLayout;

    public ViewPageFragmentAdapter(FragmentManager fm,
                                   TabLayout tabLayout, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mTabLayout = tabLayout;
        mViewPager = pager;
        mViewPager.setAdapter(this);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
}
