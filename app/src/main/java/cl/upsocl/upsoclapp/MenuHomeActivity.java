package cl.upsocl.upsoclapp;

import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
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
import com.google.gson.Gson;
import com.upsocl.upsoclapp.MainActivity;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.domain.CategoryList;
import com.upsocl.upsoclapp.keys.CustomerKeys;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.notification.QuickstartPreferences;
import com.upsocl.upsoclapp.notification.RegistrationIntentService;
import com.upsocl.upsoclapp.ui.DownloadImage;
import com.upsocl.upsoclapp.ui.adapters.PagerAdapter;
import com.upsocl.upsoclapp.ui.fragments.BookmarksFragment;
import com.upsocl.upsoclapp.ui.fragments.HelpFragment;
import com.upsocl.upsoclapp.ui.fragments.InterestsListViewFragment;
import com.upsocl.upsoclapp.ui.fragments.PreferencesFragment;
import com.upsocl.upsoclapp.ui.fragments.PrivacyFragment;

import java.util.Map;

/**
 * Created by emily.pagua on 21-90-16.
 */

public class MenuHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private ActionBarDrawerToggle toggle;
    private PreferencesManager manager;

    boolean exit = false;

    //PreferencesUser
    private SharedPreferences prefs, prefsInterests, prefsUser;

    //VAR Menu Tabs
    private TabLayout tabs;
    private ViewPager viewPager;
    private int tabPosition;
    private AppBarLayout appBarLayout;

    //VAr Google
    private GoogleApiClient mGoogleApiClient;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;

    private int lastSelected;
    private int tabPositionPager;

    private Menu menuhome;

    //private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout =(AppBarLayout) findViewById(R.id.app_bar_layout_home);
        progressBar =  (ProgressBar) findViewById(R.id.spinner);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame_menu_home);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeHome);
        if (android.os.Build.VERSION.SDK_INT <= 21){
            layout.setPadding(5,5,5,0);
        }else{
            layout.setPadding(16,0,16,16);
        }

        manager = new PreferencesManager(getApplicationContext());
        String objeto = manager.getPreferenceString(Preferences.MESSAGE_UPSOCL, Preferences.MESSAGE_UPSOCL);
        int views = manager.getPreferenceInt(Preferences.MESSAGE_UPSOCL, Preferences.MESSAGE_VIEWS);

        /*if (views!=0){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.content_message,layout);
            TextView message =  (TextView) convertView.findViewById(R.id.messageTextV) ;
            message.setText(objeto);
            convertView.setVisibility(View.VISIBLE);

            manager.SavePreferencesInt(Preferences.MESSAGE_UPSOCL,Preferences.MESSAGE_VIEWS,views-1);
        }*/

        //Get tokentID
        String token = manager.getPreferenceString(Preferences.DATA_USER,CustomerKeys.DATA_USER_TOKEN);

        uploadView();

        //INIT COD CONTENT
        uploadPreferencesUser();
        tabs = createTabLayout();

        selectTabsOption();
        uploadPager();

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        //

        lastSelected = R.id.nav_home;

        //RegistrationToken Wordpress
        if (token == null || token.isEmpty()){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            }
        };

        registerReceiver();
        wordpressRegisterReceiver();
        }else Log.i("HomeActivity", token);
        //RegistrationToken Wordpress
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
        menuhome =  menu;
        getMenuInflater().inflate(R.menu.home, menu);

        // Associate searchable configuration with the SearchView
        MenuItem menuItem =  menu.findItem(R.id.menu_item_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.menu_item_notification:
                goNotifications();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        String title = "";
        FragmentManager fragmentManager =  null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (countPreference()<=3){
            Toast.makeText(MenuHomeActivity.this, R.string.msg_select_category, Toast.LENGTH_SHORT).show();
            drawer.closeDrawers();
            return false;
        }

        switch (item.getItemId()){
            case R.id.nav_home:

                title = item.getTitle().toString();
                if (lastSelected == R.id.nav_interests)
                    uploadPager();
                else
                    setColorBarWindows(tabPositionPager);

                tabs.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);

                //noinspection ConstantConditions
                getSupportActionBar().setTitle(item.getTitle());
                lastSelected =R.id.nav_home;
                break;

            case R.id.nav_bookmarks:
                title = item.getTitle().toString();
                lastSelected =R.id.nav_bookmarks;
                visibleGoneElement();
                fragment =  BookmarksFragment.newInstance(prefs);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame_menu_home, fragment)
                        .commit();
                break;
            case R.id.nav_interests:
                title = item.getTitle().toString();
                lastSelected =R.id.nav_interests;

                Toast.makeText(MenuHomeActivity.this, R.string.msg_selected_category_preferences, Toast.LENGTH_SHORT).show();
                visibleGoneElement();

                SharedPreferences prefs =  getSharedPreferences(CategoryList.INTERESTS, Context.MODE_PRIVATE);
                InterestsListViewFragment viewFragment = new InterestsListViewFragment();
                viewFragment.setPreferences(prefs);
                fragment =  viewFragment;
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame_menu_home, fragment)
                        .commit();

                uploadPager();


                break;
            case R.id.nav_manage:
                title = item.getTitle().toString();
                lastSelected =R.id.nav_manage;
                visibleGoneElement();
                SharedPreferences prefsNoti =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);

                PreferencesFragment preferenceActivity = new PreferencesFragment();
                preferenceActivity.setPrefsUser(prefsUser);
                preferenceActivity.setPreferencesNoti(prefsNoti);

                fragment =  preferenceActivity;
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame_menu_home, fragment)
                        .commit();
                break;
            case R.id.nav_us:
                title = item.getTitle().toString();
                lastSelected =R.id.nav_us;
                visibleGoneElement();
                fragment =  new HelpFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame_menu_home, fragment)
                        .commit();
                break;

            case R.id.nav_profile:
                title = item.getTitle().toString();
                lastSelected =R.id.nav_profile;
               visibleGoneElement();
                fragment =  new PrivacyFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame_menu_home, fragment)
                        .commit();

                break;

            case R.id.calification:
                title = "Home";
                sendCalification();
                break;
        }
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendCalification() {
        getSupportActionBar().setTitle("Home");
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Alerta");
        builder.setMessage("Tu comentario es muy importante para nosotros. " +
                "Por favor, califícanos y comenta para poder mejorar esta aplicación. " +
                "Gracias por tu ayuda..!");
        builder.setCancelable(true);
        builder.setPositiveButton("Valorar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = getPackageName();
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

                }catch (ActivityNotFoundException e){
                    Log.e("MenuHomeActivity", e.getMessage());
                }
            }
        });
        builder.show();

    }

    private void goNotifications() {

        if (getNotificationId()!= null){
            Gson gs = new Gson();

            SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
            News news = gs.fromJson(prefs.getString(Preferences.NOTI_DATA,null), News.class);

            Intent intent = new Intent(this, NotificationActivity.class);
            Gson gS = new Gson();
            String target = gS.toJson(news);
            intent.putExtra("new", target);
            intent.putExtra("position","0");
            intent.putExtra("isBookmarks",false);
            intent.putExtra("leght",1);
            startActivity(intent);
        }
        else
            Toast.makeText(MenuHomeActivity.this, R.string.msg_notification_empty, Toast.LENGTH_SHORT).show();
    }

    private String getNotificationId() {

        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putInt(Preferences.NOTI_ICON,R.drawable.ic_notifications_white_24dp).apply();
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(prefs.getInt(Preferences.NOTI_ID,0));
        return  prefs.getString(Preferences.NOTI_DATA,null);
    }

    private void visibleGoneElement() {
        frameLayout.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        setColorBarLayout(R.color.primary_dark_foryou,R.color.primary_foryou);
    }

    private void uploadPreferencesUser() {
        prefs = getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        prefsInterests = getSharedPreferences(CategoryList.INTERESTS, Context.MODE_PRIVATE);
        prefsUser = getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
    }

    private int countPreference() {
        SharedPreferences prefs =  getSharedPreferences(CategoryList.INTERESTS, Context.MODE_PRIVATE);
        Map<String, ?> map = prefs.getAll();
        map.size();
        int i = 1;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            if (!e.getKey().equals(CategoryList.INTERESTS_SIZE) && e.getValue().equals(true)){
                i++;
            }
        }
        return i;
    }

    private TabLayout createTabLayout() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.lastNews));
        tabs.addTab(tabs.newTab().setText(R.string.forYou));
        tabs.addTab(tabs.newTab().setText(R.string.green));
        tabs.addTab(tabs.newTab().setText(R.string.creativity));
        tabs.addTab(tabs.newTab().setText(R.string.women));
        tabs.addTab(tabs.newTab().setText(R.string.food));
        tabs.addTab(tabs.newTab().setText(R.string.populary));
        tabs.addTab(tabs.newTab().setText(R.string.quiz));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        return tabs;
    }

    private void uploadPager() {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        adapter.setPrefs(prefsInterests);
        adapter.setHome(true);
        viewPager = (ViewPager) findViewById(R.id.pager_home_menu);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }

    private void selectTabsOption() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabPosition = tab.getPosition();
                tabPositionPager = tabPosition;
                setColorBarWindows(tabPosition);
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

    private void setColorBarWindows(int tabPosition) {
        switch (tabPosition){
            case 0:
                setColorBarLayout(R.color.primary_dark_foryou,R.color.primary_foryou);
                break;
            case 1:
                setColorBarLayout(R.color.color_primary_dark_home,R.color.color_primary_home);
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
    }

    private void setColorBarLayout(int statusBarColor, int barLayoutColor) {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(statusBarColor));
            appBarLayout.setBackgroundColor(Color.parseColor(getString(barLayoutColor)));
        }else{
            appBarLayout.setBackgroundColor(Color.parseColor(getString(barLayoutColor)));
            progressBar.setBackgroundColor(barLayoutColor);
            progressBar.setDrawingCacheBackgroundColor(Color.parseColor(getString(barLayoutColor)));
        }


    }

    //Metodo de GoogleMessage
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void uploadView() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_menu_home);
        navigationView.setNavigationItemSelectedListener(this);

        //
        SharedPreferences prefs2 =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        String userName = "", userLastName = "", imagenURL=null;

        userName = prefs2.getString(CustomerKeys.DATA_USER_FIRST_NAME," ");
        userLastName = prefs2.getString(CustomerKeys.DATA_USER_LAST_NAME," ");
        imagenURL = prefs2.getString(CustomerKeys.DATA_USER_IMAGEN_URL,null);

        if (navigationView.getHeaderView(0)!=null) {
            View view = navigationView.getHeaderView(0);

            TextView tv_username = (TextView) view.findViewById(R.id.tv_username_header);

            tv_username.setText(userName + " " + userLastName);
            if (isConnect() && imagenURL != null)
                new DownloadImage((ImageView) view.findViewById(R.id.img_profile_header), getResources()).execute(imagenURL);

            navigationView.setVisibility(View.VISIBLE);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
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
                        public void onResult(@NonNull Status status) {
                            // ...
                            Log.e("GoogleSignInApi", status.getStatusMessage());                        }
                    });
            goActivityLogin();
        }
    }

    //Create dialogeMessage
    public void createSimpleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cerrar sesión");
        dialog.setMessage("Esta seguro que desea cerrar sesión?")
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
                        //System.out.println("aun no finaliza");
                    }
                });

        AlertDialog alertDialog =  dialog.create();
        alertDialog.show();
    }

    private void goActivityLogin() {

        SharedPreferences prefsUser =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser =  prefsUser.edit();
        editorUser.clear().apply();

        Intent login = new Intent(MenuHomeActivity.this, MainActivity.class);
        startActivity(login);

        this.finish();
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

    private boolean isConnect() {

        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    //RegistrationToken Wordpress

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void wordpressRegisterReceiver() {


        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Home Activity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        if (menuhome!= null)
            changeIconNotification(menuhome);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        if (menuhome!= null)
            changeIconNotification(menuhome);
    }
    //END RegistrationToken Wordpress
}
