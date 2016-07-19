package com.upsocl.appupsocl.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upsocl.appupsocl.R;

public class InterestsFragment extends Fragment {

    public InterestsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.activity_interests, container, false);
        return root;
    }

}
