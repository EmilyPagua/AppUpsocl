package com.upsocl.appupsocl.ui.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.Interests;
import com.upsocl.appupsocl.ui.adapters.InterestsAdapter;

import java.util.ArrayList;
import java.util.Map;

public class InterestsFragment extends Fragment {


    private ArrayList<Interests> interestses = new ArrayList<>();
    private SharedPreferences preferences;
    private RecyclerView interestList;
    private InterestsAdapter adapter;

    public InterestsFragment(SharedPreferences prefs) {
        this.preferences = prefs;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_interests, container, false);

        interestList = (RecyclerView) root.findViewById(R.id.recyclerView);
        interestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new InterestsAdapter(getActivity());

        loadInterests();
        loadPreferences();

        interestList.setAdapter(adapter);

        return root;
    }

    private void loadPreferences() {
        adapter.addPreferences(preferences);
    }

    public void loadInterests(){

        Map<String, ?> map = preferences.getAll();
        map.size();
        interestses= new ArrayList<>();
        int i = 0;

        Interests obj;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            Interests interests = new Interests();
            if (e.getKey().equals(Interests.INTERESTS_SIZE)==false){
                obj = new Interests().getInterestByID(Integer.valueOf(e.getKey()));
                interests.setId(obj.getId());
                interests.setTitle(obj.getTitle());
                interests.setIsCheck(Boolean.valueOf(e.getValue().toString()));
                interests.setImagen(obj.getImagen());
                interestses.add(interests);
            }
        }
        adapter.addAll(interestses);
    }

}
