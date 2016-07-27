package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.Constants;

/**
 * Created by Texel on 2016/6/13.
 */
public class CollectionViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] titles = getResources().getStringArray(
                R.array.collection_viewpage_arrays);

        adapter.addTab(titles[0], titles[0], ChannelFragment.class, getBundle(titles[0]));
        adapter.addTab(titles[1], titles[1], ChannelFragment.class, getBundle(titles[1]));
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        return bundle;
    }
}
