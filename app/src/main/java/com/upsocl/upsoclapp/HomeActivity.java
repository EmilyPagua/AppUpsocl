package com.upsocl.upsoclapp;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.upsocl.upsoclapp.domain.Interests;
import com.upsocl.upsoclapp.keys.CustomerKeys;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.ui.DownloadImage;
import com.upsocl.upsoclapp.ui.adapters.PagerAdapter;

import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener  {

    private SearchView searchView; //
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FrameLayout frameLayout;
    private SharedPreferences prefs, prefsInterests, prefsUser;

    private View headerView;
    private TextView tv_username;
    private ActionBarDrawerToggle toggle;

    private PagerAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    boolean exit = false;
    private Toolbar toolbar;
    private int tabPosition;
    private AppBarLayout appBarLayout;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Content Faceboook
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Fin content facebook

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        tv_username = (TextView) headerView.findViewById(R.id.tv_username);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layoutHome);
        prefs = getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        prefsInterests = getSharedPreferences(Interests.INTERESTS, Context.MODE_PRIVATE);
        prefsUser = getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        appBarLayout =(AppBarLayout) findViewById(R.id.app_bar_layout_home);

        tabs = createTabLayout();
        uploadPager();
        setSupportActionBar(toolbar);
        setDrawer(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        uploadPreferences();
        selectTabsOption();

        //GOOGLE
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addApi(AppIndex.API).build();
        //GOOGLE

        setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
    }


    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    private void uploadPager() {
        adapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        adapter.setPrefs(prefsInterests);
        adapter.setHome(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }

    private void setDrawer(Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @NonNull
    private TabLayout createTabLayout() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.forYou));
        tabs.addTab(tabs.newTab().setText(R.string.lastNews));
        tabs.addTab(tabs.newTab().setText(R.string.green));
        tabs.addTab(tabs.newTab().setText(R.string.creativity));
        tabs.addTab(tabs.newTab().setText(R.string.women));
        tabs.addTab(tabs.newTab().setText(R.string.food));
        tabs.addTab(tabs.newTab().setText(R.string.populary));
        tabs.addTab(tabs.newTab().setText(R.string.quiz));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        return tabs;
    }

    private void selectTabsOption() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();
                switch (tabPosition){
                    case 0:
                        setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
                        break;
                    case 1:
                        setColorBarLayout(R.color.primary_dark_foryou,R.color.primary_foryou);
                        break;
                    case 2:
                        setColorBarLayout(R.color.primary_dark_green,R.color.primary_green);
                        break;
                    case 3:
                        setColorBarLayout(R.color.primary_dark_community,R.color.primary_community);
                        break;
                    case 4:
                        setColorBarLayout(R.color.primary_dark_women,R.color.primary_women);
                        break;
                    case 5:
                        setColorBarLayout(R.color.primary_dark_food,R.color.primary_food);
                        break;
                    case 6:
                        setColorBarLayout(R.color.primary_dark_populary,R.color.primary_populary);
                        break;
                    case 7:
                        setColorBarLayout(R.color.primary_dark_quiz,R.color.primary_quiz);
                        break;
                }
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setColorBarLayout(int statusBarColor, int barLayoutColor) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (android.os.Build.VERSION.SDK_INT >= 21)
            window.setStatusBarColor(this.getResources().getColor(statusBarColor));


            appBarLayout.setBackgroundColor(Color.parseColor(getString(barLayoutColor)));
    }

    private int countPreference() {
        SharedPreferences prefs =  getSharedPreferences(Interests.INTERESTS, Context.MODE_PRIVATE);
        Map<String, ?> map = prefs.getAll();
        map.size();
        int i = 1;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            if (e.getKey().equals(Interests.INTERESTS_SIZE)==false && e.getValue().equals(true)){
               i++;
            }
        }

        return i;
    }

    private void visibleGoneElement() {
        frameLayout.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.INVISIBLE);
        tabs.setVisibility(View.GONE);
        viewPager.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.GONE);
        setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutHome);
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

        changeIconNotification(menu);

        return true;
    }

    private void changeIconNotification(Menu menu) {
        //about icon notification
        MenuItem menuItemNotification =  menu.findItem(R.id.menu_item_notification);
        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        int icon = prefs.getInt(Preferences.NOTI_ICON, R.drawable.ic_notifications_white_24dp);
        menuItemNotification.setIcon(icon);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutHome);

        switch (item.getItemId()){
            case R.id.nav_bookmarks:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_interests:

                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_us:
                drawer.openDrawer(GravityCompat.START);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_notification:
                goNotifications();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goActivityLogin() {

        SharedPreferences prefsUser =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser =  prefsUser.edit();
        editorUser.clear().commit();

        Intent login = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(login);

        this.finish();
    }


    public void btnNotification(View view){

        goNotifications();
    }

    private void goNotifications() {

        if (getNotificationId()!= null){
            visibleGoneElement();
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(HomeActivity.this, R.string.msg_notification_empty, Toast.LENGTH_SHORT).show();
    }

    private String getNotificationId() {

        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putInt(Preferences.NOTI_ICON,R.drawable.ic_notifications_white_24dp).commit();
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(prefs.getInt(Preferences.NOTI_ID,0));
        return  prefs.getString(Preferences.NOTI_DATA,null);
    }


    private void uploadPreferences() {

        SharedPreferences prefs2 =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        String userName = "", userLastName = "", imagenURL=null;

        userName = prefs2.getString(CustomerKeys.DATA_USER_FIRST_NAME," ");
        userLastName = prefs2.getString(CustomerKeys.DATA_USER_LAST_NAME," ");
        imagenURL = prefs2.getString(CustomerKeys.DATA_USER_IMAGEN_URL,null);

        tv_username.setText(userName +" " +userLastName);
        if (imagenURL!=null)
            new DownloadImage((ImageView)headerView.findViewById(R.id.img_profile),getResources()).execute(imagenURL);
    }

    public void logout(View view){
        createSimpleDialog();
        if (exit){
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken((AccessToken) null);
            Profile.setCurrentProfile((Profile)null);

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                        }
                    });
            goActivityLogin();
        }
    }

    //google
    public void  logoutGoogle(View view){
        if (mGoogleApiClient.isConnected()){
            createSimpleDialog();
            if (exit){
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                goActivityLogin();
            }
        }
    }

    //end google

    //twitter
    public void  logoutTwitter(View view){
        createSimpleDialog();
       if (exit){
           goActivityLogin();
       }
    }
    //end twitter

    //Create dialogeMessage
    public void createSimpleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cerrar sesión");
        dialog.setMessage("Esta seguro que desea cerrar sessión?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exit = true;
                        goActivityLogin();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("aun no finaliza");
                    }
                });

        AlertDialog alertDialog =  dialog.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();//uploadPager();
        Log.v("HomeActivity", "onResume");
        setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
    }
}
