package apollo.tianya.fragment;

import android.view.View;

import apollo.tianya.R;
import apollo.tianya.adapter.MessageAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Message;

/**
 * Created by kuibo on 2016/7/23.
 */
public class MessageFragment extends BaseListFragment<Message> {

    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new MessageAdapter();
    }

    @Override
    protected DataSet<Message> parseList(byte[] datas) {
        DataSet<Message> dataset = null;
        String body = null;

        body = new String(datas);
        if (mCatalog.equals(getString(R.string.channel_messages)))
            dataset = TianyaParser.parseMessages(body);
        else if (mCatalog.equals(getString(R.string.channel_notifications)))
            dataset = TianyaParser.parseNotifications(body);

        return dataset;
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog.equals(getString(R.string.channel_messages)))
            TianyaApi.getMessages(mPageIndex, 20, mHandler);
        else if (mCatalog.equals(getString(R.string.channel_notifications)))
            TianyaApi.getNotifications(mPageIndex, 20, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }
}
