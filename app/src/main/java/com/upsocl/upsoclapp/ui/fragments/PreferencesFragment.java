package com.upsocl.upsoclapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.keys.CustomerKeys;
import com.upsocl.upsoclapp.keys.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import cl.upsocl.upsoclapp.PreferencesManager;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

/**
 * Created by emily.pagua on 22-08-16.
 */

public class PreferencesFragment  extends Fragment {

    private static  final String  TAG = "PreferencesFragment";
    private RadioGroup radioGroup;
    private SharedPreferences preferencesUser;
    private SharedPreferences preferencesNoti;
    private Button logoutFacebook;
    private Button logoutGoogle,logoutTwitter;
    private SegmentedButtonGroup group;
    private PreferencesManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);

        logoutFacebook = (Button) root.findViewById(R.id.logout_facebook);
        logoutGoogle = (Button) root.findViewById(R.id.logout_google);
        logoutTwitter = (Button) root.findViewById(R.id.logout_twitter);
        String socialNetwork = preferencesUser.getString(CustomerKeys.DATA_USER_SOCIAL_NETWORK, null);

        validateSocialNetwork(socialNetwork);

        manager = new PreferencesManager(getContext());

        group = (SegmentedButtonGroup) root.findViewById(R.id.segmentedButtonGroup);
        group.setOnClickedButtonPosition(new SegmentedButtonGroup.OnClickedButtonPosition(){

            @Override
            public void onClickedButtonPosition(int position) {
                updateButton(position);
            }
        });

        /*radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup);
        radioGroupCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editorNotifi =  preferencesNoti.edit();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                int frecuency=0;
                Date date = new Date();
                if (checkedId == R.id.rb_day){
                    frecuency =1;

                }else if (checkedId == R.id.rb_week){
                    frecuency=7;

                }else if (checkedId == R.id.rb_month){
                    frecuency=30;
                }
                editorNotifi.putInt(Preferences.NOTIFICATIONS_FRECUENCY,frecuency).apply();
                editorNotifi.putString(Preferences.NOTIFICATIONS_LAST_DATE,formatter.format(date)).apply();
            }
        });*/

        TextView nameProfile = (TextView) root.findViewById(R.id.nameProfile);
        TextView locationProfile = (TextView) root.findViewById(R.id.locationProfile);
        TextView emailProfile = (TextView) root.findViewById(R.id.emailProfile);

        nameProfile.setText(getString(R.string.title_usuario)+" "+preferencesUser.getString(CustomerKeys.DATA_USER_FIRST_NAME," "));
        emailProfile.setText(getString(R.string.title_email)+" "+preferencesUser.getString(CustomerKeys.DATA_USER_EMAIL," "));
        String location = preferencesUser.getString(CustomerKeys.DATA_USER_LOCATION,null);
        if (location!=null)
            locationProfile.setText(getString(R.string.title_localizacion)+" "+location);

        return root;
    }

    private void updateButton(int position) {

        if (position==0)
            manager.SavePreferencesString(Preferences.NOTIFICATIONS,Preferences.NOTIFICATIONS_ACEPT,Preferences.YES);
        else
            manager.SavePreferencesString(Preferences.NOTIFICATIONS,Preferences.NOTIFICATIONS_ACEPT,Preferences.NO);
    }

    /*
        private void radioGroupCheck() {

            switch (preferencesNoti.getInt(Preferences.NOTIFICATIONS_FRECUENCY,1)){
                case 0:
                    radioGroup.check(R.id.rb_day);
                    break;
                case 1:
                    radioGroup.check(R.id.rb_day);
                    break;
                case 7:
                    radioGroup.check(R.id.rb_week);
                    break;
                case 30:
                    radioGroup.check(R.id.rb_month);
                    break;

            }
        }
    */
    private void validateSocialNetwork(String socialNetwork) {
        if (socialNetwork.equals(getString(R.string.name_facebook))){
            logoutFacebook.setVisibility(View.VISIBLE);
            return;
        }
        if (socialNetwork.equals(getString(R.string.name_google))){
            logoutGoogle.setVisibility(View.VISIBLE);
            return;
        }
        if (socialNetwork.equals(getString(R.string.name_twitter))){
            logoutTwitter.setVisibility(View.VISIBLE);
            return;
        }
    }

    public void setPrefsUser(SharedPreferences preferencesUser) {
        this.preferencesUser = preferencesUser;
    }

    public void setPreferencesNoti(SharedPreferences preferencesNoti) {
        this.preferencesNoti = preferencesNoti;
    }
}
