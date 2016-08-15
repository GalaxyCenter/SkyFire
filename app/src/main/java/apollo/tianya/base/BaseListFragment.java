package apollo.tianya.base;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Entity;
import apollo.tianya.cache.CacheManager;
import apollo.tianya.util.CompatibleUtil;
import apollo.tianya.util.TLog;
import apollo.tianya.widget.EmptyLayout;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by kuibo on 2016/6/1.
 */
public abstract class BaseListFragment<T extends Entity> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, RecyclerBaseAdapter.OnItemClickListener {

    /**
     * HTTP LIST内容解析任务类
     */
    private class ParserTask extends AsyncTask<Void, Void, DataSet<T>> {

        private final byte[] responseData;
        private boolean parserError;

        public ParserTask(byte[] data) {
            this.responseData = data;
        }

        @Override
        protected DataSet<T> doInBackground(Void... voids) {
            DataSet<T> dataset = null;

            try {
                dataset = parseList(responseData);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                parserError = true;
            }

            return dataset;
        }

        @Override
        protected void onPostExecute(DataSet<T> dataset) {
            if (parserError) {

            } else {
                SaveCacheTask task = new SaveCacheTask();
                task.execute(getActivity(), dataset, getCacheKey());

                executeOnLoadDataSuccess(dataset);
                executeOnLoadFinish();
            }
        }
    }

    private class CacheTask extends AsyncTask<String, Void, DataSet<T>> {

        private final WeakReference<Context> mContext;

        private CacheTask(Context ctx) {
            mContext = new WeakReference<Context>(ctx);
        }

        @Override
        protected DataSet<T> doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(), params[0]);

            return seri == null ? null : (DataSet<T>)seri;
        }

        @Override
        protected void onPostExecute(DataSet<T> datas) {
            if (datas != null) {
                executeOnLoadDataSuccess(datas);
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    private class SaveCacheTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            Context ctx = (Context) params[0];
            Serializable seri = (Serializable) params[1];
            String key = (String) params[2];

            CacheManager.saveObject(ctx, seri, key);
            return null;
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }
                executeParserTask(responseBody);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private String TAG = this.getClass().getName();

    @BindView(R.id.listview)
    protected RecyclerView mListView;

    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected String mCatalog = null;
    protected int mPageIndex = 1;

    protected RecyclerBaseAdapter<T, RecyclerView.ViewHolder> mAdapter;
    private ParserTask mParserTask = null;
    private AsyncTask<String, Void, DataSet<T>> mCacheTask;

    protected abstract RecyclerBaseAdapter<T, RecyclerView.ViewHolder> getListAdapter();

    protected abstract DataSet<T> parseList(byte[] datas);

    protected abstract void sendRequestData();

    protected abstract String getCacheKeyPrefix();

    protected void onRefreshNetworkSuccess() {}

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if (args != null) {
            mCatalog = args.getString(Constants.BUNDLE_KEY_ARGS);
        }
    }

    @Override
    public void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (mAdapter == null) {
            mAdapter = getListAdapter();
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
        mAdapter.setOnItemClickListener(this);

        final LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mListView.setLayoutManager(llm);
        mListView.setAdapter(mAdapter);
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mAdapter == null || mAdapter.getRawItemCount() == 0) {
                    return;
                }
                // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
                if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
                    return;
                }
                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (llm.findLastVisibleItemPosition() == llm.getItemCount() - 1)
                        scrollEnd = true;
                } catch (Exception e) {
                    scrollEnd = false;
                }

                if (mState == STATE_NONE && scrollEnd) {
                    mPageIndex++;
                    mState = STATE_LOADMORE;
                    mAdapter.setState(RecyclerBaseAdapter.STATE_LOAD_MORE);
                    requestData(false);
                }
            }
        });
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        mState = STATE_LOADMORE;
        requestData(false);
    }

    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH)
            return;

        setSwipeRefreshLoadingState();
        mState = STATE_REFRESH;
        mPageIndex = 1;
        mAdapter.setState(RecyclerBaseAdapter.STATE_PULL);
        mAdapter.clear();
        requestData(true);
    }

    protected void executeOnLoadDataSuccess(DataSet<T> ds) {
        int state;

        if (ds.getObjects().size() == 0) {
            state = RecyclerBaseAdapter.STATE_NO_MORE;
        } else {
            state = RecyclerBaseAdapter.STATE_LOAD_MORE;
        }
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);

        mAdapter.setState(state);
        mAdapter.addItems(ds.getObjects());
        mAdapter.notifyDataSetChanged();
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    protected void executeOnLoadDataError(String error) {
        if (mPageIndex == 1
                && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {

            //在无网络时，滚动到底部时，mCurrentPage先自加了，然而在失败时却
            //没有减回来，如果刻意在无网络的情况下上拉，可以出现漏页问题
            mPageIndex--;

            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(RecyclerBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 获取数据
     * @param refresh 是否刷新数据
     */
    protected void requestData(boolean refresh) {
        String key = getCacheKey();
        if (!refresh && isReadCacheData()) {
            readCacheData(key);

            TLog.log(TAG, "requestData cache:" + key);
        } else {
            // 取新的数据
            sendRequestData();

            TLog.log(TAG, "requestData network:" + key);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 判断是否需要读取缓存数据
     * @return
     */
    protected boolean isReadCacheData() {
        String key = getCacheKey();
        Activity activity = getActivity();

        // 没有网络的情况下 读取缓存数据
        if (!CompatibleUtil.hasInternet())
            return true;

        // 缓存没失效 读取缓存数据
        if (!CacheManager.isCacheDataFailure(activity, key))
            return true;

        return false;
    }

    private void executeParserTask(byte[] data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mPageIndex).toString();
    }

    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }
}
