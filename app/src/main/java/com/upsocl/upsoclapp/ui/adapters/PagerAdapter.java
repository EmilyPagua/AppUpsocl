package com.upsocl.upsoclapp.ui.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upsocl.upsoclapp.domain.Category;
import com.upsocl.upsoclapp.domain.CategoryList;
import com.upsocl.upsoclapp.ui.fragments.HomePrimaryFragment;
import com.upsocl.upsoclapp.ui.fragments.HomeSegundaryFragment;

import java.util.Map;

/**
 * Created by emily.pagua on 13-07-16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private SharedPreferences prefs;
    private Boolean isHome;
    private CategoryList categoryKeys = new CategoryList();


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomePrimaryFragment homeLastNews = new HomePrimaryFragment();
                homeLastNews.setHome(isHome);
                return homeLastNews;
            case 1:
                HomeSegundaryFragment homeNews = new HomeSegundaryFragment();
                String filter= loadInterests();
                homeNews.setCategory(filter);
                homeNews.setHome(isHome);
                return homeNews;
            case 2:
                HomePrimaryFragment homeGreen = new HomePrimaryFragment();
                homeGreen.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_GREEN).getId()));
                homeGreen.setHome(isHome);
                return homeGreen;
            case 3:
                HomeSegundaryFragment homeCreativity = new HomeSegundaryFragment();
                homeCreativity.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_CREATIVITY).getId()));
                homeCreativity.setHome(isHome);
                return homeCreativity;
            case 4:
                HomePrimaryFragment  homeFood = new HomePrimaryFragment();
                homeFood.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_WOMEN).getId()));
                homeFood.setHome(isHome);
                return homeFood;
            case 5:
                HomeSegundaryFragment homeWomen = new HomeSegundaryFragment();
                homeWomen.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_FOOD).getId()));
                homeWomen.setHome(isHome);
                return homeWomen;
            case 6:
                HomePrimaryFragment homePopulary = new HomePrimaryFragment();
                homePopulary.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_POPULARY).getId()));
                homePopulary.setHome(isHome);
                return homePopulary;
            case 7:
                HomeSegundaryFragment segundaryFragment = new HomeSegundaryFragment();
                segundaryFragment.setCategory(String.valueOf(categoryKeys.getIdCategoryByName(CategoryList.OPT_QUIZ).getId()));
                segundaryFragment.setHome(isHome);
                return segundaryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public String loadInterests(){

        Map<String, ?> map = prefs.getAll();
        map.size();
        String filter = "";

        Category obj;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            if (!e.getKey().equals(CategoryList.INTERESTS_SIZE) &&
                    e.getValue().equals(true)){

               // obj = categoryKeys.getIdCategoryById(Integer.valueOf(e.getKey()));
                filter = filter.concat(e.getKey()+",");
            }
        }
        if (filter.equals(""))
            return CategoryList.OPT_COMMUNITY;
        else
            return filter.substring(0, filter.length()-1);
    }

    public void setHome(Boolean home) {
        isHome = home;
    }
}
