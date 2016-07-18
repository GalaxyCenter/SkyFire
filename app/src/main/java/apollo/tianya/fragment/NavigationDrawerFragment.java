package apollo.tianya.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageInfo;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.User;
import apollo.tianya.util.UIHelper;
import apollo.tianya.widget.AvatarView;

/**
 * Created by Texel on 2016/7/15.
 */
public class NavigationDrawerFragment extends BaseFragment
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mNameView;
    private AvatarView mAvatarView;

    private User mUser;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_USER_CHANGE)) {
                fillUI();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void initView(View view) {
        View headerView = null;
        NavigationView navigationView = null;
        super.initView(view);

        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        mNameView = (TextView) headerView.findViewById(R.id.username);
        mAvatarView = (AvatarView) headerView.findViewById(R.id.userface);

        mUser = AppContext.getInstance().getLoginUser();
        fillUI();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tag = null;

        if (id == R.id.nav_my_bookmarks) {
            tag = getResources().getString(R.string.actionbar_title_bookmarks);

            UIHelper.showSimpleBack(getActivity(), new ViewPageInfo(tag, tag, BookMarksFragment.class, getBundle(tag)));
        } else if (id == R.id.nav_my_histories) {
            tag = getResources().getString(R.string.actionbar_title_histories);
            UIHelper.showSimpleBack(getActivity(), new ViewPageInfo(tag, tag, BookMarksFragment.class, getBundle(tag)));
        } else if (id == R.id.nav_my_posts) {
            int userId = AppContext.getInstance().getLoginUserId();
            Bundle bundle = getBundle(tag);

            tag = getResources().getString(R.string.actionbar_title_posts);
            bundle.putInt(Constants.BUNDLE_KEY_USER_ID, userId);

            UIHelper.showSimpleBack(getActivity(), new ViewPageInfo(tag, tag, UserPostViewPagerFragment.class, bundle));
        } else if (id == R.id.nav_activities) {
            UIHelper.showLoginActivity(getActivity());
        }

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Bundle getBundle(int val) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_KEY_ARGS, val);
        return bundle;
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        return bundle;
    }

    private void fillUI() {
        if (mUser == null)
            return;

        mAvatarView.setUserInfo(mUser.getId(), mUser.getName());
        mAvatarView.setAvatarUrl("http://tx.tianyaui.com/logo/" + mUser.getId());

        mNameView.setText(mUser.getName());
    }

}
