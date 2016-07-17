package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.Constants;

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
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        return bundle;
    }
}
