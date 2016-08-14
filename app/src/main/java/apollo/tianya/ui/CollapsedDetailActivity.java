package apollo.tianya.ui;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import apollo.tianya.R;
import apollo.tianya.api.remote.TianyaApi;
import apollo.tianya.fragment.bar.InputFragment;

/**
 * Created by kuibo on 2016/8/13.
 */
public class CollapsedDetailActivity extends DetailActivity {

    private String TAG = "CommentDetailActivity"
            ;
    private InputFragment mInputFragment = new InputFragment();
    private CollapsingToolbarLayout mToolbarLayout = null;
    private AppBarLayout mAppBar;
    private ImageView mCover;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collapsed_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emoji_keyboard, mInputFragment).commit();
        mInputFragment.addOnActionClickListener(this);
        mInputFragment.setOnSendListener(this);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mCover = (ImageView) findViewById(R.id.cover);
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
