package apollo.tianya.fragment;

import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Thread;

/**
 * Created by Texel on 2016/7/15.
 */
public class BookMarksFragment extends ChannelFragment {

    @Override
    protected DataSet<Thread> parseList(byte[] datas) {
        DataSet<Thread> dataset = null;
        String body = null;

        body = new String(datas);
        dataset = TianyaParser.parseBookmarks(body);

        return dataset;
    }
    @Override
    protected void sendRequestData() {
        TianyaApi.getBookMarks(mPageIndex, mHandler);
    }
}
