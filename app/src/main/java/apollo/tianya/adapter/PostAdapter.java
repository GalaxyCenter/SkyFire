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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.body) TextView body;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;
        @BindView(R.id.floor) TextView floor;

        public ViewHolder(View itemView, ViewGroup parent) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public PostAdapter.ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_post, null);
        return new ViewHolder(v, viewGroup);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder vh, int position) {
        Post post = null;
        SpannableString span_body = null;
        String body = null;
        post = mItems.get(position);
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
