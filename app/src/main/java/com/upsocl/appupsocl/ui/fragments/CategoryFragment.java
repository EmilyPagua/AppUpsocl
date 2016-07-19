package com.upsocl.appupsocl.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.Category;
import com.upsocl.appupsocl.ui.adapters.CategorysAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {

    private ListView categoryList;
    private ArrayAdapter<Category> categoryAdapter;
    private List<Category> categorys = new ArrayList<>();
    private ArrayList<Category> categoryArrayList;

    public CategoryFragment(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_interests, container, false);
        categoryList = (ListView) root.findViewById(R.id.category_list);
        loadCategory();

        categoryAdapter = new CategorysAdapter(getActivity(),categorys);
        categoryList.setAdapter(categoryAdapter);
        return root;
    }

    private void loadCategory() {

        for (int i=0;i<categoryArrayList.size();i++){
            Map<String, String> map = (Map<String, String>) categoryArrayList.get(i);
            Category category =  new Category();
            category.setTitle(map.get("title"));
            category.setId(map.get("id"));
            category.setIsCheck(map.get("isCheck"));
            categorys.add(category);
        }
    }
}
