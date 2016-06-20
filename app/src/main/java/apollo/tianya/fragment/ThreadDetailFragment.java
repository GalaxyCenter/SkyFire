package apollo.tianya.fragment;

import android.view.View;
import android.widget.AdapterView;

import apollo.tianya.adapter.PostAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Post;

/**
 * Created by Texel on 2016/6/20.
 */
public class ThreadDetailFragment extends BaseListFragment {
    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new PostAdapter();
    }

    @Override
    protected DataSet<Post> parseList(byte[] datas) {
        String body = null;
        DataSet<Post> dataset = null;

        body = new String(datas);
        dataset = TianyaParser.parsePosts(body);
        return dataset;
    }

    @Override
    protected void onItemClick(AdapterView parent, View view, int position, long id) {

    }
}
