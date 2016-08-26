package cl.upsocl.upsoclapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.server.response.FieldMappingDictionary;
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
    // FIXME private InterstitialAd mInterstitialAd;

    //END PUBLICITY

    private ViewFlipper vf;
    private float init_x;

    // [START shared_tracker]
    private Tracker mTracker;

    //Element Facebook
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private FloatingActionButton fab;
    private static FacebookCallback<Sharer.Result> resultFacebookCallback = new FacebookCallback<Sharer.Result>(){
        @Override
        public void onSuccess(Sharer.Result result) {
            //System.out.println("onSuccess");
        }

        @Override
        public void onCancel() {
            //System.out.println("onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            //System.out.println("onError");
        }
    };

    //End Element Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsPrimary = gs.fromJson(getIntent().getStringExtra("new"), News.class);
        uploadNews(newsPrimary, R.id.image1, R.id.title1, R.id.detail1,R.id.webView1, R.id.progress1);
        createAdView(R.id.adView1, R.id.webView1,R.id.progress1);
        newsList.add(newsPrimary);
        newsPosition=newsPrimary;


        fab = (FloatingActionButton) findViewById(R.id.fabNext);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsPosition = getItemPosition(vf.getDisplayedChild()+1);
                uploadPreferences(newsPosition.getId());
                vf.showNext();
            }
        });


        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.viewPrimary);
        RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.viewSegundary);
        RelativeLayout layout3 = (RelativeLayout) findViewById(R.id.viewThree);
        RelativeLayout layout4 = (RelativeLayout) findViewById(R.id.viewFour);
        RelativeLayout layout5 = (RelativeLayout) findViewById(R.id.viewFive);

        //deslizar a los lados
        vf  =(ViewFlipper) findViewById(R.id.viewFlipper);

        //end deslizar a los lados

        layout1.setPadding(0,0,0,10);
        layout2.setPadding(0,0,0,10);
        layout3.setPadding(0,0,0,10);
        layout4.setPadding(0,0,0,10);
        layout5.setPadding(0,0,0,10);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //upload News

        newsSegundary = gs.fromJson(getIntent().getStringExtra("newsSegundary"), News.class);
        newsThree = gs.fromJson(getIntent().getStringExtra("newsThree"), News.class);
        newsFour = gs.fromJson(getIntent().getStringExtra("newsFour"), News.class);
        newsFive = gs.fromJson(getIntent().getStringExtra("newsFive"), News.class);
        leght =  getIntent().getIntExtra("leght",0);

        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);

        createListNews();

        for (int i = 4; i>leght-1;i--){
            vf.removeViewAt(i);
        }
        //End upload News

        //PUBLICITY
       /* FIXME mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-mb-app-pub-7682123866908966/8579205603");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginPlayingGame();
            }
        });
        requestNewInterstitial(); */

        // [START shared_tracker]
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        sendReportGoogleAnalytics(newsPosition.getLink(), "ClickPost");
        //END PUBLICITY


        //Content Faceboook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, resultFacebookCallback);
        //End content facebook

      /* FIXME   if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }*/

        if (leght==1)
            fab.setVisibility(View.GONE);
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

    private void uploadNews(News objNews, int imagen, int title, int detail, int webView, int progressBar) {
        setImage(objNews.getImage(), imagen);
        setTextViewTitle(objNews.getTitle(),title);
        setTextViewDetail(objNews.getAuthor(), objNews.getDate(), objNews.getCategories(), detail);
        enableWebView(objNews.getContent(), webView);
        enableProgessBar(progressBar);
    }

    private void enableProgessBar(int progressBar) {
        ProgressBar bar = (ProgressBar) findViewById(progressBar);
        bar.setVisibility(View.VISIBLE);
    }

    private void setTextViewTitle(String title, int textTitle){
        TextView textView = (TextView) findViewById(textTitle);
        textView.setText(title);
    }

    private void createAdView(final int adViewOne , final int webViewContent, final int progressBar) {

        final AdView adView1 = (AdView)  findViewById(adViewOne);
        final WebView webViewCreate = (WebView) findViewById(webViewContent) ;
        final ProgressBar progresNew = (ProgressBar) findViewById(progressBar);

        flag_bookmarks = false;

        AdRequest adRequest = new AdRequest.Builder().build();
        if (adView1!=null && adRequest!=null) {
            adView1.loadAd(adRequest);
            adView1.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    //System.out.println("onAdFailedToLoad "+ adViewOne );
                    createAdView(adViewOne, webViewContent, progressBar);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView1.setVisibility(View.VISIBLE);
                    webViewCreate.setVisibility(View.VISIBLE);
                    progresNew.setVisibility(View.GONE);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }
            });
        }
    }

    private void enableWebView(String content, int webViewCreate){

        WebView webViewNew = (WebView) findViewById(webViewCreate);

        webViewNew.clearHistory();
        webViewNew.clearCache(true);
        webViewNew.clearFormData();

        webViewNew.getSettings().setJavaScriptEnabled(true);
        webViewNew.getSettings().setLoadWithOverviewMode(true);
        webViewNew.getSettings().setUseWideViewPort(true);
        webViewNew.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webViewNew.getSettings().setSaveFormData(false);

        String html = ViewConstants.HTML_HEAD + content;
        html =  html.replace("\\\"","\"").replace("\\n","\n");

        webViewNew.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", null);
        webViewNew.setVisibility(View.GONE);
    }

    private void setImage(String objImage, int image){
        ImageView imageview = (ImageView) findViewById(image);
        String url = objImage;
        Picasso.with(getApplicationContext())
                .load(url)
                .into(imageview);
    }


    private void setTextViewDetail(String authorCreate, String dateCreate, String category, int textDetail) {

        TextView detail = (TextView) findViewById(textDetail);

        String author = "Por: "+ authorCreate;
        String  date = "El: "+ dateCreate;
        String categories;
        if (category!=null && category!="")
            categories = "Categorias: " + category;
        else
            categories = "Categorias: " + "Popular";

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
        clearWebView(R.id.webView1);
        clearWebView(R.id.webView2);
        clearWebView(R.id.webView3);
        clearWebView(R.id.webView4);
        clearWebView(R.id.webView5);

        vf.clearAnimation();
        this.finish();
        super.onStop();
    }

    private void clearWebView(int webViewCreate) {

        WebView webView = (WebView) findViewById(webViewCreate);

        if (webView!=null){
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    private News getItemPosition(int postion) {
        News newsPosition = new News();
        //System.out.println(vf.getDisplayedChild());
        switch (postion){
            case 0:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }*/
                createAdView(R.id.adView1, R.id.webView1, R.id.progress1);
                break;
            case 1:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                clearAdView(R.id.adView1, R.id.webView1, R.id.progress1);
                createAdView(R.id.adView2, R.id.webView2, R.id.progress2);
                clearAdView(R.id.adView3, R.id.webView3, R.id.progress3);

                beginPlayingGame();

                break;
            case 2:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                clearAdView(R.id.adView2, R.id.webView2, R.id.progress2);
                createAdView(R.id.adView3, R.id.webView3, R.id.progress3);
                clearAdView(R.id.adView4, R.id.webView4, R.id.progress4);
                break;
            case 3:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                clearAdView(R.id.adView3, R.id.webView3, R.id.progress3);
                createAdView(R.id.adView4, R.id.webView4, R.id.progress4);
                clearAdView(R.id.adView5, R.id.webView5, R.id.progress5);
                break;
            case 4:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                createAdView(R.id.adView4, R.id.webView4, R.id.progress4);
                createAdView(R.id.adView5, R.id.webView5, R.id.progress5);
                clearAdView(R.id.adView1, R.id.webView1, R.id.progress1);
                break;
            default:
                newsPosition =  newsList.get(newsList.size()-1);

                clearAdView(R.id.adView2, R.id.webView2, R.id.progress2);
                clearAdView(R.id.adView5, R.id.webView5, R.id.progress5);

                beginPlayingGame();
                createAdView(R.id.adView1, R.id.webView1, R.id.progress1);

                break;
        }

        return newsPosition;
    }

    private void clearAdView(int adViewClear, int webView, int progressBar) {
        AdView adView = (AdView)  findViewById(adViewClear);
        if (adView!=null)
            adView.setVisibility(View.GONE);

        WebView view = (WebView) findViewById(webView);
        if (view!=null)
            view.setVisibility(View.GONE);

        ProgressBar bar = (ProgressBar) findViewById(progressBar);
        if (bar!=null)
            bar.setVisibility(View.VISIBLE);
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
                        vf.showPrevious();
                    }

                    if(distance<0)
                    {
                        vf.setInAnimation(inFromLeftAnimation());
                        vf.setOutAnimation(outToRightAnimation());

                        newsPosition = getItemPosition(vf.getDisplayedChild()+1);

                        vf.showNext();
                    }

                    uploadPreferences(newsPosition.getId());


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
       /* mTracker.setScreenName("Publicidad");
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Cerrar Publicidad")
                .setAction("Publicidad")
                .build());*/
        // [END custom_event]
    }

   /* FIXME  private void requestNewInterstitial() {

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(deviceId)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }*/
    //END PUBLICITY

    public void createListNews() {
        newsList.clear();
        newsList.add(newsPrimary);
        for (int i=0; i<leght; i++){
            switch (i){
                case 1:
                    uploadNews(newsSegundary, R.id.image2, R.id.title2, R.id.detail2,R.id.webView2, R.id.progress2);
                    newsList.add(newsSegundary);
                    break;
                case 2:
                    uploadNews(newsThree, R.id.image3, R.id.title3, R.id.detail3,R.id.webView3,R.id.progress3 );
                    newsList.add(newsThree);
                    break;
                case 3:
                    uploadNews(newsFour, R.id.image4, R.id.title4, R.id.detail4,R.id.webView4 ,R.id.progress4);
                    newsList.add(newsFour);
                    break;
                case 4:
                    uploadNews(newsFive, R.id.image5, R.id.title5, R.id.detail5,R.id.webView5, R.id.progress5);
                    newsList.add(newsFive);
                    break;
            }
        }
    }

    private void createShareIntent(){

       sendReportGoogleAnalytics(newsPosition.getLink(),"CompartirOtrosMedios" );

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
            sendReportGoogleAnalytics(newsPosition.getLink(), "CompartirBotonFacebook");
            // [END custom_event]


            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(newsPosition.getTitle())
                    .setContentDescription(getString(R.string.msg_detail_facebook))
                    .setContentUrl(Uri.parse(newsPosition.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    private void sendReportGoogleAnalytics(String link, String category) {

        // [START custom_event]
        mTracker.setScreenName(link);
        mTracker.setReferrer(link);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(link)
                .setAction(category)
                .build());
        // [END custom_event]

        /*
        *  // [START custom_event]
        mTracker.setScreenName(newsPosition.getLink());
        mTracker.setReferrer(newsPosition.getLink());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("CompartirOtrosMedios")
                .setAction(newsPosition.getLink())
                .build());
        // [END custom_event]*/
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



}
