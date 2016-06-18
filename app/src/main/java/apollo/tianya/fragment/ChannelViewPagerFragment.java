package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.base.BaseViewPagerFragment;

/**
 * Created by Texel on 2016/6/1.
 */
public class ChannelViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] titles = getResources().getStringArray(
                R.array.channel_viewpage_arrays);

        for(String title:titles) {
            adapter.addTab(title, title, ChannelFragment.class,
                    getBundle(title));
        }
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseListFragment.BUNDLE_KEY_CATALOG, type);
        return bundle;
    }

}
