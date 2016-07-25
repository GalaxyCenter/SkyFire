package apollo.tianya.fragment;

import android.view.View;

import apollo.tianya.R;
import apollo.tianya.adapter.PostReplyAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by kuibo on 2016/7/24.
 */
public class PostReplyFragment extends BaseListFragment<Thread> {

    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new PostReplyAdapter();
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        DataSet<Thread> dataset = null;
        String body = null;

        body = new String(datas);

        if (mCatalog.equals(getString(R.string.channel_follows)))
            dataset = TianyaParser.parseUserFollows(body);
        else if (mCatalog.equals(getString(R.string.channel_comments)))
            dataset = TianyaParser.parseUserComments(body);
        else if (mCatalog.equals(getString(R.string.channel_replies)))
            dataset = TianyaParser.parseUserReplies(body);

        return dataset;
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog.equals(getString(R.string.channel_follows)))
            TianyaApi.getFollows(mPageIndex, 20, mHandler);
        else if (mCatalog.equals(getString(R.string.channel_comments)))
            TianyaApi.getComments(mPageIndex, 20, mHandler);
        else if (mCatalog.equals(getString(R.string.channel_replies)))
            TianyaApi.getReplies(mPageIndex, 20, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }
}
