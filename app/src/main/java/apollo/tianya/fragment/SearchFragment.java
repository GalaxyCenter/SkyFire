package apollo.tianya.fragment;

import android.app.SearchManager;
import android.content.Intent;
import android.view.View;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.util.UIHelper;

/**
 * Created by kuibo on 2016/8/18.
 */
public class SearchFragment extends BaseListFragment<Thread> {

    private String mQueryWord;
    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        String source = new String(datas);

        return TianyaParser.parseSearchThreads(source);
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.searchThreads(mQueryWord, mPageIndex, mHandler);
    }

    @Override
    public void initView(View view) {
        Intent intent = getActivity().getIntent();

        mQueryWord = intent.getStringExtra(SearchManager.QUERY);

        super.initView(view);

        DetailActivity activity = (DetailActivity) getActivity();
        String title = getResources().getString(R.string.search_title, mQueryWord);
        activity.setTitle(title);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "search_thread_" + mQueryWord;
    }

    @Override
    public void onItemClick(View view, int position) {
        Thread thread = mAdapter.getItem(position);

        UIHelper.showPostDetail(view.getContext(), thread);
    }
}
