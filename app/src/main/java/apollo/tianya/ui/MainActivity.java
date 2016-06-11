package apollo.tianya.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
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
import apollo.tianya.base.BaseActivity;
import apollo.tianya.base.BaseFragment;
import apollo.tianya.fragment.ChannelViewPagerFragment;
import apollo.tianya.util.UIHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    enum Tab {
        Tab(0, R.string.main_tab_home, R.drawable.ic_home_black_24dp, ChannelViewPagerFragment.class),
        COLLECTIONS(0, R.string.main_tab_collections, R.drawable.ic_home_black_24dp, ChannelViewPagerFragment.class),
        COMMUITIES(0, R.string.main_tab_commuities, R.drawable.ic_home_black_24dp, ChannelViewPagerFragment.class),
        NOTIFICATIONS(0, R.string.main_tab_notifications, R.drawable.ic_home_black_24dp, ChannelViewPagerFragment.class);

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


    private static final String TAG = "MainActivity";

    public FragmentTabHost mTabHost;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        BaseFragment fragment = null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            UIHelper.showLoginActivity(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initView() {
        initTabs();
    }

    private void initTabs() {
        for (int i = 0; i < Tab.values().length; i++) {
            Tab tab = Tab.values()[i];
            TabHost.TabSpec ts = mTabHost.newTabSpec(getString(tab.resName));
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_indicator, null);
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) v.findViewById(R.id.tab_title);
            Drawable d = getResources().getDrawable(tab.resIcon);

            ts.setIndicator(indicator);
            title.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
            title.setText(getString(tab.resName));

            mTabHost.addTab(ts, tab.refer, null);
        }
    }

}