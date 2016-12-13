package cl.upsocl.upsoclapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;

import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.upsocl.upsoclapp.R;

import android.view.ViewGroup.LayoutParams;

import com.upsocl.upsoclapp.domain.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emily.pagua on 21-10-16.
 */

public class DetailPagerAdapter extends AppCompatActivity {

    // [START shared_tracker]
    private Gson gs = new Gson();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        this.setContentView(R.layout.main);
        List<News> newsList = new ArrayList<>();

        int lenght = getIntent().getIntExtra("lenght",0);
        int position = getIntent().getIntExtra("position",0);

        for(int i=position; i<position+lenght; i++){
            News news = gs.fromJson(getIntent().getStringExtra("pos_"+i), News.class);
            newsList.add(news);
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), newsList);
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
                Log.e("DetailPagerAdapter Fragment", "onPageFinished");
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
    protected void onStop() {
        super.onStop();
        Log.e("DetailPagerAdapter onStop", "onStop");
    }

}
