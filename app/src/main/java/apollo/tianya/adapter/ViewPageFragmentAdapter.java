package apollo.tianya.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import apollo.tianya.R;
import apollo.tianya.base.BaseFragment;

/**
 * Created by Texel on 2016/6/1.
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final List<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();
    private final WeakHashMap<String, BaseFragment> mFragments = null;

    public ViewPageFragmentAdapter(FragmentManager m,
                                   Context c) {
        super(m);
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.refer.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    public void addTab(String title, String tag, Class<?> clss, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, clss, args);
        addFragment(viewPageInfo);
    }

    private void addFragment(ViewPageInfo info) {
//        // 加入tab title
//        View v = LayoutInflater.from(mContext).inflate(
//                R.layout.base_viewpage_fragment_tab_item, null, false);
//        TextView title = (TextView) v.findViewById(R.id.tab_title);
//        title.setText(info.title);
//
//        mTabs.add(info);
//        notifyDataSetChanged();
        BaseFragment f = null;

        mTabs.add(info);

        f = getFragmentFromCache(info);
        mFragments.put(info.tag, f);
        notifyDataSetChanged();
    }

    private BaseFragment getFragmentFromCache(ViewPageInfo info) {
        BaseFragment f = null;

        f = mFragments.get(info.tag);
        if (f == null) {
            try{f = (BaseFragment)info.refer.newInstance();}catch(Exception ex){}
            mFragments.put(info.tag, f);
        }
        return f;
    }
}
