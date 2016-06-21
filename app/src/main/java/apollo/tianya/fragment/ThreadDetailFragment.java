package apollo.tianya.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import apollo.tianya.R;
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
public class ThreadDetailFragment extends BaseListFragment<Post> implements
        RecyclerBaseAdapter.DisplayFloorHandle<Post> {

    private String mSectionId;
    private String mThreadId;
    private String mAuthor;
    private int mPageIndex;
    private int mFloor;

    @Override
    public void initView(View view) {
        mSectionId = getActivity().getIntent().getStringExtra(UIHelper.BUNDLE_KEY_SECTION_ID);
        mThreadId = getActivity().getIntent().getStringExtra(UIHelper.BUNDLE_KEY_THREAD_ID);
        mAuthor = getActivity().getIntent().getStringExtra(UIHelper.BUNDLE_KEY_AUTHOR);
        mPageIndex = getActivity().getIntent().getIntExtra(UIHelper.BUNDLE_KEY_PAGE_INDEX, 1);

        super.initView(view);

        mAdapter.setDisplayFloor(this);
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
        TianyaApi.getPosts(mSectionId, mThreadId, mPageIndex, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }


    @Override
    public void setFloor(TextView view, Post post, int position) {
        if (mAuthor.equals(post.getAuthor())) {
            view.setText(R.string.post_floor_owner);
            view.setBackgroundResource(R.drawable.post_floor_owner);
            view.setTextColor(getResources().getColor(R.color.post_floor_owner));
        } else {
            view.setText(Integer.toString(mFloor + position)
                    + getResources().getText(R.string.post_floor));
            view.setBackgroundResource(R.drawable.post_floor);
            view.setTextColor(getResources().getColor(R.color.post_floor));
        }
    }
}
