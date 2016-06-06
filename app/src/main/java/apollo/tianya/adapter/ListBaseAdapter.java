package apollo.tianya.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.bean.Entity;

/**
 * Created by Texel on 2016/6/2.
 */
public abstract class ListBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mItems;
    private LayoutInflater mInflater;

    public abstract VH getViewHolder(ViewGroup viewGroup);

    public ListBaseAdapter() {
        mItems = new ArrayList<T>();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent);
    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    public void setData(List<T> items) {
        mItems = items;

        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItem(T i) {
        mItems.add(i);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
