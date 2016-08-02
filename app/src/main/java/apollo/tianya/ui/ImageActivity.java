package apollo.tianya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import apollo.tianya.R;
import apollo.tianya.adapter.PhotoAdapter;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.bean.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Texel on 2016/8/2.
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.view_pager) ViewPager mPager;

    private String[] mImgUrls = null;
    private int mCurrentPosition = 0;
    private PhotoAdapter mAdapter = null;

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = null;

        intent = getIntent();
        mImgUrls = intent.getStringArrayExtra(Constants.BUNDLE_KEY_IMAGES);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = null;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        mAdapter = new PhotoAdapter(this);
        mPager.setAdapter(mAdapter);

        mAdapter.addItems(mImgUrls);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
