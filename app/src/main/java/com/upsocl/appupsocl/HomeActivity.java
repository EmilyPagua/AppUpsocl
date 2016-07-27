package com.upsocl.appupsocl;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.upsocl.appupsocl.domain.Category;
import com.upsocl.appupsocl.keys.CustomerKeys;
import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.notification.QuickstartPreferences;
import com.upsocl.appupsocl.ui.DownloadImage;
import com.upsocl.appupsocl.ui.adapters.PagerAdapter;
import com.upsocl.appupsocl.ui.fragments.BookmarksFragment;
import com.upsocl.appupsocl.ui.fragments.HelpFragment;
import com.upsocl.appupsocl.ui.fragments.CategoryFragment;
import com.upsocl.appupsocl.ui.fragments.PreferencesFragment;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FrameLayout frameLayout;
    private SharedPreferences prefs;
    private String location;
    private String email;
    private String birthday;
    private ArrayList<Category> categoryArrayList;
    private Gson gs = new Gson();
    private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    private static final String TAG = "MainActivity";
    private View headerView;
    private TextView tv_username;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle b = getIntent().getExtras();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        tv_username = (TextView) headerView.findViewById(R.id.tv_username);
        frameLayout= (FrameLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        prefs =  getSharedPreferences("bookmarks", Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);
        setDrawer(toolbar);

        tabs = createTabLayout();
        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        uploadPreferences();
        selectDrawerOption();
        selectTabsOption();

    }

    private void setDrawer(Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }


    private void selectTabsOption() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void selectDrawerOption() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                boolean fragmentTransacction = false;
                Fragment fragment = null;
                FragmentManager fragmentManager =  null;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragmentTransacction = false;
                        tabs.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.INVISIBLE);

                        getSupportActionBar().setTitle(item.getTitle());
                        break;

                    case R.id.nav_bookmarks:
                        visibleGoneElement();
                        fragment =  BookmarksFragment.newInstance(prefs);
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;

                        break;
                    case R.id.nav_interests:

                        visibleGoneElement();
                        fragment =  new CategoryFragment(categoryArrayList);
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;
                        break;
                    case R.id.nav_manage:

                        visibleGoneElement();
                        fragment =  new PreferencesFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;

                        break;
                    case R.id.nav_help:

                        visibleGoneElement();
                        fragment =  new HelpFragment();
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;

                        break;

                }
                if (fragmentTransacction){
                    getSupportActionBar().setTitle(item.getTitle());

                }

                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void visibleGoneElement() {
        frameLayout.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.INVISIBLE);
        tabs.setVisibility(View.GONE);
        viewPager.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    @NonNull
    private TabLayout createTabLayout() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.forYou));
        tabs.addTab(tabs.newTab().setText(R.string.green));
        tabs.addTab(tabs.newTab().setText(R.string.food));
        tabs.addTab(tabs.newTab().setText(R.string.women));
        tabs.addTab(tabs.newTab().setText(R.string.populary));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        return tabs;
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
        getMenuInflater().inflate(R.menu.home, menu);

        // Associate searchable configuration with the SearchView
        MenuItem menuItem =  menu.findItem(R.id.menu_item_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItem item_notification = menu.findItem(R.id.menu_item_notification);
        MenuItemCompat.setActionView(item_notification, R.layout.counter_menuitem_layout);

        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        String objeto = prefs.getString(Preferences.NOTI_COUNT,null);

        RelativeLayout notificationCount = (RelativeLayout) MenuItemCompat.getActionView(item_notification);
        TextView notificastionCountText = (TextView) notificationCount.findViewById(R.id.badge_notification_1);
        notificastionCountText.setText(objeto);

        MenuItemCompat.setActionView(item_notification, notificationCount);

        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId()){
            case R.id.nav_bookmarks:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_interests:

                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_help:
                drawer.openDrawer(GravityCompat.START);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void logout(View view){
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken((AccessToken) null);
        Profile.setCurrentProfile((Profile)null);
        Intent login = new Intent(HomeActivity.this, CreatePerfil.class);
        startActivity(login);
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    public void btnNotification(View view){

        goNotifications();
    }

    private void goNotifications() {
        visibleGoneElement();
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("idPost","0");
        startActivity(intent);
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void uploadPreferences() {

        SharedPreferences prefs2 =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        String userName = null, userLastName = null, imagenURL=null;

        userName = prefs2.getString(CustomerKeys.DATA_USER_FIRST_NAME,"Nombre");
        userLastName = prefs2.getString(CustomerKeys.DATA_USER_LAST_NAME,"Apellido");
        imagenURL = prefs2.getString(CustomerKeys.DATA_USER_IMAGEN_URL,null);

        tv_username.setText(userName +" " +userLastName);
        categoryArrayList = gs.fromJson(getIntent().getStringExtra("listCategory"), ArrayList.class);
        new DownloadImage((ImageView)headerView.findViewById(R.id.img_profile),getResources()).execute(imagenURL);
    }


}
