package apollo.tianya.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by kuibo on 2016/7/24.
 */
public class PostReplyFragment extends BaseListFragment<Thread> {
    
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
    public void onItemClick(View view, int postion) {

    }
}
