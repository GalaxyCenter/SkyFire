package apollo.tianya.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import apollo.tianya.R;
import apollo.tianya.adapter.ViewPageInfo;
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.bean.Notice;
import apollo.tianya.fragment.BookMarksFragment;
import apollo.tianya.fragment.ChannelViewPagerFragment;
import apollo.tianya.fragment.CollectionViewPagerFragment;
import apollo.tianya.fragment.NavigationDrawerFragment;
import apollo.tianya.fragment.NoticeViewPagerFragment;
import apollo.tianya.service.NoticeService;
import apollo.tianya.service.NoticeUtils;
import apollo.tianya.util.TLog;
import apollo.tianya.util.UIHelper;
import apollo.tianya.widget.BadgeView;

public class MainActivity extends BaseActivity {

    enum Tab {
        Tab(0, R.string.main_tab_home, R.drawable.ic_home_white_24dp, ChannelViewPagerFragment.class),
        COLLECTIONS(0, R.string.main_tab_collections, R.drawable.ic_home_white_24dp, BookMarksFragment.class),
        COMMUITIES(0, R.string.main_tab_commuities, R.drawable.ic_home_white_24dp, NoticeViewPagerFragment.class),
        NOTIFICATIONS(0, R.string.main_tab_notifications, R.drawable.ic_home_white_24dp, NoticeViewPagerFragment.class);

        int position;
        int resName;
        int resIcon;
        Class<? extends BaseFragment> refer;

        private Tab(int position, int resName, int resIcon, Class<? extends BaseFragment> refer) {
            this.position = position;
            this.resIcon = resIcon;
            this.resName = resName;
            this.refer = refer;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.INTENT_ACTION_NOTICE)) {
                Notice notice = (Notice) intent.getSerializableExtra(Constants.BUNDLE_KEY_NOTICES);
                int count = notice.comments + notice.replies + notice.follows;

                if (count > 0) {
                    mBadgView.setBadgeCount(count);
                    mBadgView.setVisibility(View.VISIBLE);
                } else {
                    mBadgView.setVisibility(View.GONE);
                }
            } else if (intent.getAction().equals(Constants.INTENT_ACTION_LOGOUT)) {
                TLog.log(TAG, "BroadcastReceiver:INTENT_ACTION_LOGOUT");
            }
        }
    };

    private static final String TAG = "MainActivity";

    public FragmentTabHost mTabHost;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private BadgeView mBadgView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        registerReceiver(mReceiver, filter);

        NoticeUtils.bindToService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NoticeUtils.unbindFromService(this);
        unregisterReceiver(mReceiver);
        mReceiver = null;
        NoticeUtils.tryToShutDown(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        Toolbar toolbar = null;

        handleIntent(getIntent());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);

        initTabs();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null)
            return;
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {

        } else if (intent.getBooleanExtra("NOTICE", false)) {
            notifitcationBarClick(intent);
        }
    }

    private void initTabs() {
        for (int i = 0; i < Tab.values().length; i++) {
            Tab tab = Tab.values()[i];
            TabHost.TabSpec ts = mTabHost.newTabSpec(getString(tab.resName));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable d = getResources().getDrawable(tab.resIcon);

            ts.setIndicator(indicator);
            title.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
            title.setText(getString(tab.resName));

            mTabHost.addTab(ts, tab.refer, null);

            if (tab.equals(Tab.NOTIFICATIONS)) {
                View nv = indicator.findViewById(R.id.tab_mes);
                mBadgView = new BadgeView(this);
                mBadgView.setVisibility(View.GONE);
                mBadgView.setTargetView(nv);
                mBadgView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                mBadgView.setGravity(Gravity.CENTER);
            }
        }
    }

    private void notifitcationBarClick(Intent fromWhich) {
        if (fromWhich != null) {
            boolean fromNoticeBar = fromWhich.getBooleanExtra(Constants.INTENT_KEY_NOTICE, false);
            if (fromNoticeBar) {
                String tag = null;

                tag = getResources().getString(R.string.actionbar_title_notice);
                UIHelper.showSimpleBack(this, new ViewPageInfo(tag, tag, NoticeViewPagerFragment.class, getBundle(tag)));
            }
        }
    }

    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_ARGS, type);
        return bundle;
    }
}
