package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apollo.tianya.R;
import apollo.tianya.bean.Post;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.Transforms;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Texel on 2016/6/20.
 */
public class PostAdapter extends RecyclerBaseAdapter<Post, PostAdapter.ViewHolder> {

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NormalViewHolder extends ViewHolder {

        @BindView(R.id.body) TextView body;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;
        @BindView(R.id.floor) TextView floor;

        public NormalViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public PostAdapter.ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_post, viewGroup, false);
        return new NormalViewHolder(v);
    }

    @Override
    public ViewHolder getFootViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_cell_footer, viewGroup, false);
        return new FooterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == TYPE_FOOTER) {

        } else {
            Post post = null;
            SpannableString span_body = null;
            String body = null;
            NormalViewHolder vh = null;

            post = mItems.get(position);
            vh = (NormalViewHolder) holder;

            vh.author.setText(post.getAuthor());
            vh.time.setText(Formatter.friendlyTime(post.getPostDate()));
            vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
            vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + post.getAuthorId());

            if (mDisplayFloorHandle != null)
                mDisplayFloorHandle.setFloor(vh.floor, post, position);

            body = Transforms.formatPost(post.getBody());
            span_body = new SpannableString(body);
            vh.body.setText(span_body);
        }
    }

    protected boolean hasFooterView(){
        return true;
    }
}
