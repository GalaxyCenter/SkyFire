package apollo.tianya.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;

import apollo.tianya.R;
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
        Toolbar toolbar = null;
        int actionBarTitle = 0;

        actionBarTitle = R.string.actionbar_title_detail;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(actionBarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
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

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, ViewCompat.isLaidOut(mAppBar));
    }

    public void setExpanded(boolean expanded, boolean animate) {
        mAppBar.setExpanded(expanded, animate);
    }
}
