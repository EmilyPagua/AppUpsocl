package com.upsocl.upsoclapp.ui.fragments;

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
import android.widget.TextView;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.keys.CustomerKeys;

public class PreferencesFragment  extends Fragment {

    private Switch aSwitch;
    private RadioGroup radioGroup;
    private SharedPreferences preferences;
    private Button logoutFacebook;
    private Button logoutGoogle,logoutTwitter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);

        logoutFacebook = (Button) root.findViewById(R.id.logout_facebook);
        logoutGoogle = (Button) root.findViewById(R.id.logout_google);
        logoutTwitter = (Button) root.findViewById(R.id.logout_twitter);
        String socialNetwork = preferences.getString(CustomerKeys.DATA_USER_SOCIAL_NETWORK, null);

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

        TextView nameProfile = (TextView) root.findViewById(R.id.nameProfile);
        TextView locationProfile = (TextView) root.findViewById(R.id.locationProfile);
        TextView emailProfile = (TextView) root.findViewById(R.id.emailProfile);

        nameProfile.setText("Usuario: "+preferences.getString(CustomerKeys.DATA_USER_FIRST_NAME," "));
        emailProfile.setText("Email: "+preferences.getString(CustomerKeys.DATA_USER_EMAIL," "));
        String location = preferences.getString(CustomerKeys.DATA_USER_LOCATION,null);
        if (location!=null)
            locationProfile.setText("Ubicación: "+location);

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
        return preferences;
    }

    public void setPrefs(SharedPreferences preferences) {
        this.preferences = preferences;
    }
}
