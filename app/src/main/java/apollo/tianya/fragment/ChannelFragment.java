package apollo.tianya.fragment;


import android.view.View;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.UIHelper;

/**
 * Created by kuibo on 2016/6/1.
 */
public class ChannelFragment extends BaseListFragment<Thread> {

    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        DataSet<Thread> dataset = null;
        String body = null;

        body = new String(datas);

        if (mCatalog.equals(getString(R.string.channel_hot)))
            dataset = TianyaParser.parseHotThread(body);
        else if (mCatalog.equals(getString(R.string.nav_my_bookmarks))
                || mCatalog.equals(getString(R.string.channel_user_thread_bookmarks)))
            dataset = TianyaParser.parseBookmarks(body);
        else if (mCatalog.equals(getString(R.string.nav_my_histories)))
            dataset = TianyaParser.parseHistory(body);
        else
            dataset = TianyaParser.parseRecommendThread(body);

        return dataset;
    }

    public void onItemClick(View view, int position) {
        Thread thread = mAdapter.getItem(position);

        if (TianyaParser.parseThreadUrl(thread.getUrl()) != null)
            UIHelper.showPostDetail(view.getContext(), thread);
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog.equals(getString(R.string.channel_recommend)))
            TianyaApi.getRecommendThread(mHandler);
        else if (mCatalog.equals(getString(R.string.channel_hot)))
            TianyaApi.getHotThread(1, mHandler);
        else if (mCatalog.equals(getString(R.string.channel_bagua)))
            TianyaApi.getChannel("bagua", mHandler);

        else if (mCatalog.equals(getString(R.string.channel_guoji)))
            TianyaApi.getChannel("guoji", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_car)))
            TianyaApi.getChannel("car", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_gushi)))
            TianyaApi.getChannel("gushi", mHandler);

        else if (mCatalog.equals(getString(R.string.channel_guihua)))
            TianyaApi.getChannel("guihua", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_qinggan)))
            TianyaApi.getChannel("qinggan", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_zatan)))
            TianyaApi.getChannel("zatan", mHandler);

        else if (mCatalog.equals(getString(R.string.channel_shishang)))
            TianyaApi.getChannel("shishang", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_lvyou)))
            TianyaApi.getChannel("lvyou", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_meitu)))
            TianyaApi.getChannel("meitu", mHandler);
        else if (mCatalog.equals(getString(R.string.channel_chengshi)))
            TianyaApi.getChannel("chengshi", mHandler);

        else if (mCatalog.equals(getString(R.string.nav_my_bookmarks))
                || mCatalog.equals(getString(R.string.channel_user_thread_bookmarks)))
            TianyaApi.getBookMarks(mPageIndex, mHandler);
        else if (mCatalog.equals(getString(R.string.nav_my_histories)))
            TianyaApi.getUserHistory(mHandler);

    }
}
