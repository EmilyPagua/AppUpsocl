package com.upsocl.appupsocl.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.upsocl.appupsocl.R;

public class PreferencesFragment  extends Fragment {

    private Switch aSwitch;
    private RadioGroup radioGroup;

    public PreferencesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);


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

}
