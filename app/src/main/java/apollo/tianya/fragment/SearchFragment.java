package apollo.tianya.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by kuibo on 2016/8/18.
 */
public class SearchFragment extends BaseListFragment<Thread> {
    @Override
    protected RecyclerBaseAdapter<Thread, RecyclerView.ViewHolder> getListAdapter() {
        return null;
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        return null;
    }

    @Override
    protected void sendRequestData() {

    }

    @Override
    protected String getCacheKeyPrefix() {
        return null;
    }

    @Override
    public void onItemClick(View view, int postion) {

    }
}
