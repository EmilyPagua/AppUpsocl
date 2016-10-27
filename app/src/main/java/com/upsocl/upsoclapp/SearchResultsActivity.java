package com.upsocl.upsoclapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.upsocl.upsoclapp.ui.fragments.NewsFragment;

import cl.upsocl.upsoclapp.MenuHomeActivity;

/**
 * Created by emily.pagua on 13-10-16.
 */

public class SearchResultsActivity extends AppCompatActivity {

    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.news_content);
        if (android.os.Build.VERSION.SDK_INT <= 21){
            layout.setPadding(5,5,5,0);
        }else{
            layout.setPadding(16,0,16,16);
        }

        setToolBar();
        handleIntent(getIntent());
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setWord(word);
        setFragment(newsFragment);
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        toolbar.setTitle("Resultado de busqueda");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            word= intent.getStringExtra(SearchManager.QUERY);
        }
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.news_content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(SearchResultsActivity.this, MenuHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        SearchResultsActivity.this.finish();
    }
}
