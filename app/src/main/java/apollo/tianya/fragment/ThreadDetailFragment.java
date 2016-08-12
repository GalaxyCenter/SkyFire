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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import apollo.tianya.ui.DetailActivity;
import apollo.tianya.util.Regex;
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
    private String mFilterAuthor;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

                if (holder instanceof PostAdapter.PostItemViewHolder) {
                    final PostAdapter.PostItemViewHolder vh = (PostAdapter.PostItemViewHolder) holder;

                    vh.filter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Snackbar.make(vh.copy, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                            //        .setAction("Action", null).show();
                            mFilterAuthor = post.getAuthor();
                            mPageIndex = 1;
                            mAdapter.clear();
                            requestData(false);
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
                } else if (holder instanceof PostAdapter.HeaderViewHolder) {
                    final PostAdapter.HeaderViewHolder vh = (PostAdapter.HeaderViewHolder) holder;

                    vh.section.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Thread thread = (Thread) post;
                            UIHelper.showThreadsActivity(getContext(), thread.getSectionId());
                        }
                    });
                }

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
    protected void executeOnLoadDataSuccess(DataSet<Post> data) {
        if (mPageIndex == 1 && data != null && data.getObjects().size() != 0) {
            Post p = data.getObjects().get(0);
            String regex_str = "(?s)<img.*?original=\"(.*?)\".*?[/]?>";
            List<Map<String,Object>> list = null;
            Map<String,Object> map = null;
            String img_src = null;
            DetailActivity activity = (DetailActivity) getActivity();

            list = Regex.getStartAndEndIndex(p.getBody(), Pattern.compile(regex_str, Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
            for(int i=0; i<list.size(); i++) {
                map = list.get(i);
                img_src = (String) map.get("str1");
                break;
            }

            if (!TextUtils.isEmpty(img_src)) {
                activity.setCover(img_src);
                activity.setExpanded(true);
            }
        }
        if (!TextUtils.isEmpty(mFilterAuthor)) {
            List<Post> raws = data.getObjects();
            List<Post> filted = new ArrayList<Post>();

            for(int i=1; i<raws.size(); i++) {
                if (mFilterAuthor.equals(raws.get(i).getAuthor())) {
                    filted.add(raws.get(i));
                }
            }

            data.setObjects(filted);
        }
        super.executeOnLoadDataSuccess(data);
    }

    @Override
    protected void sendRequestData() {
        TianyaApi.getPosts(mSectionId, mThreadId, mPageIndex, mHandler);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "thread_" + mSectionId + "_" + mThreadId;
    }

    @Override
    public void onItemClick(View view, int postion) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.thread_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_fav:
                Thread thread = (Thread) (Object) mAdapter.getItem(0);

                if (isAddBookmark)
                    TianyaApi.removeBookmark(thread, mRemoveBookMarkHandle);
                else
                    TianyaApi.addBookmark(thread, mAddBookMarkHandle);
                break;

            case R.id.menu_jump:
                mFlightDialog.show(getActivity().getSupportFragmentManager(), "DD");
                break;

            case R.id.menu_share:
                Snackbar.make(mListView, getActivity().getString(R.string.unsuport), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.menu_mark:
                String content = getResources().getString(R.string.post_content_mark);
                TianyaApi.createPost(mSectionId, mThreadId, "", content, mCreatePostHandler);
                break;
        }
        return super.onOptionsItemSelected(item);
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
