package apollo.tianya.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apollo.tianya.R;
import apollo.tianya.bean.Message;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.Transforms;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kuibo on 2016/7/23.
 */
public class MessageAdapter extends RecyclerBaseAdapter<Message, MessageAdapter.ViewHolder> {

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class NormalViewHolder extends ViewHolder {

        @BindView(R.id.body) TextView content;
        @BindView(R.id.author) TextView author;

        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;

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

    protected boolean hasFooterView(){
        return true;
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_msg, viewGroup, false);
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

        } else {
            Message msg = null;
            String body = null;
            SpannableString span_body = null;
            NormalViewHolder vh = null;

            msg = mItems.get(position);

            vh = (NormalViewHolder) holder;
            vh.author.setText(msg.getSenderName());
            vh.time.setText(Formatter.friendlyTime(msg.getPostDate()));
            vh.face.setUserInfo(msg.getSenderId(), msg.getSenderName());

            body = Transforms.formatPost(msg.getValue());
            span_body = new SpannableString(body);
            vh.content.setText(span_body);
        }
    }
}
