package apollo.tianya.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.Transforms;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Texel on 2016/6/20.
 */
public class PostAdapter extends RecyclerBaseAdapter<Post, PostAdapter.ViewHolder> {

    public interface PostOptionHandle {
        void setOption(ViewHolder holder, Post post, int position);
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class PostItemViewHolder extends ViewHolder {

        Post post = null;

        @BindView(R.id.body) TextView body;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;
        @BindView(R.id.floor) TextView floor;

        @BindView(R.id.opt_filter) public TextView filter;
        @BindView(R.id.opt_comment) public TextView comment;
        @BindView(R.id.opt_copy) public TextView copy;
        @BindView(R.id.opt_quote) public TextView quote;

        public PostItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderViewHolder extends ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.section) TextView section;
        @BindView(R.id.views) TextView views;
        @BindView(R.id.replies) TextView replies;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    private PostOptionHandle mPostOptionHandle;

    public void setPostOptionHandle(PostOptionHandle callBack) {
        mPostOptionHandle = callBack;
    }

    @Override
    public PostAdapter.ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_post, viewGroup, false);
        return new PostItemViewHolder(v);
    }

    @Override
    public ViewHolder getHeaderViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_post_header, viewGroup, false);

        return new HeaderViewHolder(v);
    }

    @Override
    public ViewHolder getFooterViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_cell_footer, viewGroup, false);
        return new FooterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == TYPE_HEADER) {
            HeaderViewHolder vh = null;
            Thread thread = null;

            if (mItems.size() == 0)
                return;

            vh = (HeaderViewHolder) holder;
            thread = (Thread) mItems.get(0);

            vh.title.setText(thread.getTitle());
            vh.views.setText(Integer.toString(thread.getViews()));
            vh.replies.setText(Integer.toString(thread.getReplies()));
            vh.section.setText(thread.getSectionName());

        } else if (itemType == TYPE_FOOTER) {

        } else {
            Post post = null;
            SpannableString span_body = null;
            String body = null;
            PostItemViewHolder vh = null;

            position = getRealPosition(holder);
            post = mItems.get(position);
            vh = (PostItemViewHolder) holder;

            vh.post = post;
            vh.author.setText(post.getAuthor());
            vh.time.setText(Formatter.friendlyTime(post.getPostDate()));
            vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
            vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + post.getAuthorId());

            if (mDisplayFloorHandle != null)
                mDisplayFloorHandle.setFloor(vh.floor, post, position);

            if (mPostOptionHandle != null)
                mPostOptionHandle.setOption(vh, post, position);

            if (!TextUtils.isEmpty(post.getBody())) {
                body = Transforms.formatPost(post.getBody());
                span_body = new SpannableString(body);
                vh.body.setText(span_body);
            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return hasHeaderView() ? position - 1 : position ;
    }

    protected boolean hasFooterView(){
        return true;
    }

    protected boolean hasHeaderView(){
        return true;
    }
}
