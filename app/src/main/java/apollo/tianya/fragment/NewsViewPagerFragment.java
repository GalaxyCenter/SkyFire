package apollo.tianya.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.NewsList;

/**
 * Created by Texel on 2016/6/1.
 */
public class NewsViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);
        adapter.addTab(title[0], "news", NewsListFragment.class,
                getBundle(NewsList.CATALOG_ALL));
        adapter.addTab(title[1], "news_week", NewsListFragment.class,
                getBundle(NewsList.CATALOG_WEEK));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

}
