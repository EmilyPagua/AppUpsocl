package com.upsocl.appupsocl.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upsocl.appupsocl.ui.fragments.HomeFoodActivity;
import com.upsocl.appupsocl.ui.fragments.HomeGreenActivity;
import com.upsocl.appupsocl.ui.fragments.HomeNewsActivity;
import com.upsocl.appupsocl.ui.fragments.HomePopularyActivity;
import com.upsocl.appupsocl.ui.fragments.HomeWomenActivity;

/**
 * Created by upsocl on 13-07-16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeNewsActivity homeNewsActivity = new HomeNewsActivity();
                return homeNewsActivity;
            case 1:
                HomeGreenActivity tab2 = new HomeGreenActivity();
                return tab2;
            case 2:
                HomeFoodActivity foodActivity = new HomeFoodActivity();
                return foodActivity;
            case 3:
                HomeWomenActivity womenActivity = new HomeWomenActivity();
                return womenActivity;
            case 4:
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
}
