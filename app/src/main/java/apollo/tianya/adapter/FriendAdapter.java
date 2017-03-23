package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import apollo.tianya.R;
import apollo.tianya.bean.User;

/**
 * Created by Texel on 2016/10/31.
 */

public class FriendAdapter extends RecyclerBaseAdapter<User, FriendAdapter.ViewHolder> {

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FriendItemViewHolder extends FriendAdapter.ViewHolder {

        public FriendItemViewHolder(View itemView, ViewGroup parent) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_thread, null);
        return new FriendItemViewHolder(v, viewGroup);
    }

    @Override
    public ViewHolder getHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public ViewHolder getFooterViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


}
