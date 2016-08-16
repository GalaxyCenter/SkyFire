package apollo.tianya.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.util.UIHelper;

/**
 * Created by Texel on 2016/8/12.
 */
public class ThreadsFragment extends BaseListFragment<Thread> {

    private String mSectionId;
    private long mNextId;

    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        String body = null;
        DataSet<Thread> dataset = null;

        body = new String(datas);
        dataset = TianyaParser.parseThreads(body);
        return dataset;
    }

    @Override
    protected void executeOnLoadDataSuccess(DataSet<Thread> datas) {
        if (datas != null && datas.getObjects().size() > 0) {
            List<Thread> list = datas.getObjects();
            Thread thread = list.get(list.size() - 1);

            mNextId = thread.getPostDate().getTime();

            DetailActivity activity = (DetailActivity) getActivity();
            activity.setTitle(thread.getSectionName());
        }

        super.executeOnLoadDataSuccess(datas);
    }

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH)
            return;

        setSwipeRefreshLoadingState();
        mState = STATE_REFRESH;
        mNextId = 0;
        mAdapter.clear();
        requestData(true);
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.getThreads(mSectionId, mNextId, mHandler);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "threads_" + mSectionId + "_" + mNextId;
    }

    @Override
    public void onItemClick(View view, int position) {
        Thread thread = mAdapter.getItem(position);

        UIHelper.showPostDetail(view.getContext(), thread);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void initView(View view) {
        mSectionId = getActivity().getIntent().getStringExtra(Constants.BUNDLE_KEY_SECTION_ID);

        super.initView(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.thread_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_fav:
                Snackbar.make(mListView, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.menu_share:
                Snackbar.make(mListView, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.menu_search:
                Snackbar.make(mListView, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
