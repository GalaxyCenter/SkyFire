package apollo.tianya.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Texel on 2016/6/2.
 */
public abstract class RecyclerBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public interface DisplayFloorHandle<T> {
        void setFloor(TextView view, T t, int position);
    }

    public static final int STATE_EMPTY_ITEM = 0;
    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_PULL = 2;
    public static final int STATE_NO_MORE = 3;
    public static final int STATE_NO_DATA = 4;
    public static final int STATE_LESS_ONE_PAGE = 5;
    public static final int STATE_NETWORK_ERROR = 6;


    protected int state = STATE_LESS_ONE_PAGE;

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_HEADER = 1;
    public final static int TYPE_FOOTER = 2;

    protected List<T> mItems;
    private LayoutInflater mInflater;
    private LinearLayout mFooterView;

    protected OnItemClickListener mItemClickListener;
    protected DisplayFloorHandle mDisplayFloorHandle;

    public abstract VH getViewHolder(ViewGroup viewGroup);
    public abstract VH getHeaderViewHolder(ViewGroup viewGroup);
    public abstract VH getFooterViewHolder(ViewGroup viewGroup);

    public RecyclerBaseAdapter() {
        mItems = new ArrayList<T>();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        int count = mItems.size();

        if (hasHeaderView())
            count ++;

        if (hasFooterView())
            count ++;

        return count;
    }

    public int getRawItemCount() {
        return mItems.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            return getHeaderViewHolder(parent);
        } else if (viewType == TYPE_FOOTER) {
            return getFooterViewHolder(parent);
        } else {
            final VH vh = getViewHolder(parent);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = vh.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mItemClickListener != null)
                        mItemClickListener.onItemClick(view, position);
                }
            });
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_NORMAL;

        if (hasHeaderView() && position == 0)
            type = TYPE_HEADER;

        if (hasFooterView() && position == getItemCount() - 1)
            type = TYPE_FOOTER;

        return type;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mItemClickListener = l;
    }

    public void setDisplayFloor(DisplayFloorHandle<T> callBack) {
        mDisplayFloorHandle = callBack;
    }

    protected boolean hasFooterView(){
        return false;
    }

    protected boolean hasHeaderView(){
        return false;
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

    public void setState(int state) {
        this.state = state;
        notifyDataSetChanged();
    }

    public int getState() {
        return this.state;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }
}
