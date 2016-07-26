package apollo.tianya.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.bean.Post;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.Transforms;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Texel on 2016/7/25.
 */
public class PostReplyAdapter extends RecyclerBaseAdapter<Post, PostReplyAdapter.ViewHolder> {

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NormalViewHolder extends ViewHolder {

        @BindView(R.id.body) TextView body;
        @BindView(R.id.summary) TextView summary;
        @BindView(R.id.reply) TextView reply;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;

        public NormalViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class FooterViewHolder extends ViewHolder {

        @BindView(R.id.progressbar) ProgressBar progress;
        @BindView(R.id.text) TextView text;

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    protected boolean hasFooterView(){
        return true;
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_post_reply, viewGroup, false);
        return new NormalViewHolder(v);
    }

    @Override
    public ViewHolder getFootViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_cell_footer, viewGroup, false);
        return new FooterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == TYPE_FOOTER) {
            FooterViewHolder vh = null;

        } else {
            Post post = null;
            Post comment = null;
            SpannableString span_body = null;
            String summary = null;
            NormalViewHolder vh = null;

            post = mItems.get(position);
            comment = post.getComment().get(0);

            vh = (NormalViewHolder) holder;

            vh.reply.setText(comment.getAuthor());
            vh.face.setUserInfo(comment.getAuthorId(), comment.getAuthor());
            vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + comment.getAuthorId());
            if (!TextUtils.isEmpty(comment.getBody())) {
                summary = Transforms.formatPost(comment.getBody());
                span_body = new SpannableString(summary);
                vh.body.setText(span_body);
            }

            summary = post.getBody();
            if (!TextUtils.isEmpty(summary)) {
                summary = Transforms.stripHtmlXmlTags(summary);
                summary = AppContext.getInstance().getResources().getString(R.string.me)
                        + ":" + summary.trim().replace("　", "") + "//" + post.getTitle();
            } else {
                summary = post.getTitle();
            }
            vh.summary.setText(summary);
            vh.time.setText(Formatter.friendlyTime(comment.getPostDate()));

        }
    }
}
