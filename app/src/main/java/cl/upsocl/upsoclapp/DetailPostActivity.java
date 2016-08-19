package cl.upsocl.upsoclapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.AnalyticsApplication;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.ui.ViewConstants;

import java.util.ArrayList;
import java.util.List;

import static io.fabric.sdk.android.services.common.CommonUtils.md5;

public class DetailPostActivity extends AppCompatActivity {

    private MenuItem item_bookmark;
    private News newsPosition;
    private List<News> newsList =  new ArrayList<>();

    private Gson gs = new Gson();
    private boolean flag_bookmarks, isBookmarks;

    private ShareActionProvider mShareActionProvider;
    private News newsPrimary, newsSegundary, newsThree, newsFour, newsFive;

    private int leght;

    //PUBLICITY
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    //END PUBLICITY

    private ViewFlipper vf;
    private float init_x;

    // [START shared_tracker]
    private Tracker mTracker;

    //Element Facebook
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

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

    //End Element Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNext);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsPosition = getItemPosition(vf.getDisplayedChild()+1);
                uploadPreferences(newsPosition.getId());
                vf.showNext();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //deslizar a los lados
        vf  =(ViewFlipper) findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        //end deslizar a los lados

        //upload News
        newsPrimary = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        newsSegundary = gs.fromJson(getIntent().getStringExtra("newsSegundary"), News.class);
        newsThree = gs.fromJson(getIntent().getStringExtra("newsThree"), News.class);
        newsFour = gs.fromJson(getIntent().getStringExtra("newsFour"), News.class);
        newsFive = gs.fromJson(getIntent().getStringExtra("newsFive"), News.class);
        leght =  getIntent().getIntExtra("leght",0);

        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);


        uploadNews(newsPrimary, R.id.imageViewDetail, R.id.detailTextView, R.id.detail,R.id.webView, R.id.adView_primary_one,R.id.adView_primary_two);
        newsList.add(newsPrimary);
        newsPosition=newsPrimary;

        createListNews();

        for (int i = 4; i>leght-1;i--){
            vf.removeViewAt(i);
        }
        //End upload News

        //PUBLICITY
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-mb-app-pub-7682123866908966/8579205603");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginPlayingGame();
            }
        });
        requestNewInterstitial();

        // [START shared_tracker]
        AnalyticsApplication application = (AnalyticsApplication) getApplication();

        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        //END PUBLICITY

        //Content Faceboook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, resultFacebookCallback);
        //Fin content facebook

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

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
        if (objeto!=null){
            item_bookmark.setIcon(R.drawable.ic_bookmark_white_36dp);
            flag_bookmarks = true;
        }
        else{
            item_bookmark.setIcon(R.drawable.ic_bookmark_border_white_36dp);
        }
    }

    private void uploadNews(News objNews, int imagen, int title, int detail, int webView, int adViewOne, int adViewTwo) {
        createAdView(adViewOne);
        setImage(objNews.getImage(), imagen);
        setTextViewTitle(objNews.getTitle(),title);
        setTextViewDetail(objNews.getAuthor(), objNews.getDate(), objNews.getCategories(), detail);
        enableWebView(objNews.getContent(), webView);
        createAdView(adViewTwo);
    }

    private void createAdView(int adViewOne) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mAdView = (AdView) findViewById(adViewOne);

        flag_bookmarks = false;

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onStop() {
        clearWebView(R.id.webView);
        clearWebView(R.id.webViewSegundary);
        clearWebView(R.id.webViewThree);
        clearWebView(R.id.webViewFour);
        clearWebView(R.id.webViewFive);

        vf.clearAnimation();
        this.finish();
        super.onStop();
    }

    private void clearWebView(int webViewDetail) {
        WebView webView = (WebView) findViewById(webViewDetail);
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
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

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                break;
            case 1:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                beginPlayingGame();

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
                beginPlayingGame();
                break;
        }

        return newsPosition;
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

    //Animaciones
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

    //PUBLICITY
    private void beginPlayingGame() {
        // [START custom_event]
        mTracker.setScreenName("Publicidad");
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Cerrar Publicidad")
                .setAction("Publicidad")
                .build());
        // [END custom_event]
    }

    private void requestNewInterstitial() {

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(deviceId)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    //END PUBLICITY


    public void createListNews() {
        for (int i=0; i<leght; i++){
            switch (i){
                case 1:
                    uploadNews(newsSegundary, R.id.imageViewDetailSegundary, R.id.detailTextViewSegundary,
                            R.id.detailSegundary,R.id.webViewSegundary, R.id.adView_segundary_one,R.id.adView_segundary_two );
                    newsList.add(newsSegundary);
                    break;
                case 2:
                    uploadNews(newsThree, R.id.imageViewDetailThree, R.id.detailTextViewThree,
                            R.id.detailThree,R.id.webViewThree,R.id.adView_three_one, R.id.adView_three_two);
                    newsList.add(newsThree);
                    break;
                case 3:
                    uploadNews(newsFour, R.id.imageViewDetailFour, R.id.detailTextViewFour,
                            R.id.detailFour,R.id.webViewFour, R.id.adView_four_one, R.id.adView_four_two);
                    newsList.add(newsFour);
                    break;
                case 4:
                    uploadNews(newsFive, R.id.imageViewDetailFive, R.id.detailTextViewFive,
                            R.id.detailFive,R.id.webViewFive, R.id.adView_five_one, R.id.adView_five_two);
                    newsList.add(newsFive);
                    break;
            }
        }
    }

    private void createShareIntent(){

        // [START custom_event]
        mTracker.setScreenName(newsPosition.getLink());
        mTracker.setReferrer(newsPosition.getLink());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("CompartirOtrosMedios")
                .setAction(newsPosition.getLink())
                .build());
        // [END custom_event]

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsPosition.getLink());
        this.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +newsPosition.getTitle()));
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

    public void facebookShare() {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            // [START custom_event]
            mTracker.setScreenName(newsPosition.getLink());
            mTracker.setReferrer(newsPosition.getLink());
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("CompartirBotonFacebook")
                    .setAction(newsPosition.getLink())
                    .build());
            // [END custom_event]


            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(newsPosition.getTitle())
                    .setContentDescription(getString(R.string.msg_detail_facebook))
                    .setContentUrl(Uri.parse(newsPosition.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }
}
