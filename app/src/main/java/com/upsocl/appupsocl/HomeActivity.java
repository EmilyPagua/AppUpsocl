package com.upsocl.appupsocl;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.upsocl.appupsocl.ui.DownloadImage;
import com.upsocl.appupsocl.ui.adapters.PagerAdapter;
import com.upsocl.appupsocl.ui.fragments.NewsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SearchView searchView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle b = getIntent().getExtras();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        TextView tv_username = (TextView) headerView.findViewById(R.id.tv_username);

        String urlImagen;
        if (b!=null){
            tv_username.setText(b.getString("name"));
            urlImagen = b.getString("imagenURL");
            new DownloadImage((ImageView)headerView.findViewById(R.id.img_profile),getResources()).execute(urlImagen);
        }

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabs = createTabLayout();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
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

        /* setFragment(new NewsFragment(0, null));
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        setFragment(new NewsFragment(tab.getPosition(), null));
                        PublisherAdView mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
                        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
                        mPublisherAdView.loadAd(adRequest);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        // ...
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        // ...
                    }
                }
        );*/

}

    @NonNull
    private TabLayout createTabLayout() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Para ti"));
        tabs.addTab(tabs.newTab().setText("Verde"));
        tabs.addTab(tabs.newTab().setText("Comida"));
        tabs.addTab(tabs.newTab().setText("Mujer"));
        tabs.addTab(tabs.newTab().setText("Mas popular"));
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
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_interests) {
            Intent  intent = new Intent(this,InterestsActivity.class);
            HomeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent  intent = new Intent(this,PreferencesActivity.class);
            HomeActivity.this.startActivity(intent);

        } else if (id == R.id.nav_help) {
            Intent  intent = new Intent(this,HelpActivity.class);
            HomeActivity.this.startActivity(intent);
        }
        else if (id == R.id.nav_close) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.news_content, fragment);
        fragmentTransaction.commit();
    }

    public void logout(){
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken((AccessToken) null);
        Profile.setCurrentProfile((Profile)null);
        Intent login = new Intent(HomeActivity.this, CreatePerfil.class);
        startActivity(login);
        finish();
    }


}
