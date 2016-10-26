package cl.upsocl.upsoclapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.upsocl.upsoclapp.R;

import android.view.ViewGroup.LayoutParams;

import com.upsocl.upsoclapp.domain.News;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by upsocl on 21-10-16.
 */

public class DetailPagerAdapter extends AppCompatActivity {

    // [START shared_tracker]
    private Gson gs = new Gson();
    private News newsPrimary, newsSegundary, newsThree, newsFour, newsFive;
    private int leght;
    private Toolbar toolbar;

    private boolean isBookmarks;

    private PageAdapter pageAdapter;

    private List<News> newsList ;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        this.setContentView(R.layout.main);
        newsList = new ArrayList<>();

        newsPrimary = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        newsSegundary = gs.fromJson(getIntent().getStringExtra("newsSegundary"), News.class);
        newsThree = gs.fromJson(getIntent().getStringExtra("newsThree"), News.class);
        newsFour = gs.fromJson(getIntent().getStringExtra("newsFour"), News.class);
        newsFive = gs.fromJson(getIntent().getStringExtra("newsFive"), News.class);
        leght =  getIntent().getIntExtra("leght",0);

        newsList.add(newsPrimary);
        newsList.add(newsSegundary);
        newsList.add(newsThree);
        newsList.add(newsFour);
        newsList.add(newsFive);

        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), newsList);
        viewPager.setAdapter(pageAdapter);

        final ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
    }

    private class PageAdapter extends FragmentPagerAdapter {
        List<News> newsList;
        int positionItem;

        public PageAdapter(FragmentManager fm, List<News> newsList) {
            super(fm);
            this.newsList = newsList;
        }

        @Override
        public Fragment getItem(int position) {
            setPositionItem(position);

            if (newsList.get(position)!=null){
                return DetailPageFragment.newInstance (newsList.get(position));
            }else{
                onBackPressed();
                return null;
            }
        }
        @Override
        public int getCount() {
            return newsList.size();
        }

        public void setPositionItem(int positionItem) {
            this.positionItem = positionItem;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
