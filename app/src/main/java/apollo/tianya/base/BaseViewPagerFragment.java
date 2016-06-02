package apollo.tianya.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.widget.EmptyLayout;

/**
 * 带有导航条的基类
 * Created by Texel on 2016/6/1.
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

    private TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);

    @Override
    protected int getLayoutId() {
        return R.layout.base_viewpage_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(), getContext());

        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        onSetupTabAdapter(mTabsAdapter);
    }
}
