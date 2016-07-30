package apollo.tianya.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.adapter.PostAdapter;
import apollo.tianya.adapter.RecyclerBaseAdapter;
import apollo.tianya.api.TianyaParser;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseListFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.DataSet;
import apollo.tianya.bean.Post;
import apollo.tianya.bean.Thread;
import apollo.tianya.fragment.bar.BarBaseFragment;
import apollo.tianya.fragment.bar.InputFragment;
import apollo.tianya.util.Transforms;
import apollo.tianya.util.UIHelper;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Texel on 2016/6/20.
 */
public class ThreadDetailFragment extends BaseListFragment<Post> implements
        RecyclerBaseAdapter.DisplayFloorHandle<Post>, InputFragment.OnSendListener,
        BarBaseFragment.OnActionClickListener {

    @SuppressLint("ValidFragment")
    public class FlightDialogFragment extends DialogFragment {

        private EditText mFloor;

        public FlightDialogFragment() {}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dlg_flight, null);

            mFloor = (EditText) view.findViewById(R.id.floor);

            builder.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String tmp = mFloor.getText().toString();

                    if (!TextUtils.isEmpty(tmp)) {
                        ThreadDetailFragment.this.skip2Floor(Integer.parseInt(tmp));
                        FlightDialogFragment.this.dismiss();
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FlightDialogFragment.this.dismiss();
                }
            });
            return builder.create();
        }

    }

    private FlightDialogFragment mFlightDialog = null;

    private String mSectionId;
    private String mThreadId;
    private String mAuthor;
    private int mFloor;
    private boolean isAddBookmark;

    private final AsyncHttpResponseHandler mAddBookMarkHandle = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String body = null;

            body = new String(responseBody); // {"message":"收藏帖子成功！","data":{},"code":"1","success":1}
            String message = null;
            JSONObject json = null;
            boolean isSuccess = false;

            try {
                json = new JSONObject(body);
                isSuccess = json.getInt("success") == 1;
                message = json.getString("message");
            } catch (JSONException ex) {
            }

            if (isSuccess == false) {
                Snackbar.make(mListView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(mListView, R.string.add_bookmark_success, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                isAddBookmark = true;
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private final AsyncHttpResponseHandler mRemoveBookMarkHandle = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String body = null;

            body = new String(responseBody); // {"message":"收藏帖子成功！","data":{},"code":"1","success":1}
            String message = null;
            JSONObject json = null;
            boolean isSuccess = false;

            try {
                json = new JSONObject(body);
                isSuccess = json.getInt("success") == 1;
                message = json.getString("message");
            } catch (JSONException ex) {
            }

            if (isSuccess == false) {
                Snackbar.make(mListView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(mListView, R.string.remove_bookmark_success, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                isAddBookmark = false;
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private final AsyncHttpResponseHandler mCreatePostHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String body = null;

            body = new String(responseBody);
            String message = null;
            JSONObject json = null;
            boolean isSuccess = false;
            try {
                json = new JSONObject(body);
                isSuccess = "1".equals(json.get("success"));
                message = json.getString("message");
            } catch (JSONException ex) {
            }

            if (isSuccess == false) {
                Snackbar.make(mListView, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(mListView, R.string.post_reply_success, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @Override
    public void initView(View view) {
        mSectionId = getActivity().getIntent().getStringExtra(Constants.BUNDLE_KEY_SECTION_ID);
        mThreadId = getActivity().getIntent().getStringExtra(Constants.BUNDLE_KEY_THREAD_ID);
        mAuthor = getActivity().getIntent().getStringExtra(Constants.BUNDLE_KEY_AUTHOR);
        mPageIndex = getActivity().getIntent().getIntExtra(Constants.BUNDLE_KEY_PAGE_INDEX, 1);

        super.initView(view);

        mAdapter.setDisplayFloor(this);
        ((PostAdapter)(Object)mAdapter).setPostOptionHandle(new PostAdapter.OptionHandle() {
            @Override
            public void handleOption(PostAdapter.ViewHolder holder, final Post post, int position) {
                final PostAdapter.PostItemViewHolder vh = (PostAdapter.PostItemViewHolder) holder;

                vh.filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(vh.copy, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                vh.copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cbm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        cbm.setText(Transforms.formatPost(post.getBody(), false));

                        Snackbar.make(vh.copy, getActivity().getString(R.string.copy_data_success), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                vh.quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(vh.copy, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                vh.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(vh.copy, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
        mFlightDialog = new FlightDialogFragment();
    }

    @Override
    protected RecyclerBaseAdapter getListAdapter() {
        return new PostAdapter();
    }

    @Override
    protected DataSet<Post> parseList(byte[] datas) {
        String body = null;
        DataSet<Post> dataset = null;

        body = new String(datas);
        dataset = TianyaParser.parsePosts(body);
        return dataset;
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.getPosts(mSectionId, mThreadId, mPageIndex, mHandler);
    }

    @Override
    public void onItemClick(View view, int postion) {
    }

    @Override
    public void setFloor(TextView view, Post post, int position) {
        if (mAuthor.equals(post.getAuthor())) {
            view.setText(R.string.post_floor_owner);
            view.setBackgroundResource(R.drawable.post_floor_owner);
            view.setTextColor(getResources().getColor(R.color.post_floor_owner));
        } else {
            view.setText(Integer.toString(mFloor + position)
                    + getResources().getText(R.string.post_floor));
            view.setBackgroundResource(R.drawable.post_floor);
            view.setTextColor(getResources().getColor(R.color.post_floor));
        }
    }

    @Override
    public void onSend(Editable editor) {
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }

        TianyaApi.createPost(mSectionId, mThreadId, "", editor.toString(), mCreatePostHandler);
    }

    @Override
    public void onActionClick(BarBaseFragment.Action action) {
        switch (action) {
            case ACTION_FLIGHT:
                mFlightDialog.show(getActivity().getSupportFragmentManager(), "DD");
                break;

            case ACTION_FAVORITE:
                Thread thread = (Thread) (Object) mAdapter.getItem(0);

                if (isAddBookmark)
                    TianyaApi.removeBookmark(thread, mRemoveBookMarkHandle);
                else
                    TianyaApi.addBookmark(thread, mAddBookMarkHandle);
                break;

            case ACTION_SHARE:

                break;
        }
    }

    private void skip2Floor(int floor) {
        Snackbar.make(mListView, R.string.unsuport, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

}
