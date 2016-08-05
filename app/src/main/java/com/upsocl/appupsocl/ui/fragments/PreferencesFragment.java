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

import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.keys.CustomerKeys;

public class PreferencesFragment  extends Fragment {

    private Switch aSwitch;
    private RadioGroup radioGroup;
    private SharedPreferences prefs;
    private Button logoutFacebook;
    private Button logoutGoogle,logoutTwitter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);

        logoutFacebook = (Button) root.findViewById(R.id.logout_facebook);
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

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }
}
