package apollo.tianya.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import apollo.tianya.R;
import apollo.tianya.adapter.ListBaseAdapter;
import apollo.tianya.bean.Entity;
import butterknife.BindView;

/**
 * Created by kuibo on 2016/6/1.
 */
public abstract class BaseListFragment<T extends Entity> extends BaseFragment {
    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    protected abstract ListBaseAdapter<T> getListAdapter();

    @BindView(R.id.listview)
    protected ListView mListView;

    protected ListBaseAdapter<T> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public void initView(View view) {

        if (mAdapter == null) {
            mAdapter = getListAdapter();
        }
        mListView.setAdapter(mAdapter);
    }

}
