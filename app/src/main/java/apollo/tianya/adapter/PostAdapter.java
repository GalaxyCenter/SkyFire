package apollo.tianya.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.CompatibleUtil;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.SpannableUtil;
import apollo.tianya.util.Transforms;
import apollo.tianya.util.UIHelper;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Texel on 2016/6/20.
 */
public class PostAdapter extends RecyclerBaseAdapter<Post, PostAdapter.ViewHolder> {

    public interface OptionHandle {
        void handleOption(ViewHolder holder, Post post, int position);
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
        @BindView(R.id.section) public TextView section;
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

    private OptionHandle mOptionHandle;
    private int mMaxWidth;
    private int mMaxHeight;
    private DisplayImageOptions mOptions;

    public void setPostOptionHandle(OptionHandle callBack) {
        mOptionHandle = callBack;
    }

    public PostAdapter() {
        mMaxWidth = CompatibleUtil.getDisplayMetrics().widthPixels;
        mMaxHeight = CompatibleUtil.getDisplayMetrics().heightPixels;

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
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
            Post post = null;
            if (mItems.size() == 0)
                return;

            vh = (HeaderViewHolder) holder;
            post = mItems.get(0);
            if (post instanceof Thread) {
                thread = (Thread) post;

                vh.title.setText(thread.getTitle());
                vh.views.setText(Integer.toString(thread.getViews()));
                vh.replies.setText(Integer.toString(thread.getReplies()));
                vh.section.setText(thread.getSectionName());

                if (mOptionHandle != null)
                    mOptionHandle.handleOption(vh, post, position);
            }
        } else if (itemType == TYPE_FOOTER) {

        } else {
            Post post = null;
            SpannableString span_body = null;
            String body = null;
            final PostItemViewHolder vh = (PostItemViewHolder) holder;

            position = getRealPosition(holder);
            post = mItems.get(position);

            vh.post = post;
            vh.author.setText(post.getAuthor());
            vh.time.setText(Formatter.friendlyTime(post.getPostDate()));
            vh.face.setBackgroundResource(R.drawable.ic_account_circle_blue_37dp);
            vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
            vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + post.getAuthorId());

            if (mDisplayFloorHandle != null)
                mDisplayFloorHandle.setFloor(vh.floor, post, position);

            if (mOptionHandle != null)
                mOptionHandle.handleOption(vh, post, position);

            if (!TextUtils.isEmpty(post.getBody())) {
                body = Transforms.formatPost(post.getBody());
                span_body = new SpannableString(body);
                SpannableUtil.drawImage(span_body, body, SpannableUtil.DRAWABLE_LOADING, mOptions,
                        new SpannableUtil.ImageLoadedHandle() {
                            @Override
                            public void onImageLoaded(SpannableString spannable, String url, Bitmap bmp) {
                                ImageSpan[] image_spans = spannable.getSpans(0, spannable.length(), ImageSpan.class);
                                for (ImageSpan span : image_spans) {
                                    if (span.getDrawable() == SpannableUtil.DRAWABLE_LOADING
                                            && span.getSource().equals(url)) {
                                        int start = spannable.getSpanStart(span);
                                        int end = spannable.getSpanEnd(span);
                                        spannable.removeSpan(span);

                                        BitmapDrawable draw = new BitmapDrawable(AppContext.getInstance().getResources(), bmp);
                                        draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
                                        span = new ImageSpan(draw, url, ImageSpan.ALIGN_BOTTOM);
                                        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        vh.body.setText(spannable);
                                        break;
                                    }
                                }
                            }
                        }, new SpannableUtil.OnImageClickListener() {
                            @Override
                            public void onClick(View v, String url) {
                                UIHelper.showImageActivity(vh.body.getContext(), url);
                            }
                        });
                vh.body.setText(span_body);
                vh.body.setLineSpacing(0.0F, 1.2F);
                vh.body.setTextSize(AppContext.getFontSize());
                vh.body.setMovementMethod(LinkMovementMethod.getInstance());
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
