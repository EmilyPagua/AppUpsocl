package com.upsocl.appupsocl.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.ui.fragments.HomeFoodActivity;
import com.upsocl.appupsocl.ui.fragments.HomeGreenActivity;
import com.upsocl.appupsocl.ui.fragments.HomeLastNews;
import com.upsocl.appupsocl.ui.fragments.HomeNewsActivity;
import com.upsocl.appupsocl.ui.fragments.HomePopularyActivity;
import com.upsocl.appupsocl.ui.fragments.HomeWomenActivity;

/**
 * Created by upsocl on 13-07-16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    SharedPreferences prefs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeLastNews homeLastNews = new HomeLastNews();
                homeLastNews.setPreferences(prefs);
                return homeLastNews;
            case 1:
                HomeNewsActivity homeNewsActivity = new HomeNewsActivity();
                return homeNewsActivity;
            case 2:
                HomeGreenActivity tab2 = new HomeGreenActivity();
                return tab2;
            case 3:
                HomeFoodActivity foodActivity = new HomeFoodActivity();
                return foodActivity;
            case 4:
                HomeWomenActivity womenActivity = new HomeWomenActivity();
                return womenActivity;
            case 5:
                HomePopularyActivity popularyActivity = new HomePopularyActivity();
                return popularyActivity;
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
}
