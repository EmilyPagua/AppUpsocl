package com.upsocl.appupsocl.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upsocl.appupsocl.GreenActivity;

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
                GreenActivity tab1 = new GreenActivity();
                return tab1;
            case 1:
                GreenActivity tab2 = new GreenActivity();
                return tab2;
            case 2:
                GreenActivity tab3 = new GreenActivity();
                return tab3;
            case 3:
                GreenActivity tab4 = new GreenActivity();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
