package apollo.tianya.fragment;

import apollo.tianya.adapter.ListBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaApi;
import apollo.tianya.base.BaseListFragment;

/**
 * Created by kuibo on 2016/6/1.
 */
public class ChannelFragment extends BaseListFragment {

    @Override
    protected ListBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.getChannel(mCatalog, mCurrentPage, mHandler);
    }
}
