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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.body) TextView content;
        @BindView(R.id.author) TextView author;

        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;

        public ViewHolder(View itemView, ViewGroup parent) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MessageAdapter.ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_msg, null);
        return new ViewHolder(v, viewGroup);
    }

    @Override
    public MessageAdapter.ViewHolder getFootViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder vh, int position) {
        Message msg = null;
        String body = null;
        SpannableString span_body = null;

        msg = mItems.get(position);

        vh.author.setText(msg.getSenderName());
        vh.time.setText(Formatter.friendlyTime(msg.getPostDate()));
        vh.face.setUserInfo(msg.getSenderId(), msg.getSenderName());

        body = Transforms.formatPost(msg.getValue());
        span_body = new SpannableString(body);
        vh.content.setText(span_body);
    }
}
