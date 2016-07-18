package apollo.tianya.fragment;

import android.os.Bundle;

import java.util.List;

import apollo.tianya.R;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by Texel on 2016/7/18.
 */
public class UserPostsFragment extends ChannelFragment {

    public static class DataSetEx {
        public int pubNextId;
        public int techNextId;
        public int cityNextId;
        public DataSet<Thread> dataset;
    }

    private int mPubNextId = Integer.MAX_VALUE;
    private int mTechNextId = Integer.MAX_VALUE;
    private int mCityNextId = Integer.MAX_VALUE;
    private int mUserId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if (args != null) {
            mUserId = args.getInt(Constants.BUNDLE_KEY_USER_ID);
        }
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        DataSetEx dataset = null;
        String body = null;

        body = new String(datas);
        dataset = TianyaParser.parseUserPosts(body);

        mPubNextId = dataset.pubNextId;
        mTechNextId = dataset.techNextId;
        mCityNextId = dataset.cityNextId;

        return dataset.dataset;
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog.equals(getString(R.string.channel_user_threads)))
            TianyaApi.getUserThreads(mUserId, mPubNextId, mTechNextId, mCityNextId, mHandler);
        else if (mCatalog.equals(getString(R.string.channel_user_posts)))
            TianyaApi.getUserPosts(mUserId, mPubNextId, mTechNextId, mCityNextId, mHandler);
    }
}
