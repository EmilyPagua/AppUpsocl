package com.upsocl.upsoclapp.ui.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upsocl.upsoclapp.domain.Interests;
import com.upsocl.upsoclapp.keys.CategoryKeys;
import com.upsocl.upsoclapp.ui.fragments.HomePrimaryFragment;
import com.upsocl.upsoclapp.ui.fragments.HomeSegundaryFragment;

import java.util.Map;

/**
 * Created by upsocl on 13-07-16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private SharedPreferences prefs;
    private Boolean isHome;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomePrimaryFragment homeLastNews = new HomePrimaryFragment();
                String filter= loadInterests();
                homeLastNews.setCategoryName(filter);
                homeLastNews.setHome(isHome);
                return homeLastNews;
            case 1:
                HomeSegundaryFragment homeNews = new HomeSegundaryFragment();
                homeNews.setHome(isHome);
                return homeNews;
            case 2:
                HomePrimaryFragment homeGreen = new HomePrimaryFragment();
                homeGreen.setCategoryName(CategoryKeys.ID_CATEGORY_GREEN);
                homeGreen.setHome(isHome);
                return homeGreen;
            case 3:
                HomeSegundaryFragment homeCreativity = new HomeSegundaryFragment();
                homeCreativity.setCategoryName(CategoryKeys.ID_CATEGORY_CREATIVITY);
                homeCreativity.setHome(isHome);
                return homeCreativity;
            case 4:
                HomePrimaryFragment  homeFood = new HomePrimaryFragment();
                homeFood.setCategoryName(CategoryKeys.ID_CATEGORY_WOMEN);
                homeFood.setHome(isHome);
                return homeFood;
            case 5:
                HomeSegundaryFragment homeWomen = new HomeSegundaryFragment();
                homeWomen.setCategoryName(CategoryKeys.ID_CATEGORY_FOOD);
                homeWomen.setHome(isHome);
                return homeWomen;
            case 6:
                HomePrimaryFragment homePopulary = new HomePrimaryFragment();
                homePopulary.setCategoryName(CategoryKeys.ID_CATEGORY_POPULARY);
                homePopulary.setHome(isHome);
                return homePopulary;
            case 7:
                HomeSegundaryFragment segundaryFragment = new HomeSegundaryFragment();
                segundaryFragment.setCategoryName(CategoryKeys.ID_CATEGORY_QUIZ);
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

        Interests obj;

        for (Map.Entry<String, ?> e: map.entrySet()) {

            if (e.getKey().equals(Interests.INTERESTS_SIZE) == false &&
                    e.getValue().equals(true)){

                obj = new Interests().getInterestByID(Integer.valueOf(e.getKey()));
                filter = filter.concat(obj.getNameCategory()+",");
            }
        }
        return filter.substring(0, filter.length()-1);
    }

    public void setHome(Boolean home) {
        isHome = home;
    }
}
