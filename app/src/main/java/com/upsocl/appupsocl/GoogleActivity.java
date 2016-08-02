package com.upsocl.appupsocl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.upsocl.appupsocl.domain.UserLogin;

import io.fabric.sdk.android.Fabric;


public class GoogleActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YHKSSBjXVukNLxKgV2FbYAjyx";
    private static final String TWITTER_SECRET = "ovzgDNisjtBnyPzU4CI50myqh3BUOFvRUxZHMHEITifPss5eY7";


    private TwitterLoginButton loginButtonTwitter;
    private TextView status;


    //TextViews
    private TextView textViewName;
    private TextView textViewEmail;


    //END GOOGLE LOGIN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_google);


       /* loginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        status = (TextView)findViewById(R.id.status);
        status.setText("Status: Ready");

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key),
                getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig));

        loginButton.setCallback(new LonginHandler());*/

        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButtonTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {

                                //If it succeeds creating a User object from userResult.data
                                User user = userResult.data;

                                //Getting the profile image url
                                String profileImage = user.profileImageUrl.replace("_normal", "");

                                UserLogin userLogin=  new UserLogin(userResult.data.email,
                                        user.name,
                                        user.name,
                                        null,
                                        user.location,
                                        null, 0,profileImage,
                                        getString(R.string.name_facebook));

                                System.out.println(user.profileImageUrl);

                                //Creating an Intent
                                /*Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

                                //Adding the values to intent
                                intent.putExtra(KEY_USERNAME,username);
                                intent.putExtra(KEY_PROFILE_IMAGE_URL, profileImage);

                                //Starting intent
                                startActivity(intent);*/

                            }

                            @Override
                            public void failure(TwitterException exception) {

                            }
                        });
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
    }

}
