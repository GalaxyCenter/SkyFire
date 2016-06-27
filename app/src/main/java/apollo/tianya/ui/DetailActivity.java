package apollo.tianya.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import apollo.tianya.R;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.emoji.ToolbarFragment;
import apollo.tianya.fragment.ThreadDetailFragment;

/**
 * 帖子详情Activity
 * Created by Texel on 2016/6/20.
 */
public class DetailActivity extends BaseActivity {

    private ToolbarFragment toolFragment = new ToolbarFragment();

    @Override
    protected void init(Bundle savedInstanceState) {
        BaseFragment fragment = null;
        FragmentTransaction trans = null;

        fragment = new ThreadDetailFragment();
        trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emoji_keyboard, toolFragment).commit();
    }
}
