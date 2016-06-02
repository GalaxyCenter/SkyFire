package apollo.tianya.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import apollo.tianya.bean.Entity;

/**
 * Created by Texel on 2016/6/2.
 */
public class ListBaseAdapter<T extends Entity> extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

}
