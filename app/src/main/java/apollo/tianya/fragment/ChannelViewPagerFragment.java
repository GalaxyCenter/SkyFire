package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.ChannelList;

/**
 * Created by Texel on 2016/6/1.
 */
public class ChannelViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);
        adapter.addTab(title[0], "news", ChannelFragment.class,
                getBundle(ChannelList.CATALOG_ALL));
        adapter.addTab(title[1], "news_week", ChannelFragment.class,
                getBundle(ChannelList.CATALOG_WEEK));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

}
