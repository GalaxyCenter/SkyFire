package apollo.tianya.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import apollo.tianya.R;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.util.Formatter;
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

        AsyncHttpResponseHandlerEx handle = null;
        ParserTask task = null;

        public ViewHolder(View itemView) {
            super(itemView);

            handle = new AsyncHttpResponseHandlerEx();
            handle.vh = this;

            ButterKnife.bind(this, itemView);
        }
    }

    static class AsyncHttpResponseHandlerEx extends com.loopj.android.http.AsyncHttpResponseHandler {

        ViewHolder vh;

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (vh.task != null)
                vh.task.cancel(true);

            vh.task = new ParserTask(responseBody);
            vh.task.execute();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    static class ParserTask extends AsyncTask<Void, Void, DataSet<Post>> {

        private final byte[] responseData;
        private boolean parserError;

        public ParserTask(byte[] data) {
            this.responseData = data;
        }

        @Override
        protected DataSet<Post> doInBackground(Void... voids) {
            DataSet<Post> dataset = null;

            try {
                dataset = TianyaParser.parsePosts(new String(responseData));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                parserError = true;
            }

            return dataset;
        }

        @Override
        protected void onPostExecute(DataSet<Post> dataset) {
            Log.i(TAG, "ThreadAdapter@ParserTask OK");
        }
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup viewGroup) {
        View v = getLayoutInflater(viewGroup.getContext()).inflate(R.layout.list_item_thread, null);
        return new ViewHolder(v);
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
        vh.face.setAvatarUrl("http://tx.tianyaui.com/logo/1749397");

        TianyaApi.getPosts(thread.getUrl(), vh.handle);
    }
}
