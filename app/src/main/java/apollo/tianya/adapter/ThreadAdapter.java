package apollo.tianya.adapter;

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
public class ThreadAdapter extends ListBaseAdapter<Thread> {

    static class ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.views) TextView views;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        Thread thread;

        if (view == null) {
            view = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_thread, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        thread = mItems.get(i);
        vh.title.setText(thread.getTitle());
        vh.author.setText(thread.getAuthor());
        vh.views.setText(Integer.toString(thread.getViews()));

        return view;
    }
}
