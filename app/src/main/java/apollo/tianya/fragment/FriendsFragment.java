package apollo.tianya.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import apollo.tianya.adapter.FriendAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.User;

/**
 * Created by Texel on 2016/10/31.
 */

public class FriendsFragment extends BaseListFragment<User> {
    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new FriendAdapter();
    }

    @Override
    protected DataSet<User> parseList(byte[] datas) {
        return null;
    }

    @Override
    protected void sendRequestData() {

    }

    @Override
    protected String getCacheKeyPrefix() {
        return "friends_" ;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
