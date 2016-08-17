package apollo.tianya.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Section;
import apollo.tianya.bean.Thread;
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.util.UIHelper;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/8/12.
 */
public class ThreadsFragment extends BaseListFragment<Thread> {

    private final AsyncHttpResponseHandler mAddRemoveBookMarkHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String body = null;

            body = new String(responseBody);
            String message = null;
            JSONObject json = null;
            boolean isSuccess = false;

            try {
                json = new JSONObject(body);
                isSuccess = json.getInt("success") == 1;
                message = json.getString("message");
            } catch (JSONException ex) {
            }

            if (isSuccess == false) {
                Snackbar.make(mListView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                if ("用户加入版块成功！".equals(message)) {
                    Snackbar.make(mListView, R.string.add_bookmark_success, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(mListView, R.string.remove_bookmark_success, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private String mSectionId;
    private long mNextId;
    private Section mSection;

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

            if (mNextId == 0)
                mSection = thread.getSection();

            mNextId = thread.getPostDate().getTime();

            DetailActivity activity = (DetailActivity) getActivity();
            activity.setTitle(thread.getSectionName());

            activity.invalidateOptionsMenu();
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

        if (mSection != null && mSection.isFav())
            menu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_bookmark_selected_24dp);
        else
            menu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_bookmark_normal_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_fav:
                if (!mSection.isFav()) {
                    TianyaApi.addBookmark(mSection, mAddRemoveBookMarkHandler);
                    mSection.setFav(true);
                    item.setIcon(R.drawable.ic_bookmark_selected_24dp);
                } else {
                    TianyaApi.removeBookmark(mSection, mAddRemoveBookMarkHandler);
                    mSection.setFav(false);
                    item.setIcon(R.drawable.ic_bookmark_normal_24dp);
                }
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
