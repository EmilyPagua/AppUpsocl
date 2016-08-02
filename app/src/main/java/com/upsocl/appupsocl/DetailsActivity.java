package com.upsocl.appupsocl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.appupsocl.domain.Interests;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.keys.Preferences;
import com.upsocl.appupsocl.ui.ViewConstants;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private  Gson gs = new Gson();
    private News newsPrimary, newsSegundary, newsThree, newsFour, newsFive;
    private ShareActionProvider mShareActionProvider;

   // private LinearLayout viewDetail;
    private int leght;
    private boolean bookmarks_save, flag_bookmarks, isBookmarks, isHome;


    private ViewFlipper vf;
    private float init_x;
    private String titleSendFacebook;
    private String urlSendFacebook;
    private List<News> newsList =  new ArrayList<>();

    private MenuItem item_bookmark;
    private News newsPosition;

    //Element Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private static FacebookCallback<Sharer.Result> resultFacebookCallback = new FacebookCallback<Sharer.Result>(){
        @Override
        public void onSuccess(Sharer.Result result) {
            System.out.println("onSuccess");
        }

        @Override
        public void onCancel() {
            System.out.println("onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            System.out.println("onError");
        }
    };
    //fin element Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //deslizar a los lados
        vf  =(ViewFlipper) findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        //end deslizar a los lados

        //Content Faceboook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, resultFacebookCallback);
        //Fin content facebook

        newsPrimary = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        newsSegundary = gs.fromJson(getIntent().getStringExtra("newsSegundary"), News.class);
        newsThree = gs.fromJson(getIntent().getStringExtra("newsThree"), News.class);
        newsFour = gs.fromJson(getIntent().getStringExtra("newsFour"), News.class);
        newsFive = gs.fromJson(getIntent().getStringExtra("newsFive"), News.class);;

        leght =  getIntent().getIntExtra("leght",0);

        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);
        isHome =  getIntent().getBooleanExtra("isHome",false);
        int j=1;

        for (int i=0; i<leght; i++){
            switch (i){
                case 0:
                    uploadNews(newsPrimary, R.id.imageViewDetail, R.id.detailTextView, R.id.detail,R.id.webView);
                    newsList.add(newsPrimary);
                    newsPosition=newsPrimary;
                    break;
                case 1:
                    uploadNews(newsSegundary, R.id.imageViewDetailSegundary, R.id.detailTextViewSegundary,
                            R.id.detailSegundary,R.id.webViewSegundary);
                    newsList.add(newsSegundary);
                    break;
                case 2:
                    uploadNews(newsThree, R.id.imageViewDetailThree, R.id.detailTextViewThree,
                            R.id.detailThree,R.id.webViewThree);
                    newsList.add(newsThree);
                    break;
                case 3:
                    uploadNews(newsFour, R.id.imageViewDetailFour, R.id.detailTextViewFour,
                            R.id.detailFour,R.id.webViewFour);
                    newsList.add(newsFour);
                    break;
                case 4:
                    uploadNews(newsFive, R.id.imageViewDetailFive, R.id.detailTextViewFive,
                            R.id.detailFive,R.id.webViewFive);
                    newsList.add(newsFive);
                    break;
            }
        }

        for (int i = 4; i>leght-1;i--){
            vf.removeViewAt(i);
        }
        flag_bookmarks = false;
    }

    private void uploadNews(News objNews, int imagen, int title, int detail, int webView) {
        setToolBar();
        setImage(objNews.getImage(), imagen);
        setTextViewTitle(objNews.getTitle(),title);
        setTextViewDetail(objNews.getAuthor(), objNews.getDate(), objNews.getCategories(), detail);
        enableWebView(objNews.getContent(), webView);
    }


    private void setTextViewDetail(String authorCreate, String dateCreate, String category, int textDetail) {

        TextView detail = (TextView) findViewById(textDetail);

        String author = "Por: "+ authorCreate;
        String  date = "El: "+ dateCreate;
        String categories = "Categorias: " + category;

        SpannableStringBuilder sBauthor = setStyleText(author, 4,author.length());
        SpannableStringBuilder sBdetail = setStyleText(date, 3,date.length());
        SpannableStringBuilder sBcategory = setStyleText(categories, 10,categories.length());

        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail, ". ",sBcategory);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        detail.setText(result, TextView.BufferType.SPANNABLE);
    }

    private SpannableStringBuilder setStyleText(String text, int i, int length) {

        SpannableStringBuilder result = new SpannableStringBuilder(text);
        result.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), i, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return result;

    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_segundary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHomeActivity();
            }
        });
        setShareIntent(getIntent());
    }

    private void goHomeActivity() {
        Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        DetailsActivity.this.finish();
    }


    private void enableWebView(String content, int webViewDetail){
        WebView webView = (WebView) findViewById(webViewDetail);
        String html = ViewConstants.HTML_HEAD + content;
        html =  html.replace("\\\"","\"").replace("\\n","\n");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", "");
    }

    private void setImage(String objImage, int image){
        ImageView imageview = (ImageView) findViewById(image);
        String url = objImage;
        Picasso.with(getApplicationContext())
                .load(url)
                .into(imageview);
    }

    private void setTextViewTitle(String title, int textTitle){
        TextView textView = (TextView) findViewById(textTitle);
        textView.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        item_bookmark = menu.findItem(R.id.menu_item_bookmarks);
        if (isBookmarks){
            item_bookmark.setVisible(false);
        }

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        uploadPreferences(newsPosition.getId());
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

            case R.id.menu_share_facebook:
                facebookShare();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void uploadPreferences(String id) {

        SharedPreferences prefs2 =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        String objeto = null;
        objeto = prefs2.getString(id,null);
        bookmarks_save = prefs2.getBoolean("bookmarks_save", false);
        if (objeto!=null){
            item_bookmark.setIcon(R.drawable.ic_bookmark_white_36dp);
            flag_bookmarks = true;
        }
        else{
            item_bookmark.setIcon(R.drawable.ic_bookmark_border_white_36dp);
        }

    }

    private void savePreferences(MenuItem item) {
        Gson gS = new Gson();
        String target = gS.toJson(newsPosition);

        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();

        prefs.getAll().size();
        if (newsPosition.getId()!="0")
        {
            flag_bookmarks = true;
            Toast.makeText(this, "Salvado como preferido", Toast.LENGTH_SHORT).show();
            editor.putString(String.valueOf(newsPosition.getId()), target);
            editor.commit();
            item.setIcon(R.drawable.ic_bookmark_white_36dp);
        }
    }

    private void removePreference(MenuItem item) {
        flag_bookmarks = false;
        Toast.makeText(this, "No esta como preferido", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.remove(String.valueOf(newsPosition.getId())).commit();
        item.setIcon(R.drawable.ic_bookmark_border_white_36dp);
    }


    private void createShareIntent(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsPosition.getLink());
        this.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +newsPosition.getTitle()));
    }

    public void facebookShare() {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(newsPosition.getTitle())
                    .setContentDescription(getString(R.string.msg_detail_facebook))
                    .setContentUrl(Uri.parse(newsPosition.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        goHomeActivity();
    }



    private class ListenerTouchViewFlipper implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    init_x=event.getX();
                    return true;
                case MotionEvent.ACTION_UP:
                    float distance =init_x-event.getX();

                    if(distance>0)
                    {
                        vf.setInAnimation(inFromRightAnimation());
                        vf.setOutAnimation(outToLeftAnimation());
                        newsPosition = getItemPosition(vf.getDisplayedChild()-1);

                        uploadPreferences(newsPosition.getId());
                        vf.showPrevious();
                    }

                    if(distance<0)
                    {
                        vf.setInAnimation(inFromLeftAnimation());
                        vf.setOutAnimation(outToRightAnimation());

                        newsPosition = getItemPosition(vf.getDisplayedChild()+1);
                        uploadPreferences(newsPosition.getId());

                        vf.showNext();
                    }

                default:
                    break;
            }
            return false;
        }
    }

    private News getItemPosition(int postion) {
        News newsPosition = new News();
        System.out.println(vf.getDisplayedChild());
        switch (postion){
            case 0:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);
                break;
            case 1:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                break;
            case 2:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);
                break;
            case 3:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);
                break;
            default:
                newsPosition =  newsList.get(newsList.size()-1);
                break;
        }
        return newsPosition;
    }


    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f );

        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());

        return inFromRight;

    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }


}
