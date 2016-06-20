package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apollo.tianya.R;
import apollo.tianya.bean.Post;
import apollo.tianya.util.Formatter;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;

/**
 * Created by Texel on 2016/6/20.
 */
public class PostAdapter extends RecyclerBaseAdapter<Post, PostAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.body) TextView body;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;

        public ViewHolder(View itemView, ViewGroup parent) {
            super(itemView);
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

        post = mItems.get(position);
        vh.author.setText(post.getAuthor());
        vh.time.setText(Formatter.friendlyTime(post.getPostDate()));
        vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
        vh.time.setText("");
        vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + post.getAuthorId());
    }
}
