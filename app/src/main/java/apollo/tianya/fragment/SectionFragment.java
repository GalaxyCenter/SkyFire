package apollo.tianya.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import apollo.tianya.R;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.adapter.SectionAdapter;
import apollo.tianya.adapter.ThreadAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Section;

/**
 * Created by kuibo on 2016/7/27.
 */
public class SectionFragment extends BaseListFragment<Section> {
    @Override
    protected RecyclerBaseAdapter<Section, RecyclerView.ViewHolder> getListAdapter() {
        return new SectionAdapter();
    }

    @Override
    protected DataSet<Section> parseList(byte[] datas) {
        DataSet<Section> dataset = null;
        String body = null;

        body = new String(datas);

        if (mCatalog.equals(getString(R.string.channel_user_section_bookmarks)))
            dataset = TianyaParser.parseSectionBookMarks(body);

        return dataset;
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog.equals(getString(R.string.channel_user_section_bookmarks)))
            TianyaApi.getBookMarks(mPageIndex, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }
}
