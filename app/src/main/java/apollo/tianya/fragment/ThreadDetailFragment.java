package apollo.tianya.fragment;

import android.view.View;
import android.widget.AdapterView;

import apollo.tianya.adapter.PostAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.UIHelper;

/**
 * Created by Texel on 2016/6/20.
 */
public class ThreadDetailFragment extends BaseListFragment<Post> {

    private String mSection;
    private String mThreadId;
    private int mPageIndex;

    @Override
    public void initView(View view) {
        mSection = getActivity().getIntent().getStringExtra(UIHelper.BUNDLE_KEY_SECTION_ID);
        mThreadId = getActivity().getIntent().getStringExtra(UIHelper.BUNDLE_KEY_THREAD_ID);
        mPageIndex = getActivity().getIntent().getIntExtra(UIHelper.BUNDLE_KEY_PAGE_INDEX, 1);
    }

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
    protected void sendRequestData() {
        TianyaApi.getPosts(mSection, mThreadId, mPageIndex, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {
    }
}
