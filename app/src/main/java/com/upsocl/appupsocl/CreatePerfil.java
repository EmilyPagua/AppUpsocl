package com.upsocl.appupsocl;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonObject;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.upsocl.appupsocl.domain.Interests;
import com.upsocl.appupsocl.domain.UserLogin;
import com.upsocl.appupsocl.io.ApiConstants;
import com.upsocl.appupsocl.io.WordpressApiAdapter;
import com.upsocl.appupsocl.keys.CategoryKeys;
import com.upsocl.appupsocl.keys.CustomerKeys;
import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.notification.QuickstartPreferences;
import com.upsocl.appupsocl.notification.RegistrationIntentService;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreatePerfil extends AppCompatActivity implements Callback<JsonObject> ,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener  {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YHKSSBjXVukNLxKgV2FbYAjyx";
    private static final String TWITTER_SECRET = "ovzgDNisjtBnyPzU4CI50myqh3BUOFvRUxZHMHEITifPss5eY7";


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "CreatePerfil";


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private Button btn_culture, btn_community,  btn_quiz,        btn_world,  btn_green ,
                   btn_women,   btn_colaboration,       btn_inspiration, btn_health, btn_relations,
                   btn_family,  btn_creativity, btn_beauty,      btn_diversity, btn_movies, btn_styleLive;

    private ArrayList<Interests> listOptions =  new ArrayList<>();

    private String nameSocialNetwork;
    private Integer countCategorySelected;

    private FacebookCallback<LoginResult> callback;

    private ProgressDialog dialog;
    private LinearLayout layoutButton;

    //GOOGLE Login
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN =  100;
    //END GOOGLE LOGIN


    //TWITTER LOGIN
    private TwitterLoginButton loginButtonTwitter;

    //END TWITTER LOGIN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_create_perfil);

        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken((AccessToken) null);
        Profile.setCurrentProfile((Profile) null);

        savePreferencesNotifications();

        countCategorySelected = 0;

        btn_culture = (Button) findViewById(R.id.btn_culture);
        btn_community = (Button) findViewById(R.id.btn_community);
        btn_quiz = (Button) findViewById(R.id.btn_quiz);
        btn_world = (Button) findViewById(R.id.btn_worl);
        btn_green = (Button) findViewById(R.id.btn_green);
        btn_women = (Button) findViewById(R.id.btn_women);
        btn_colaboration = (Button) findViewById(R.id.btn_colaboration);
        btn_inspiration = (Button) findViewById(R.id.btn_inspiration);
        btn_health = (Button) findViewById(R.id.btn_health);
        btn_family = (Button) findViewById(R.id.btn_family);
        btn_creativity = (Button) findViewById(R.id.btn_creativity);
        btn_beauty = (Button) findViewById(R.id.btn_beauty);
        btn_movies = (Button) findViewById(R.id.btn_movies);
        btn_styleLive = (Button) findViewById(R.id.btn_styleLive);
        btn_relations = (Button) findViewById(R.id.btn_relations);
        btn_diversity = (Button) findViewById(R.id.btn_diversity);
        layoutButton =  (LinearLayout) findViewById(R.id.linearLayoutButton);

        uploadData();

        if (isConnect()==true){
            //FACEBOOOK LOGIN
            loginButton = (LoginButton) findViewById(R.id.login_button);
            callbackManager = CallbackManager.Factory.create();

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if (countCategorySelected >= 3) {
                        AccessToken.setCurrentAccessToken(currentAccessToken);
                        loginButton.setVisibility(View.GONE);
                    } //else
                    LoginManager.getInstance().logOut();
                }
            };

            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                }
            };

            accessTokenTracker.startTracking();
            profileTracker.startTracking();

            createCallbackFacebook();

            //END FACEBOOK LOGIN
            loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_location"));
            loginButton.registerCallback(callbackManager, callback);

            //Google
            configureGoogleLogin();

            //TWITTER
            configureTwitterLogin();

            //RegistrationToken Wordpress
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        //Toast.makeText(CreatePerfil.this, R.string.gcm_send_message, Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(CreatePerfil.this, R.string.token_error_message, Toast.LENGTH_SHORT).show();
                    }
                }
            };

            registerReceiver();
            wordpressRegisterReceiver();

        }else{
            createSimpleDialog();

        }
    }

    private void uploadData() {

        listOptions = (ArrayList<Interests>) new Interests().createList();
        SharedPreferences prefs =  getSharedPreferences(Interests.INTERESTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.clear().commit();
        int sizeList =  listOptions.size();
        for (int i =0; i<sizeList; i++){
            editor.putBoolean(String.valueOf(listOptions.get(i).getId()), false).commit();
        }
        editor.putInt(Interests.INTERESTS_SIZE, Interests.INTERESTS_SIZE_VALUE).commit();

        SharedPreferences prefsUser =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser =  prefsUser.edit();
        editorUser.clear().commit();
    }


    private void wordpressRegisterReceiver() {

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

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

    private void createCallbackFacebook() {

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try{
                                    UserLogin userLogin=  new UserLogin(object.getString("email"),
                                            object.getString("name") + " " + object.getString("last_name"),
                                            "",
                                            null,
                                            object.getJSONObject("location").getString("name"),
                                            null, 0,"http://graph.facebook.com/"+object.getString("id")+"/picture?type=large",
                                            getString(R.string.name_facebook));

                                    uploadDialog();

                                    new DownloadTask().execute(userLogin);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name, email,gender,birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                createCallbackFacebook();
                configureTwitterLogin();
                wordpressRegisterReceiver();
            }

            @Override
            public void onError(FacebookException error) {
                createSimpleDialog();
            }
        };
    }

    private void configureGoogleLogin() {
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);
    }


    private void configureTwitterLogin() {

        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtonTwitter.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                uploadDialog();
                TwitterSession session = result.data;
                    // with your app's user model

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new com.twitter.sdk.android.core.Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {

                                    //If it succeeds creating a User object from userResult.data
                                User user = userResult.data;
                                UserLogin userLogin = new UserLogin(userResult.data.email,
                                        user.name,
                                        "",
                                        null,
                                        user.location,
                                        null, 0, user.profileImageUrl,
                                        getString(R.string.name_twitter));

                                    new DownloadTask().execute(userLogin);
                                }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.d("TwitterException", "Login with Twitter failure", exception);

                            }
                        });
                }

                @Override
                public void failure(TwitterException exception) {
                    createSimpleDialog();
                }
            });
    }


    private void uploadDialog() {
        dialog = ProgressDialog.show(CreatePerfil.this, getString(R.string.msg_dialog_title),
                    getString(R.string.msg_dialog_content), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (listOptions.size()>=3){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);

        }
        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);


    }


    private void saveUserWP(final UserLogin userLogin) {

        WordpressApiAdapter.getApiServiceCustomer(ApiConstants.BASE_URL_CUSTOMER)
                .saveCustomer(userLogin.getFirstName(),
                        userLogin.getLastName(),
                        isNull(userLogin.getEmail()),
                        "",
                        isNull(userLogin.getLocation()),
                        userLogin.getSocialNetwork(),
                        userLogin.getToken(), new Callback<JsonObject>() {
                            @Override
                            public void success(JsonObject object, Response response) {

                                if (object.get("success").equals("true")){
                                    int id = object.get("id").getAsInt();
                                    userLogin.setId(id);
                                    saveUser(userLogin);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("Error" , error.getMessage());
                            }
                        });
    }

    private String isNull(String userLogin) {
        if (userLogin==null)
            return "";
        else
        return userLogin;
    }

    private void saveUser(UserLogin userLogin) {
        SharedPreferences prefs =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putString(CustomerKeys.DATA_USER_TOKEN, userLogin.getToken());
        editor.putString(CustomerKeys.DATA_USER_FIRST_NAME, userLogin.getFirstName());
        editor.putString(CustomerKeys.DATA_USER_LAST_NAME, userLogin.getLastName());
        editor.putString(CustomerKeys.DATA_USER_BIRTHDAY, userLogin.getBirthday());
        editor.putString(CustomerKeys.DATA_USER_LOCATION, userLogin.getLocation());
        editor.putString(CustomerKeys.DATA_USER_IMAGEN_URL, userLogin.getImagenURL());
        editor.putString(CustomerKeys.DATA_USER_EMAIL, userLogin.getEmail());
        editor.putInt(CustomerKeys.DATA_USER_ID, userLogin.getId());
        editor.putString(CustomerKeys.DATA_USER_SOCIAL_NETWORK, userLogin.getSocialNetwork());
        editor.commit();
    }

    @Nullable
    private String convertFormat(String birthday) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

        Date date =  null;
        try {

            date = formatter.parse(birthday);
            return formatter2.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    public void success(JsonObject integers, Response response) {
        System.out.println(":  " + integers);
    }

    @Override
    public void failure(RetrofitError error) {
        System.out.println(":  " + error);
    }

    private void savePreferencesNotifications(){
        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.clear().commit();
    }

    private class DownloadTask extends AsyncTask {
        UserLogin userLogin ;

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences prefs =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
            String token = prefs.getString(CustomerKeys.DATA_USER_TOKEN,null);

            userLogin = (UserLogin) objects[0];
            userLogin.setToken(token);
            saveUser(userLogin);
            saveUserWP(userLogin);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Intent intent = new Intent(CreatePerfil.this, HomeActivity.class);
            startActivity(intent);
            CreatePerfil.this.finish();
            super.onPostExecute(o);

        }
    }

    public void btnCuture(View view){
        changeStatusButton(CategoryKeys.OPT_CULTURA,btn_culture);
    }

    public void btnCommunity(View view){

        changeStatusButton(CategoryKeys.OPT_COMMUNITY,btn_community);
    }

    public void btnQuiz(View view){
        changeStatusButton(CategoryKeys.OPT_QUIZ,btn_quiz);
    }

    public void bntWorl(View view){
        changeStatusButton(CategoryKeys.OPT_WORLD,btn_world);
    }

    public void btnWomen(View view){
        changeStatusButton(CategoryKeys.OPT_WOMEN,btn_women);
    }

    public void btnDiversity(View view){
        changeStatusButton(CategoryKeys.OPT_DIVERSITY,btn_diversity);
    }

    public void btnGreen(View view){
        changeStatusButton(CategoryKeys.OPT_GREEN,btn_green);
    }

    public void btnColaboration(View view){

        changeStatusButton(CategoryKeys.OPT_COLABORATION,btn_colaboration);
    }

    public void btnInspiration(View view){

        changeStatusButton(CategoryKeys.OPT_INSPIRATION,btn_inspiration);
    }

    public void btnHealth(View view){
        changeStatusButton(CategoryKeys.OPT_HEALTH, btn_health);
    }

    public void btnRelations(View view){

        changeStatusButton(CategoryKeys.OPT_RELATIONS, btn_relations);
    }

    public void btnFamily(View view){
        changeStatusButton(CategoryKeys.OPT_FAMILY, btn_family);
    }

    public void btnCreativity(View view){
        changeStatusButton(CategoryKeys.OPT_CREATIVITY, btn_creativity);
    }
    public void btnBeauty(View view){
        changeStatusButton(CategoryKeys.OPT_BEAUTY, btn_beauty);
    }

    public void btnMovies(View view){
        changeStatusButton(CategoryKeys.OPT_MOVIES, btn_movies);
    }

    public void btnStyleLive(View view){
        changeStatusButton(CategoryKeys.OPT_STYLELIVE, btn_styleLive);
    }

    private void changeStatusButton(String optCultura, Button button) {

        if (changeListOpt(optCultura))
            button.setBackground(getResources().getDrawable(R.drawable.boton_desahabilitado));
        else
            button.setBackground(getResources().getDrawable(R.drawable.boton_normal));
    }

    private Boolean changeListOpt(String option) {
        boolean flag= true;

        for (int i=0;i< listOptions.size(); i++){
            if (listOptions.get(i).getTitle().equals(option)){
                if (listOptions.get(i).getIsCheck().equals(true)){
                    flag=false;
                    countCategorySelected --;
                }
                else{
                    countCategorySelected ++;
                }
                listOptions.get(i).setIsCheck(flag);
                saveInterests(listOptions.get(i).getId(),flag);
            }
        }

        if (countCategorySelected>2){
            layoutButton.setVisibility(View.VISIBLE);
        }else
            layoutButton.setVisibility(View.INVISIBLE);

        return flag;
    }

    private void saveInterests(int objet, boolean flag){

        SharedPreferences prefs =  getSharedPreferences(Interests.INTERESTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putBoolean(String.valueOf(objet), flag).commit();
    }



    //GOOGLE LOGIN

    @Override
    public void onClick(View v) {

        if (v == signInButton)
            signIn();

    }


    private void signIn() {
        if (isConnect()==true){
            //Creating an intent
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            //Starting intent for result
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else{
            createSimpleDialog();
        }
    }


    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {
        uploadDialog();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        //If the login succeed
        if (result.isSuccess()) {
            uploadDialog();
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            Uri urlImagen;
            String urlIma =null;
            //Displaying name and email

            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            urlImagen = acct.getPhotoUrl();
            if (urlImagen==null)
                urlIma = person.getImage().getUrl();
            else
                urlIma = urlImagen.toString();

            UserLogin userLogin=  new UserLogin(acct.getEmail(),
                    acct.getGivenName() +" "+acct.getFamilyName(),
                    "",
                    person.getBirthday(),
                    person.getCurrentLocation(),
                    null,0,urlIma,
                    getString(R.string.name_google));


            new DownloadTask().execute(userLogin);

            //Loading image
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();

        }
    }

    //END GOOGLE LOGIN


    public void createSimpleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage(getString(R.string.msg_session_not_found))
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        createCallbackFacebook();
                        configureTwitterLogin();
                        wordpressRegisterReceiver();
                        finish();
                    }
                });

        AlertDialog alertDialog =  dialog.create();
        alertDialog.show();
    }

}
