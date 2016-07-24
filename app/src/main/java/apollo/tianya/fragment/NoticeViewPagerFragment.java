package apollo.tianya.fragment;

import android.os.Bundle;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageFragmentAdapter;
import apollo.tianya.base.BaseViewPagerFragment;
import apollo.tianya.bean.Constants;

/**
 * Created by kuibo on 2016/7/21.
 */
public class NoticeViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] titles = getResources().getStringArray(
                R.array.notice_viewpage_arrays);

        for(int i=0; i<titles.length; i++) {
            if (i<3)
                adapter.addTab(titles[i], titles[i], PostReplyFragment.class, getBundle(titles[i]));
            else
                adapter.addTab(titles[i], titles[i], MessageFragment.class, getBundle(titles[i]));
        }
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        return bundle;
    }

}
