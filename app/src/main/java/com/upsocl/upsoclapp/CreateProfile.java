package com.upsocl.upsoclapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.upsocl.upsoclapp.domain.UserLogin;
import com.upsocl.upsoclapp.io.ApiConstants;
import com.upsocl.upsoclapp.io.WordpressApiAdapter;
import com.upsocl.upsoclapp.domain.CategoryList;
import com.upsocl.upsoclapp.keys.CustomerKeys;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.notification.QuickstartPreferences;
import com.upsocl.upsoclapp.notification.RegistrationIntentService;

import cl.upsocl.upsoclapp.MenuHomeActivity;
import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by emily.pagua on 13-09-10.
 */

public class CreateProfile extends AppCompatActivity implements Callback<JsonObject> ,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = "CreateProfile";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YHKSSBjXVukNLxKgV2FbYAjyx";
    private static final String TWITTER_SECRET = "ovzgDNisjtBnyPzU4CI50myqh3BUOFvRUxZHMHEITifPss5eY7";

    private CallbackManager callbackManager;

    private Button btn_culture, btn_community,  btn_quiz,        btn_world,  btn_green ,
                   btn_women,   btn_colaboration,       btn_inspiration, btn_health, btn_relations,
                   btn_family,  btn_creativity, btn_beauty,      btn_diversity, btn_movies, btn_styleLive;

   // FIXME private ArrayList<Interests> listOptions =  new ArrayList<>();
    private CategoryList listOptions = new CategoryList();

    private Integer countCategorySelected;

    private ProgressDialog dialog;
    private LinearLayout layoutButton;

    //GOOGLE Login
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN =  9001;//100
    //END GOOGLE LOGIN

    private TwitterLoginButton loginButtonTwitter;    //TWITTER LOGIN

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;

    private UserLogin userLogin;
    private int error=0;


    //format Email
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        @Override
        public void onCancel() {
            createSimpleDialog("Cancel");

        }

        @Override
        public void onError(FacebookException e) {
            createSimpleDialog("Error");

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());

        //LoginManager.getInstance().logOut();

        listOptions.createCategoryList();

        setContentView(R.layout.activity_create_perfil);

        error =getIntent().getIntExtra("error",0);

        savePreferencesNotifications();
        //Package Facebook
        getFecebookKeyHash();
        //end Package Facebook

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

        if (isConnect()){
            //-------------------------FACEBOOOK LOGIN
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            callbackManager = null;
            callbackManager = CallbackManager.Factory.create();

            AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                    if (countCategorySelected >= 3) {
                        AccessToken.setCurrentAccessToken(currentAccessToken);
                        layoutButton.setVisibility(View.INVISIBLE);
                    } else
                        LoginManager.getInstance().logOut();
                }
            };

            ProfileTracker profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                }
            };

            accessTokenTracker.startTracking();
            profileTracker.startTracking();

            createCallbackFacebook();
            loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "user_location"));
            loginButton.registerCallback(callbackManager, callback);

            //------------------END FACEBOOK LOGIN

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
                    /*boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(HomeActivity.this, R.string.gcm_send_message, Toast.LENGTH_SHORT).show();
                } else {
                     Toast.makeText(HomeActivity.this, R.string.token_error_message, Toast.LENGTH_SHORT).show();
                }*/
                }
            };

            registerReceiver();
            wordpressRegisterReceiver();
            //RegistrationToken Wordpress
        }else{
            createSimpleDialog(getString(R.string.msg_session_not_found));
        }
    }

    private void getFecebookKeyHash() {
        //Verificacion de clave facebook
        try {

            String packageName = getApplicationContext().getPackageName();
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);

            Log.d("packageName:", packageName);
            String valor= packageName +"  ";
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                valor = valor + " " + Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:",valor );
            }
           // createSimpleDialog(packageName +" "+valor);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("NameNotFoundException:" , e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.d("NoSuchAlgorithmException:" , e.getMessage());
        }

        //end verififcacion clave facebook
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
    //END RegistrationToken Wordpress
    private void uploadData() {

        //Fixme listOptions = (ArrayList<Interests>) new Interests().createList();
        SharedPreferences prefs =  getSharedPreferences(listOptions.INTERESTS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.clear().apply();
        int sizeList =  listOptions.getCategoryList().size();
        for (int i =0; i<sizeList; i++){
            editor.putBoolean(String.valueOf(listOptions.getCategoryList().get(i).getId()), false).apply();
        }
        editor.putInt(listOptions.INTERESTS_SIZE, listOptions.INTERESTS_SIZE_VALUE).apply();

        SharedPreferences prefsUser =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser =  prefsUser.edit();
        editorUser.clear().apply();
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
                                    userLogin=  new UserLogin(null,
                                            isNull(object.getString("name") ),
                                            "",
                                            null,
                                            null,
                                            null, 0,object.getJSONObject("picture").getJSONObject("data").getString("url"),
                                            getString(R.string.name_facebook));

                                    if (object.isNull("email") ) userLogin.setEmail(null);
                                    else
                                        userLogin.setEmail(object.getString("email"));


                                    if (object.isNull("location") ) userLogin.setLocation("");
                                    else{
                                        if (object.getJSONObject("location").isNull("name") )
                                            userLogin.setLocation("");
                                        else
                                            userLogin.setLocation(object.getJSONObject("location").getString("name"));
                                    }

                                     if (userLogin.getEmail()==null)
                                        showDialog(0);
                                    else
                                        validateInformation(userLogin);

                                } catch (JSONException e) {
                                    Log.e(TAG +" FacebookCallback",  e.getMessage());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name, email,gender,birthday, location, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                createCallbackFacebook();
                configureTwitterLogin();
            }

            @Override
            public void onError(FacebookException error) {
                createSimpleDialog("Problemas al iniciar sesión con Facebook, intente más tarde "+error);
            }
        };
    }

    private void validateInformation(UserLogin userLogin) {
        layoutButton.setVisibility(View.INVISIBLE);
        if (userLogin.getEmail()==null)
            showDialog(0);
        else{
            uploadDialog();
            new DownloadTask().execute(userLogin);
        }

    }

    /**
     * Called to create a dialog to be shown.
     */
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case 0:
                return createExampleDialog();
            default:
                return null;
        }
    }

    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createExampleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Faltan datos para crear su perfil");
        builder.setMessage("Ingrese su email personal:");

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(0);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (validateEmail(value)){
                    userLogin.setEmail(value);
                    uploadDialog();
                    new DownloadTask().execute(userLogin);
                }else{
                    input.setText("");
                    createSimpleDialog("Debe ingresar un email válido");
                }
                return;
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSimpleDialog(getString(R.string.msg_session_not_found));
                return;
            }
        });

        return builder.create();
    }

    //----------GOOGLE CONFIGURE
    private void configureGoogleLogin() {

        GoogleSignInOptions gso;
        if (error==0){
            //Initializing google signin option
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.PROFILE))
                    .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                    .requestEmail()
                    .build();

        }else{
            //Initializing google signin option
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    //.requestScopes(new Scope(Scopes.PROFILE))
                    //.requestScopes(new Scope(Scopes.PLUS_LOGIN))
                    .requestEmail()
                    .build();
        }


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

    @Override
    public void onClick(View v) {
        if (v == signInButton)
            signIn();
    }


    private void signIn() {
        if (isConnect()){
            //Creating an intent
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            //Starting intent for result
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else{
            createSimpleDialog(getString(R.string.msg_session_not_found));
        }
    }

    private void handleSignInResult(GoogleSignInResult result, Person person) {

        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            Uri urlImagen;
            String urlIma =null;
            //Displaying name and email

            urlImagen = acct != null ? acct.getPhotoUrl() : null;

            if (person==null){

                if (urlImagen!=null)
                    urlIma = urlImagen.toString();

                userLogin=  new UserLogin(acct.getEmail(),
                        acct.getDisplayName(),
                        "",
                        null,
                        null,
                        null,0,urlIma,
                        getString(R.string.name_google));

            }else{

                if (urlImagen==null)
                    urlIma = person.getImage().getUrl();
                else
                    urlIma = urlImagen.toString();

                userLogin =  new UserLogin(acct.getEmail(),
                        acct.getDisplayName(),
                        "",
                        isNull(person.getBirthday()),//,
                        isNull(person.getCurrentLocation()),//,
                        null,0,urlIma,
                        getString(R.string.name_google));
            }

            validateInformation(userLogin);

            //Loading image
        } else {
            //If login fails

            if (error ==0 ){
                Toast.makeText(this, "Problemas al iniciar sesión, intente de nuevo", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(CreateProfile.this, CreateProfile.class);
                mainIntent.putExtra("error",1);
                startActivity(mainIntent);
                finish();
            }else{
                if (error==1){
                    Toast.makeText(this, "Problemas al iniciar sesión, intente de nuevo o intente con otra red social", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //END ----------GOOGLE CONFIGURE

    private void configureTwitterLogin() {

        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtonTwitter.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = result.data;
                    // with your app's user model

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new com.twitter.sdk.android.core.Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {
                                //If it succeeds creating a User object from userResult.data
                                User user = userResult.data;
                                userLogin = new UserLogin(userResult.data.email,
                                        user.name,
                                        "",
                                        null,
                                        user.location,
                                        null, 0, user.profileImageUrl,
                                        getString(R.string.name_twitter));

                                validateInformation(userLogin);
                                }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.d("TwitterException", "Login with Twitter failure", exception);

                            }
                        });
                }

                @Override
                public void failure(TwitterException error) {
                    createSimpleDialog(getString(R.string.msg_session_not_found));
                }
            });
    }


    private void uploadDialog() {
        dialog = ProgressDialog.show(CreateProfile.this, getString(R.string.msg_dialog_title),
                    getString(R.string.msg_dialog_content), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (listOptions.getCategoryList().size()>=3){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {

            Person person = null;
            if (mGoogleApiClient.hasConnectedApi(Plus.API))
                person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            //Calling a new function to handle signin
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            //Toast.makeText(this, "estado de google: "+result.getStatus(), Toast.LENGTH_LONG).show();
            handleSignInResult(result, person);
        }
        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
    }


    private void saveUserWP(final UserLogin userLoginFinal) {

        WordpressApiAdapter.getApiServiceCustomer(ApiConstants.BASE_URL_CUSTOMER)
                .saveCustomer(userLoginFinal.getFirstName(),
                        userLoginFinal.getLastName(),
                        isNull(userLoginFinal.getEmail()),
                        "",
                        isNull(userLoginFinal.getLocation()),
                        userLoginFinal.getSocialNetwork(),
                        userLoginFinal.getToken(), new Callback<JsonObject>() {
                            @Override
                            public void success(JsonObject object, Response response) {

                                //noinspection EqualsBetweenInconvertibleTypes
                                if (object.get("success").equals("true")){
                                    int id = object.get("id").getAsInt();
                                    userLoginFinal.setId(id);
                                    saveUser(userLoginFinal);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("Error WordpressApiAdapter" , error.getMessage());
                            }
                        });
    }

    private String isNull(String objectUser) {
        if (objectUser==null)
            return "";
        else
        return objectUser;
    }

    private void saveUser(UserLogin userLoginFinal) {
        SharedPreferences prefs =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putString(CustomerKeys.DATA_USER_TOKEN, userLoginFinal.getToken());
        editor.putString(CustomerKeys.DATA_USER_FIRST_NAME, userLoginFinal.getFirstName());
        editor.putString(CustomerKeys.DATA_USER_LAST_NAME, userLoginFinal.getLastName());
        editor.putString(CustomerKeys.DATA_USER_BIRTHDAY, userLoginFinal.getBirthday());
        editor.putString(CustomerKeys.DATA_USER_LOCATION, userLoginFinal.getLocation());
        editor.putString(CustomerKeys.DATA_USER_IMAGEN_URL, userLoginFinal.getImagenURL());
        editor.putString(CustomerKeys.DATA_USER_EMAIL, userLoginFinal.getEmail());
        editor.putInt(CustomerKeys.DATA_USER_ID, userLoginFinal.getId());
        editor.putString(CustomerKeys.DATA_USER_SOCIAL_NETWORK, userLoginFinal.getSocialNetwork());
        editor.apply();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        SharedPreferences prefsNoti =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNoti =  prefsNoti.edit();
        editorNoti.putInt(Preferences.NOTIFICATIONS_FRECUENCY,1).apply();
        editorNoti.putString(Preferences.NOTIFICATIONS_LAST_DATE,formatter.format(date)).commit();

    }


    @Override
    public void success(JsonObject integers, Response response) {
        //System.out.println(":  " + integers);
    }

    @Override
    public void failure(RetrofitError error) {
        //System.out.println(" Error" + error);
    }

    private void savePreferencesNotifications(){
        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.clear().apply();
    }

    private class DownloadTask extends AsyncTask {
        UserLogin userLoginfinal ;

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences prefs =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
            String token = prefs.getString(CustomerKeys.DATA_USER_TOKEN,null);

            userLoginfinal = (UserLogin) objects[0];
            userLoginfinal.setToken(token);
            saveUser(userLoginfinal);
            saveUserWP(userLoginfinal);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent intent = new Intent(CreateProfile.this, MenuHomeActivity.class);
            startActivity(intent);
            CreateProfile.this.finish();
        }
    }

    public void btnCuture(View view){
        changeStatusButton(CategoryList.OPT_CULTURA,btn_culture);
    }

    public void btnCommunity(View view){

        changeStatusButton(CategoryList.OPT_COMMUNITY,btn_community);
    }

    public void btnQuiz(View view){
        changeStatusButton(CategoryList.OPT_QUIZ,btn_quiz);
    }

    public void bntWorl(View view){
        changeStatusButton(CategoryList.OPT_WORLD,btn_world);
    }

    public void btnWomen(View view){
        changeStatusButton(CategoryList.OPT_WOMEN,btn_women);
    }

    public void btnDiversity(View view){
        changeStatusButton(CategoryList.OPT_DIVERSITY,btn_diversity);
    }

    public void btnGreen(View view){
        changeStatusButton(CategoryList.OPT_GREEN,btn_green);
    }

    public void btnColaboration(View view){

        changeStatusButton(CategoryList.OPT_COLABORATION,btn_colaboration);
    }

    public void btnInspiration(View view){

        changeStatusButton(CategoryList.OPT_INSPIRATION,btn_inspiration);
    }

    public void btnHealth(View view){
        changeStatusButton(CategoryList.OPT_HEALTH, btn_health);
    }

    public void btnRelations(View view){

        changeStatusButton(CategoryList.OPT_RELATIONS, btn_relations);
    }

    public void btnFamily(View view){
        changeStatusButton(CategoryList.OPT_FAMILY, btn_family);
    }

    public void btnCreativity(View view){
        changeStatusButton(CategoryList.OPT_CREATIVITY, btn_creativity);
    }
    public void btnBeauty(View view){
        changeStatusButton(CategoryList.OPT_BEAUTY, btn_beauty);
    }

    public void btnMovies(View view){
        changeStatusButton(CategoryList.OPT_MOVIES, btn_movies);
    }

    public void btnStyleLive(View view){
        changeStatusButton(CategoryList.OPT_STYLELIVE, btn_styleLive);
    }

    private void changeStatusButton(String optCultura, Button button) {

        try{
            if (changeListOpt(optCultura)){
                button.setBackground(getResources().getDrawable(R.drawable.boton_desahabilitado));
                button.setTextColor(Color.WHITE);
            } else{
                button.setBackground(getResources().getDrawable(R.drawable.boton_normal));
                button.setTextColor(Color.BLACK);
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }

    private Boolean changeListOpt(String option) {
        boolean flag= true;

        for (int i=0;i< listOptions.getCategoryList().size(); i++){
            if (listOptions.getCategoryList().get(i).getName().equals(option)){
                if (listOptions.getCategoryList().get(i).getCheck().equals(true)){
                    flag=false;
                    countCategorySelected --;
                }
                else{
                    countCategorySelected ++;
                }
                listOptions.getCategoryList().get(i).setCheck(flag);
                saveInterests(listOptions.getCategoryList().get(i).getId(),flag);
            }
        }

        if (countCategorySelected>2){
            layoutButton.setVisibility(View.VISIBLE);
        }else
            layoutButton.setVisibility(View.INVISIBLE);

        return flag;
    }

    private void saveInterests(int objet, boolean flag){

        SharedPreferences prefs =  getSharedPreferences(listOptions.INTERESTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putBoolean(String.valueOf(objet), flag).apply();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult error) {
        uploadDialog();
    }

    public void createSimpleDialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        createCallbackFacebook();
                        configureTwitterLogin();
                        Intent mainIntent = new Intent(CreateProfile.this, CreateProfile.class);
                        CreateProfile.this.startActivity(mainIntent);
                        CreateProfile.this.finish();
                    }
                });

        AlertDialog alertDialog =  dialog.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    //Validate format email
    public static boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
