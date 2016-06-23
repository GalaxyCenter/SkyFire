package apollo.tianya.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

import apollo.tianya.R;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.DateTime;
import apollo.tianya.util.Formatter;
import apollo.tianya.util.Transforms;
import apollo.tianya.widget.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/6/2.
 */
public class ThreadAdapter extends RecyclerBaseAdapter<Thread, ThreadAdapter.ViewHolder> {

    private static String TAG = "ThreadAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.summary) TextView summary;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.views) TextView views;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.userface) AvatarView face;
        @BindView(R.id.photos) ViewPager photos;

        AsyncPostHttpResponseHandler postHandle = null;
        AsyncUserIdHttpResponseHandler userIdHandle = null;
        ParserTask task = null;

        public ViewHolder(View itemView, ViewGroup parent) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            PhotoAdapter adapter = null;

            postHandle = new AsyncPostHttpResponseHandler();
            postHandle.vh = this;

            userIdHandle = new AsyncUserIdHttpResponseHandler();
            userIdHandle.vh = this;

            adapter = new PhotoAdapter((Activity)parent.getContext());
            photos.setAdapter(adapter);
            photos.setClipChildren(false);
            photos.setClipToPadding(false);
            photos.setOffscreenPageLimit(30);
        }
    }

    static class AsyncPostHttpResponseHandler extends AsyncHttpResponseHandler {

        public ViewHolder vh;

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (vh.task != null)
                vh.task.cancel(true);

            vh.task = new ParserTask(responseBody);
            vh.task.vh = vh;
            vh.task.execute();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    static class AsyncUserIdHttpResponseHandler extends AsyncHttpResponseHandler {

        public ViewHolder vh;

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String source = new String(responseBody);
            int userId = 0;

            userId = TianyaParser.parseUserId(source);
            vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/" + userId);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    static class ParserTask extends AsyncTask<Void, Void, Post> {

        private final byte[] responseData;
        private boolean parserError;
        public ViewHolder vh;

        public ParserTask(byte[] data) {
            this.responseData = data;
        }

        @Override
        protected Post doInBackground(Void... voids) {
            DataSet<Post> dataset = null;
            String source = null;
            Post post = null;
            List<String> photos = null;

            source = new String(responseData);
            try {
                dataset = TianyaParser.parsePosts(source);

                if (dataset == null || dataset.getObjects() == null || dataset.getObjects().size() == 0)
                    post = TianyaParser.parsePage(source);
                else
                    post = dataset.getObjects().get(0);

                photos = TianyaParser.parseThreadImage(source);
                if (photos == null) {
                    String photo = null;

                    photo = TianyaParser.parseImage(source);
                    if (!TextUtils.isEmpty(photo)) {
                        photos = new ArrayList<String>();
                        photos.add(photo);
                    }
                }
                post.setPhotos(photos);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                parserError = true;
            }

            Log.i(TAG, post.getTitle() + "#" + StringUtil.join(post.getPhotos(), "#"));
            return post;
        }

        @Override
        protected void onPostExecute(Post post) {
            if (parserError) {

            } else {
                String sub_summary = null;
                PhotoAdapter adapter = null;

                sub_summary = Transforms.stripHtmlXmlTags(post.getBody());
                sub_summary = Formatter.checkStringLength(sub_summary, 100);
                vh.summary.setText(sub_summary);

                vh.views.setText(Integer.toString(post.getViews()));
                vh.time.setText(DateTime.toString(post.getPostDate()));
                if (TextUtils.isEmpty(vh.author.getText())) {
                    vh.author.setText(post.getAuthor());
                    TianyaApi.getUserId(post.getAuthor(), vh.userIdHandle);
                }

                adapter = (PhotoAdapter)vh.photos.getAdapter();
                adapter.removeAllItem();
                adapter.addItems(post.getPhotos());
            }
        }
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_thread, null);
        return new ViewHolder(v, viewGroup);
    }

    @Override
    public ViewHolder getFootViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Thread thread;

        thread = mItems.get(position);
        vh.title.setText(thread.getTitle());
        vh.author.setText(thread.getAuthor());
        vh.views.setText(Integer.toString(thread.getViews()));
        vh.time.setText(Formatter.friendlyTime(thread.getPostDate()));
        vh.face.setUserInfo(thread.getAuthorId(), thread.getAuthor());
        vh.summary.setText("");
        vh.time.setText("");
        vh.views.setText("0");

        ((PhotoAdapter)vh.photos.getAdapter()).removeAllItem();

        vh.postHandle.vh = vh;
        TianyaApi.getPosts(thread.getUrl(), vh.postHandle);

        if(!TextUtils.isEmpty(thread.getAuthor()))
            TianyaApi.getUserId(thread.getAuthor(), vh.userIdHandle);
    }
}
