package apollo.tianya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import apollo.tianya.R;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.fragment.BookMarksFrament;

/**
 * Created by Texel on 2016/7/15.
 */
public class SimpleBackActivity extends BaseActivity {

    public static enum SimpleBackPage {

        BOOKMARKS(1, R.string.actionbar_title_bookmarks, BookMarksFrament.class),
        HISTORIES(2, R.string.actionbar_title_histories, BookMarksFrament.class),
        POSTS(3, R.string.actionbar_title_posts, BookMarksFrament.class);

        int title;
        Class<? extends BaseFragment> refer;
        int value;

        private SimpleBackPage(int value, int title, Class<? extends BaseFragment> refer) {
            this.value = value;
            this.title = title;
            this.refer = refer;
        }

        public int getTitle() {
            return title;
        }

        public void setTitle(int title) {
            this.title = title;
        }

        public Class<?> getRefer() {
            return refer;
        }

        public void setRefer(Class<? extends BaseFragment> refer) {
            this.refer = refer;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public static SimpleBackPage getPageByValue(int val) {
            for (SimpleBackPage p : values()) {
                if (p.getValue() == val)
                    return p;
            }
            return null;
        }
    }

    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";

    private static final String TAG = "SimpleBackActivity";
    protected WeakReference<Fragment> mFragment;
    protected int mPageValue = -1;
    protected SimpleBackPage mPage;

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = null;
        int actionBarTitle = 0;

        actionBarTitle = mPage.title;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(actionBarTitle);
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

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }

        mPage = SimpleBackPage.getPageByValue(pageValue);
        if (mPage == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }
    }
}
