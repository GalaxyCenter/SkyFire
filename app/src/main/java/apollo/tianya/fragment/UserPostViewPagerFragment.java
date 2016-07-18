package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.Constants;

/**
 * Created by Texel on 2016/7/18.
 */
public class UserPostViewPagerFragment extends BaseViewPagerFragment {

    private int mUserId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if (args != null) {
            mUserId = args.getInt(Constants.BUNDLE_KEY_USER_ID);
        }
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] titles = getResources().getStringArray(
                R.array.user_posts_viewpage_arrays);

        for(String title:titles) {
            adapter.addTab(title, title, UserPostsFragment.class,
                    getBundle(title));
        }
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        bundle.putInt(Constants.BUNDLE_KEY_USER_ID, mUserId);
        return bundle;
    }
}