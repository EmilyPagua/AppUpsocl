package com.upsocl.upsoclapp.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.keys.CustomerKeys;

/**
 * Created by upsocl on 09-08-16.
 */
public class PrivacyFragment extends Fragment {

    public PrivacyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);

        TextView privacyDetail1 = (TextView) root.findViewById(R.id.privacyDetail1);
        TextView privacyDetail2 = (TextView) root.findViewById(R.id.privacyDetail2);

        privacyDetail1.setText(getString(R.string.privacity_1)+ " "+getString(R.string.privacity_2) );
        privacyDetail2.setText(getString(R.string.privacity_3) +" "+getString(R.string.privacity_4));

        TextView getInfoDetail = (TextView) root.findViewById(R.id.getInfoDetail);
        TextView accessInfoDetail = (TextView) root.findViewById(R.id.accessInfoDetail);
        TextView useInfoDetail1 = (TextView) root.findViewById(R.id.useInfoDetail1);
        TextView useInfoDetail2 = (TextView) root.findViewById(R.id.useInfoDetail2);
        TextView useInfoDetail3 = (TextView) root.findViewById(R.id.useInfoDetail3);
        TextView useInfoDetail4 = (TextView) root.findViewById(R.id.useInfoDetail4);
        TextView publicInfoDetail = (TextView) root.findViewById(R.id.publicInfoDetail);
        TextView modifyInfoDetail = (TextView) root.findViewById(R.id.modifyInfoDetail);

        getInfoDetail.setText(getString(R.string.privacity_5) );
        useInfoDetail1.setText(getString(R.string.privacity_6));
        useInfoDetail2.setText(getString(R.string.privacity_7));
        useInfoDetail3.setText(getString(R.string.privacity_8));
        useInfoDetail4.setText(getString(R.string.privacity_9));
        accessInfoDetail.setText(getString(R.string.privacity_10));
        publicInfoDetail.setText(getString(R.string.privacity_11));
        modifyInfoDetail.setText(getString(R.string.privacity_12));


        return root;
    }

}
