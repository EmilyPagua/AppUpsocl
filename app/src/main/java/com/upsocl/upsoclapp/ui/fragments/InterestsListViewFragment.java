package com.upsocl.upsoclapp.ui.fragments;

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

        Map<String, ?> map = preferences.getAll();
        map.size();
        rows= new ArrayList<>();
        SharedPreferences.Editor editor =  preferences.edit();
        CategoryList listOptions = new CategoryList();

        Category obj;

        for (Map.Entry<String, ?> e: map.entrySet()) {
            int i = CategoryList.INTERESTS_SIZE_VALUE;

            if (!e.getKey().equals(CategoryList.INTERESTS_SIZE)){

                obj = listOptions.getCategoryById(Integer.valueOf(e.getKey()));
                if (obj !=null && obj.getImage()!=0){
                    obj.setCheck(Boolean.valueOf(e.getValue().toString()));
                }else{
                    obj =  listOptions.categoryList.get(i);
                    editor.remove(String.valueOf(e.getKey())).apply();

                    boolean value = false;
                    if (obj.getId() >306 )
                        value = true;

                    editor.putBoolean(String.valueOf(obj.getId()), value).apply();
                    obj.setCheck(value);
                    i--;
                }
                rows.add(obj);
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
