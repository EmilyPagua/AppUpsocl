package com.upsocl.upsoclapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.ui.ViewConstants;

import cl.upsocl.upsoclapp.MenuHomeActivity;


public class NotificationActivity extends AppCompatActivity  {

    private Gson gs = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setToolBar();
        getNotification();
    }

    private void getNotification() {

        SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
        News news = gs.fromJson(prefs.getString(Preferences.NOTI_DATA,null), News.class);
        if (news!=null){
            setImage(news.getImage());
            setTextViewTitle(news.getTitle());
            setTextViewDetail(news.getAuthor(), news.getDate());
            enableWebView(news.getContent());
        }
    }

    private void setTextViewDetail(String authorCreate, String dateCreate) {

        TextView detail = (TextView) findViewById(R.id.detail);

        String author = "Por: "+ authorCreate;
        String  date = "El: "+ dateCreate;

        SpannableStringBuilder sBauthor = new SpannableStringBuilder(author);
        sBauthor.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), 4, author.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        SpannableStringBuilder sBdetail = new SpannableStringBuilder(date);
        sBdetail.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), 3, date.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        detail.setText(result, TextView.BufferType.SPANNABLE);
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

    private void setTextViewTitle(String title){
        TextView textView = (TextView) findViewById(R.id.detailTextView);
        textView.setText(title);
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_segundary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.app_notifications);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(NotificationActivity.this, MenuHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        NotificationActivity.this.finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(NotificationActivity.this, MenuHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        NotificationActivity.this.finish();
    }

}
