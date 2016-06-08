package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apollo.tianya.R;
import apollo.tianya.bean.Thread;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Texel on 2016/6/2.
 */
public class ThreadAdapter extends RecyclerBaseAdapter<Thread, ThreadAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.views) TextView views;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_thread, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Thread thread;

        thread = mItems.get(position);
        vh.title.setText(thread.getTitle());
        vh.author.setText(thread.getAuthor());
        vh.views.setText(Integer.toString(thread.getViews()));
    }
}
