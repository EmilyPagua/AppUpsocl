package com.upsocl.appupsocl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.appupsocl.domain.Category;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.ui.ViewConstants;
import com.upsocl.appupsocl.ui.fragments.BookmarksFragment;

import java.util.ArrayList;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {
    private  Gson gs = new Gson();
    private News obj;
    private ShareActionProvider mShareActionProvider;
    private  ArrayList<Category> newsArrayList;
    float initialX =  Float.NaN;
    private LinearLayout viewDetail;
    private int position;
    private boolean bookmarks_save, flag_bookmarks, isBookmarks, isNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        obj = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        newsArrayList = gs.fromJson(getIntent().getStringExtra("listNews"), ArrayList.class);
        position =  getIntent().getIntExtra("position",0);
        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);
        isNotification =  getIntent().getBooleanExtra("isNotification",false);

        viewDetail = (LinearLayout) findViewById(R.id.viewDetailLinear);
        viewDetail.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                float finalX = event.getX();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        return  true;

                    case (MotionEvent.ACTION_MOVE):

                        if (initialX < finalX)
                            getNewsObjet(position-1);

                        if (initialX > finalX)
                            getNewsObjet(position+1);

                        return true;
                }
                return false;
            }
        });

        setToolBar();
        setImage(obj.getImage());
        setTextViewTitle(obj.getTitle());
        setTextViewDetail(obj.getAuthor(), obj.getDate());
        enableWebView(obj.getContent());
        flag_bookmarks = false;
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

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_segundary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotification){
                    Intent intent = new Intent(DetailActivity.this, NotificationActivity.class);
                    startActivity(intent);
                }else
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

    private void setTextViewTitle(String title){
        TextView textView = (TextView) findViewById(R.id.detailTextView);
        textView.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        MenuItem item_bookmark = menu.findItem(R.id.menu_item_bookmarks);
        if (isBookmarks){
            item_bookmark.setVisible(false);
        }

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        uploadPreferences(String.valueOf(obj.getId()),item_bookmark);

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                createShareIntent();
                return true;

            case R.id.menu_item_bookmarks:

                if (flag_bookmarks)
                    removePreference(item);
                else
                    savePreferences(item) ;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewsObjet(int i) {
        if (newsArrayList!=null){
            if ((i>=0 ) && (i<newsArrayList.size()-1)){
                Map<String, String> map = (Map<String, String>) newsArrayList.get(i);
                News newsObj =  new News();
                newsObj.setTitle(map.get("title"));
                newsObj.setImage(map.get("image"));
                newsObj.setContent(map.get("content"));
                newsObj.setId(map.get("id"));
                newsObj.setAuthor(map.get("author"));
                newsObj.setLink(map.get("link"));

                Gson gS = new Gson();
                String target = gS.toJson(newsObj);
                String listNews = gS.toJson(newsArrayList);
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("new", target);
                intent.putExtra("listNews", listNews);
                intent.putExtra("position",i);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                this.finish();
                startActivity(intent);
            }
        }
    }

    private void uploadPreferences(String id, MenuItem item) {

        SharedPreferences prefs2 =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        String objeto = null;
        objeto = prefs2.getString(id,null);
        bookmarks_save = prefs2.getBoolean("bookmarks_save", false);

        if (objeto!=null){
            item.setIcon(R.drawable.ic_bookmark_white_36dp);
            flag_bookmarks = true;
        }
    }

    private void savePreferences(MenuItem item) {
        Gson gS = new Gson();
        String target = gS.toJson(obj);

        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();

        prefs.getAll().size();
        if (obj.getId()!="0")
        {
            flag_bookmarks = true;
            Toast.makeText(this, "Salvado como preferido", Toast.LENGTH_SHORT).show();

            editor.putString(String.valueOf(obj.getId()), target);
            editor.commit();
            item.setIcon(R.drawable.ic_bookmark_white_36dp);
        }
    }

    private void removePreference(MenuItem item) {
        flag_bookmarks = false;
        Toast.makeText(this, "No esta como preferido", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.remove(String.valueOf(obj.getId())).commit();
        item.setIcon(R.drawable.ic_bookmark_border_white_36dp);
    }


    private void createShareIntent(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, obj.getLink());
        this.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +obj.getTitle()));
    }
}
