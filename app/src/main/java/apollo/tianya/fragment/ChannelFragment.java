package apollo.tianya.fragment;


import apollo.tianya.adapter.ListBaseAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaApi;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by kuibo on 2016/6/1.
 */
public class ChannelFragment extends BaseListFragment {

    @Override
    protected ListBaseAdapter getListAdapter() {
        return new ThreadAdapter();
    }

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
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
