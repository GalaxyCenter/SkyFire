package apollo.tianya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageInfo;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.fragment.BookMarksFrament;

/**
 * Created by Texel on 2016/7/15.
 */
public class SimpleBackActivity extends BaseActivity {

    private static final String TAG = "SimpleBackActivity";
    protected WeakReference<Fragment> mFragment;
    protected ViewPageInfo mPageInfo;

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();

        mPageInfo = (ViewPageInfo) intent.getParcelableExtra(Constants.BUNDLE_KEY_PAGEINFO);
        initFromIntent(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = null;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mPageInfo.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initFromIntent(Intent intent) {
        if (intent == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }

        if (mPageInfo == null) {
            throw new IllegalArgumentException("can not find page");
        }

        try {
            Fragment fragment = (Fragment) mPageInfo.refer.newInstance();

            Bundle args = intent.getBundleExtra(Constants.BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commitAllowingStateLoss();

            mFragment = new WeakReference<Fragment>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:");
        }
    }
}
