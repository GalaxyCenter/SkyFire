package apollo.tianya.base;

import android.widget.ListView;

import apollo.tianya.R;

/**
 * Created by kuibo on 2016/6/1.
 */
public class BaseListFragment extends BaseFragment {
    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    @InjectView(R.id.listview)
    protected ListView mListView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }
}
