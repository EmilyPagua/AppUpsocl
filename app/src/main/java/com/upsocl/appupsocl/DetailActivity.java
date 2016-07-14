package com.upsocl.appupsocl;

import android.content.Intent;
import android.gesture.Gesture;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.ui.ViewConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {
    private  Gson gs = new Gson();
    private News obj;
    private ShareActionProvider mShareActionProvider;
    private  ArrayList<News> newsArrayList;
    private String TAG = Gesture.class.getSimpleName();
    float initialX =  Float.NaN;
    private LinearLayout viewDetail;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        obj = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        newsArrayList = gs.fromJson(getIntent().getStringExtra("listNews"), ArrayList.class);
        position =  getIntent().getIntExtra("position",0);

        viewDetail = (LinearLayout) findViewById(R.id.viewDetailLinear);
        viewDetail.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                onTouchEvent(event);
                return true;
            }
        });

        setToolBar();
        setImage(obj.getImage());
        setTextView(obj.getTitle());
        enableWebView(obj.getContent());
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_segundary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setShareIntent(getIntent());
    }

    private void enableWebView(String content){
        WebView webView = (WebView) findViewById(R.id.webView);
        String html = ViewConstants.HTML_HEAD + content;
        html =  html.replace("\\\"","\"").replace("\\n","\n");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", "");
    }

    private void setImage(String objImage){
        ImageView imageview = (ImageView) findViewById(R.id.imageViewDetail);
        String url = objImage;
        Picasso.with(getApplicationContext())
                .load(url)
                .into(imageview);
    }

    private void setTextView(String title){
        TextView textView = (TextView) findViewById(R.id.detailTextView);
        textView.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public void share_url_new(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, obj.getImage());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                share_url_new();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);
        float finalX = event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;

            case (MotionEvent.ACTION_MOVE):

                if (initialX < finalX){
                    getNewsObjet(position-1);

                }


                if (initialX > finalX){
                    getNewsObjet(position+1);
                }


                return true;
        }
        return  super.onTouchEvent(event);
    }

    private void getNewsObjet(int i) {

        if ((i<0 )|| (i>newsArrayList.size()-1)){
            Log.d(TAG, "El registro no se encuentra");
        }else{
            Map<String, String> map = (Map<String, String>) newsArrayList.get(i);
            setImage(map.get("image"));
            setTextView(map.get("title"));
            enableWebView(map.get("content"));
            position = i;
            Log.d(TAG, position +"  Proxima vista: " + map.get("title"));
        }
    }

}
