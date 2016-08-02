package com.upsocl.appupsocl.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.upsocl.appupsocl.NotificationActivity;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.Customer;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.io.ApiConstants;
import com.upsocl.appupsocl.io.WordpressApiAdapter;
import com.upsocl.appupsocl.keys.CustomerKeys;
import com.upsocl.appupsocl.keys.Preferences;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by upsocl on 20-07-16.
 */
public class MyGcmListenerService extends GcmListenerService implements Callback<News> {

    private static final String  TAG = "MyGcmListenerService";
    private String message;
    private String contentTitle;
    private int idMessage;
    private String idPost;

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        message = data.getString("message");
        contentTitle = data.getString("contentTitle");
        idMessage =  Integer.valueOf(data.getString("idMessage"));
        idPost =  data.getString("idPost");

        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
            Log.d(TAG, "message received from some topic" );
        } else {
            // normal downstream message.
            Log.d(TAG, "normal downstream message" );
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        SharedPreferences prefs =  getSharedPreferences(Preferences.DATA_USER, Context.MODE_PRIVATE);
        String token = prefs.getString(CustomerKeys.DATA_USER_TOKEN,null);
        int id_wp =prefs.getInt(CustomerKeys.DATA_USER_ID, 0);
        if (token!=null){
            loadPosts(idPost);
        }
        // [END_EXCLUDE]
    }

    private void sendNotification (String message,String title, int idMessage,String idPost){
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =  PendingIntent.getActivity (this, 0,
                intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(idMessage /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void success(News news, Response response) {
        if (news!=null){

            Gson gS = new Gson();
            String newsJson = gS.toJson(news);

            SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  prefs.edit();
            editor.putString(Preferences.NOTI_ID,idPost).commit();
            editor.putString(Preferences.NOTI_DATA,newsJson).commit();
            sendNotification(message, contentTitle, idMessage, idPost);
        }
    }


    @Override
    public void failure(RetrofitError error) {
        System.out.println(error);
    }

    public void loadPosts(String idPost){
        if (idPost.equals("0")==false)
            WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getPost(idPost, this);

    }

}


