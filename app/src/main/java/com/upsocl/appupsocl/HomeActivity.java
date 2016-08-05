package com.upsocl.appupsocl;

import android.annotation.TargetApi;
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
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.upsocl.appupsocl.domain.Interests;
import com.upsocl.appupsocl.keys.CustomerKeys;
import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.notification.QuickstartPreferences;
import com.upsocl.appupsocl.ui.DownloadImage;
import com.upsocl.appupsocl.ui.adapters.PagerAdapter;
import com.upsocl.appupsocl.ui.fragments.BookmarksFragment;
import com.upsocl.appupsocl.ui.fragments.HelpFragment;
import com.upsocl.appupsocl.ui.fragments.InterestsFragment;
import com.upsocl.appupsocl.ui.fragments.PreferencesFragment;


import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener  {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FrameLayout frameLayout;
    private SharedPreferences prefs, prefsInterests, prefsUser;
    private ArrayList<Interests> categoryArrayList;
    private Gson gs = new Gson();
    private boolean isReceiverRegistered;

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
        selectDrawerOption();
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


        //RegistrationToken Wordpress
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(HomeActivity.this, R.string.gcm_send_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, R.string.token_error_message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        //RegistrationToken Wordpress

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

    private void selectDrawerOption() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                boolean fragmentTransacction = false;
                Fragment fragment = null;
                FragmentManager fragmentManager =  null;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutHome);

                if (countPreference()<=3){
                    Toast.makeText(HomeActivity.this, R.string.msg_select_category, Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    return false;
                }

                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragmentTransacction = false;
                        tabs.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.INVISIBLE);
                        uploadPager();

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
                        SharedPreferences prefs =  getSharedPreferences(Interests.INTERESTS, Context.MODE_PRIVATE);
                        InterestsFragment interestsFragment = new InterestsFragment();
                        interestsFragment.setPreferences(prefs);
                        fragment =  interestsFragment;
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;
                        break;
                    case R.id.nav_manage:
                        visibleGoneElement();
                        PreferencesFragment preferenceActivity = new PreferencesFragment();
                        preferenceActivity.setPrefs(prefsUser);
                        fragment =  preferenceActivity;
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        fragmentTransacction = true;

                        break;
                    case R.id.nav_us:

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setColorBarLayout(int statusBarColor, int barLayoutColor) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

        return true;
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
        return  prefs.getString(Preferences.NOTI_DATA,null);
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
        String userName = "", userLastName = "", imagenURL=null;

        userName = prefs2.getString(CustomerKeys.DATA_USER_FIRST_NAME," ");
        userLastName = prefs2.getString(CustomerKeys.DATA_USER_LAST_NAME," ");
        imagenURL = prefs2.getString(CustomerKeys.DATA_USER_IMAGEN_URL,null);

        tv_username.setText(userName +" " +userLastName);
        categoryArrayList = gs.fromJson(getIntent().getStringExtra("listCategory"), ArrayList.class);
        if (imagenURL!=null)
            new DownloadImage((ImageView)headerView.findViewById(R.id.img_profile),getResources()).execute(imagenURL);
    }

    public void logout(View view){
        createSimpleDialog();
        if (exit){
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken((AccessToken) null);
            Profile.setCurrentProfile((Profile)null);
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
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.upsocl.appupsocl/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
        Log.v("HomeActivity", "Restart");
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       Action viewAction = Action.newAction(  //FIXME
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.upsocl.appupsocl/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
        Log.v("HomeActivity", "Restart");
    }
    //End dialogeMessage



    @Override
    public void onDestroy(){
        super.onDestroy();
        //Nuestro código a ejecutar en este momento
        Log.v("HomeActivity", "onDestroy");
    }

    @Override
    public void onRestart(){
        super.onRestart();

        Log.v("HomeActivity", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("HomeActivity", "onResume");
        setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
        uploadPager();
        registerReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("HomeActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    //TOKENT WORDPRESS

    //END TOKENT WORDPRESS
}
