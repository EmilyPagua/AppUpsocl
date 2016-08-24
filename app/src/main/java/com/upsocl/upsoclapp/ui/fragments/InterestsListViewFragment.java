package com.upsocl.upsoclapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.Interests;
import com.upsocl.upsoclapp.ui.adapters.CustomArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by upsocl on 22-08-16.
 */
public class InterestsListViewFragment  extends Fragment {

    List<Interests> rows;

    private ListView listView;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates) {
        View root = inflater.inflate(R.layout.listview, container, false);

        listView = (ListView) root.findViewById(R.id.list);
        loadInterests();
        CustomArrayAdapter  customArrayAdapter = new CustomArrayAdapter(getContext(),rows);
        customArrayAdapter.setPreferences(preferences);

        listView.setAdapter(customArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Ha seleccionado un elemento", Toast.LENGTH_LONG).show();
            }
        });

        return root;

    }

    public void loadInterests(){

        Map<String, ?> map = preferences.getAll();
        map.size();
        rows= new ArrayList<>();

        Interests obj;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            Interests interests = new Interests();
            if (e.getKey().equals(Interests.INTERESTS_SIZE)==false){
                obj = new Interests().getInterestByID(Integer.valueOf(e.getKey()));
                interests.setId(obj.getId());
                interests.setTitle(obj.getTitle());
                interests.setIsCheck(Boolean.valueOf(e.getValue().toString()));
                interests.setImagen(obj.getImagen());
                rows.add(interests);
            }
        }
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

}
