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
import com.upsocl.upsoclapp.domain.Category;
import com.upsocl.upsoclapp.domain.CategoryList;
import com.upsocl.upsoclapp.ui.adapters.CustomArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily.pagua on 22-08-16.
 */
public class InterestsListViewFragment  extends Fragment {

    private List<Category> rows;
    private CategoryList listOptions = new CategoryList();
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates) {
        View root = inflater.inflate(R.layout.listview, container, false);

        ListView listView = (ListView) root.findViewById(R.id.list);
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

        CategoryList listOptions = new CategoryList();
        Map<String, ?> map = preferences.getAll();
        map.size();
        rows= new ArrayList<>();

        Category obj;
        int i = CategoryList.INTERESTS_SIZE_VALUE-1;
        for (Map.Entry<String, ?> e: map.entrySet()) {


            if (!e.getKey().equals(CategoryList.INTERESTS_SIZE)){
                obj = listOptions.getCategoryById(Integer.valueOf(e.getKey()));
                if (obj !=null && obj.getImage()!=0){
                    rows.add(obj);
                }else{
                    SharedPreferences.Editor editor =  preferences.edit();
                    editor.remove(String.valueOf(e.getKey())).apply();

                    obj =  listOptions.getCategoryList().get(i);
                    if (obj.getId() >300 )
                        editor.putBoolean(String.valueOf(obj.getId()), true).apply();
                    else
                        editor.putBoolean(String.valueOf(obj.getId()), true).apply();

                    rows.add(obj);
                    i--;
                }
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
