package apollo.tianya.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.fragment.SearchFragment;
import apollo.tianya.fragment.ThreadDetailFragment;
import apollo.tianya.fragment.bar.BarBaseFragment;
import apollo.tianya.fragment.bar.BarBaseFragment.Action;
import apollo.tianya.fragment.bar.BarBaseFragment.OnActionClickListener;
import apollo.tianya.fragment.bar.InputFragment;
import apollo.tianya.fragment.bar.ToolbarFragment;
import apollo.tianya.util.TLog;

/**
 * 帖子详情Activity
 * Created by Texel on 2016/6/20.
 */
public class DetailActivity extends BaseActivity implements
        OnActionClickListener, InputFragment.OnSendListener {

    private String TAG = "DetailActivity";

    private Toolbar mToolbar = null;
    private InputFragment.OnSendListener mSendListener = null;
    private OnActionClickListener mActionClickListener = null;

    @Override
    protected void init(Bundle savedInstanceState) {
        FragmentTransaction trans = null;
        BaseFragment fragment = null;
        Intent intent = null;
        Class<? extends BaseFragment> clazz = null;
        String query = null;

        intent = getIntent();
        clazz = (Class<? extends BaseFragment>) intent.getSerializableExtra(Constants.BUNDLE_KEY_FRAGMENT);

        query = intent.getStringExtra(SearchManager.QUERY);
        if (TextUtils.isEmpty(query)) {
            try {
                fragment = clazz.newInstance();// new ThreadDetailFragment();
            } catch (Exception ex) {
                TLog.error(ex.getMessage());
            }
        } else {
            fragment = new SearchFragment();
        }
        trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();

        if (fragment instanceof InputFragment.OnSendListener)
            mSendListener = (InputFragment.OnSendListener) fragment;

        if (fragment instanceof OnActionClickListener)
            mActionClickListener = (OnActionClickListener) fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        int actionBarTitle = 0;

        //actionBarTitle = R.string.actionbar_title_detail;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActionClick(Action action) {
        switch (action) {
            case ACTION_CHANGE:

                break;

            case ACTION_FLIGHT:
                break;
        }
    }

    @Override
    public void onSend(Editable editor) {
        mSendListener.onSend(editor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }
}
