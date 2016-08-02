package com.upsocl.appupsocl.ui.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.keys.CustomerKeys;

public class PreferencesFragment  extends Fragment {

    private Switch aSwitch;
    private RadioGroup radioGroup;
    private SharedPreferences prefs;
    private LoginButton logoutFacebook;
    private Button logoutGoogle,logoutTwitter;

    public PreferencesFragment(SharedPreferences prefs) {
        this.prefs =  prefs;
    }

    public PreferencesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);

        logoutFacebook = (LoginButton) root.findViewById(R.id.logout_facebook);
        logoutGoogle = (Button) root.findViewById(R.id.logout_google);
        logoutTwitter = (Button) root.findViewById(R.id.logout_twitter);
        String socialNetwork = prefs.getString(CustomerKeys.DATA_USER_SOCIAL_NETWORK, null);


        validateSocialNetwork(socialNetwork);

        aSwitch = (Switch) root.findViewById(R.id.switch_notification);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitch.setChecked(true);
                    //FIXME
                }else{
                    radioGroup.clearCheck();
                    aSwitch.setChecked(false);
                }
            }
        });

        radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rb_week);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_day){
                    aSwitch.setChecked(true);

                }else if (checkedId == R.id.rb_week){
                    aSwitch.setChecked(true);

                }else if (checkedId == R.id.rb_month){
                    aSwitch.setChecked(true);
                }
            }
        });


        return root;

    }

    private void validateSocialNetwork(String socialNetwork) {
        if (socialNetwork.equals(getString(R.string.name_facebook))){
            logoutTwitter.setVisibility(View.GONE);
            logoutGoogle.setVisibility(View.GONE);
            return;
        }
        if (socialNetwork.equals(getString(R.string.name_google))){
            logoutTwitter.setVisibility(View.GONE);
            logoutFacebook.setVisibility(View.GONE);
            return;
        }
        if (socialNetwork.equals(getString(R.string.name_twitter))){
            logoutFacebook.setVisibility(View.GONE);
            logoutGoogle.setVisibility(View.GONE);
            return;
        }
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }
}
