package apollo.tianya.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.ImageView;

import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.fragment.ThreadDetailFragment;
import apollo.tianya.fragment.bar.BarBaseFragment;
import apollo.tianya.fragment.bar.BarBaseFragment.Action;
import apollo.tianya.fragment.bar.BarBaseFragment.OnActionClickListener;
import apollo.tianya.fragment.bar.InputFragment;
import apollo.tianya.fragment.bar.ToolbarFragment;

/**
 * 帖子详情Activity
 * Created by Texel on 2016/6/20.
 */
public class DetailActivity extends BaseActivity implements
        OnActionClickListener, InputFragment.OnSendListener {

    private ToolbarFragment mToolFragment = new ToolbarFragment();
    private InputFragment mInputFragment = new InputFragment();
    private BarBaseFragment mNewFragment = null;
    private AppBarLayout mAppBar;
    private ImageView mCover;
    private Toolbar mToolbar = null;
    private CollapsingToolbarLayout mToolbarLayout = null;

    private InputFragment.OnSendListener mSendListener = null;
    private OnActionClickListener mActionClickListener = null;

    @Override
    protected void init(Bundle savedInstanceState) {
        FragmentTransaction trans = null;
        BaseFragment fragment = null;

        fragment = new ThreadDetailFragment();
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

        actionBarTitle = R.string.actionbar_title_detail;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(actionBarTitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mToolbarLayout.setTitle("SEGA");
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mCover = (ImageView) findViewById(R.id.cover);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emoji_keyboard, mToolFragment).commit();
        mToolFragment.addOnActionClickListener(this);
        mToolFragment.addOnActionClickListener(mActionClickListener);
        mInputFragment.addOnActionClickListener(this);
        mInputFragment.setOnSendListener(this);

        mNewFragment = mInputFragment;
    }

    @Override
    public void onActionClick(Action action) {
        switch (action) {
            case ACTION_CHANGE:
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.footer_menu_slide_in,
                                R.anim.footer_menu_slide_out)
                        .replace(R.id.emoji_keyboard, mNewFragment)
                        .commit();

                mNewFragment = mNewFragment == mInputFragment ? mToolFragment : mInputFragment;
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
        mToolbarLayout.setTitle(title);
    }

    public void setCover(String img) {
        TianyaApi.displayImage(img, mCover);
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, ViewCompat.isLaidOut(mAppBar));
    }

    public void setExpanded(boolean expanded, boolean animate) {
        mAppBar.setExpanded(expanded, animate);
    }
}
