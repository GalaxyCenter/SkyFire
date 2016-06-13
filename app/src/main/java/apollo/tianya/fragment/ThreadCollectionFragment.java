package apollo.tianya.fragment;

import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaApi;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by Texel on 2016/6/13.
 */
public class ThreadCollectionFragment extends BaseListFragment {
    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected DataSet parseList(byte[] datas) {
        DataSet<Thread> dataset = null;
        String body = null;

        body = new String(datas);
        dataset = TianyaParser.parseRecommendThread(body);
        return dataset;
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.getRecommendThread(mHandler);
    }


}
