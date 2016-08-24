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
    // FIXME private InterstitialAd mInterstitialAd;

    //END PUBLICITY

    private ViewFlipper vf;
    private float init_x;

    // [START shared_tracker]
    private Tracker mTracker;

    //Element Facebook
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private TextView textViewTitle, textViewDetail;
    private WebView webView1,webView2,webView3,webView4,webView5;


    private ProgressDialog dialog;
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

        textViewTitle = (TextView) findViewById(R.id.detailTextView);
        textViewDetail = (TextView) findViewById(R.id.detail);

        webView1 = (WebView) findViewById(R.id.webViewPrimary);
        webView2 = (WebView) findViewById(R.id.webViewSegundary);
        webView3 = (WebView) findViewById(R.id.webViewThree);
        webView4 = (WebView) findViewById(R.id.webViewFour);
        webView5 = (WebView) findViewById(R.id.webViewFive);

        isBookmarks =  getIntent().getBooleanExtra("isBookmarks",false);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.realtiveContent);

        if (android.os.Build.VERSION.SDK_INT <= 21){
            layout.setPadding(5,5,5,0);
        }

        textViewTitle.setText(newsPrimary.getTitle());
        uploadImagensNews(newsPrimary, R.id.imageViewDetail);
        setTextViewDetail(newsPrimary.getAuthor(), newsPrimary.getDate(), newsPrimary.getCategories());

        newsList.add(newsPrimary);
        newsPosition=newsPrimary;

        //UploadPublicty

        dialog = ProgressDialog.show(DetailPostActivity.this, getString(R.string.msg_dialog_postDetail),
                getString(R.string.msg_dialog_content), true);

       final AdView adView1 = (AdView) findViewById(R.id.adView_primary_one);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (adView1!=null && adRequest!=null) {
            adView1.loadAd(adRequest);
            adView1.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                    final AdView adView2 = (AdView) findViewById(R.id.adView_primary_two);
                    AdRequest adRequest2 = new AdRequest.Builder().build();
                    if (adView2!=null && adRequest2!=null) {
                        adView2.loadAd(adRequest2);
                        adView2.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdLeftApplication() {
                                super.onAdLeftApplication();
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                if (dialog!=null && dialog.isShowing()){
                                    dialog.dismiss();
                                    uploadNews(newsPrimary, R.id.webViewPrimary);
                                }

                            }
                        });
                    }
                }
            });
        }


        //End UploadPublicty

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

        // [START custom_event]
        mTracker.setScreenName(newsPrimary.getLink());
        mTracker.setReferrer(newsPrimary.getLink());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("ClickPost")
                .setAction(newsPrimary.getLink())
                .build());
        // [END custom_event]t]
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

        ImageView imageview = (ImageView) findViewById(R.id.imageViewDetail);
        imageview.setOnTouchListener(new ListenerTouchViewFlipper());

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

    private void uploadNews(News objNews, int webViewContext) {

        textViewTitle.setText(objNews.getTitle());
        setTextViewDetail(objNews.getAuthor(), objNews.getDate(), objNews.getCategories());
        enableWebView(objNews.getContent(),webViewContext);
    }

    private void enableWebView(String content, int webViewCreate){

        clearAllWebView();

        WebView webViewNew = (WebView) findViewById(webViewCreate);

        webViewNew.getSettings().setJavaScriptEnabled(true);
        webViewNew.getSettings().setLoadWithOverviewMode(true);
        webViewNew.getSettings().setUseWideViewPort(true);
        webViewNew.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webViewNew.getSettings().setSaveFormData(false);

        String html = ViewConstants.HTML_HEAD + content;
        html =  html.replace("\\\"","\"").replace("\\n","\n");

        webViewNew.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", null);
        webViewNew.setVisibility(View.VISIBLE);
    }

    private void clearAllWebView() {
        webView1.setVisibility(View.GONE);
        webView2.setVisibility(View.GONE);
        webView3.setVisibility(View.GONE);
        webView4.setVisibility(View.GONE);
        webView5.setVisibility(View.GONE);
    }

    private void setImage(String objImage, int image){
        ImageView imageview = (ImageView) findViewById(image);
        String url = objImage;
        Picasso.with(getApplicationContext())
                .load(url)
                .into(imageview);
    }


    private void setTextViewDetail(String authorCreate, String dateCreate, String category) {

        String author = "Por: "+ authorCreate;
        String  date = "El: "+ dateCreate;
        String categories = "Categorias: " + category;

        SpannableStringBuilder sBauthor = setStyleText(author, 4,author.length());
        SpannableStringBuilder sBdetail = setStyleText(date, 3,date.length());
        SpannableStringBuilder sBcategory = setStyleText(categories, 10,categories.length());

        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail, ". ",sBcategory);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        textViewDetail.setText(result, TextView.BufferType.SPANNABLE);
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
        clearWebView(R.id.webViewPrimary);
        clearWebView(R.id.webViewSegundary);
        clearWebView(R.id.webViewThree);
        clearWebView(R.id.webViewFour);
        clearWebView(R.id.webViewFive);

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
        System.out.println("Cambiar a " + vf.getDisplayedChild());
        switch (postion){
            case 0:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                uploadNews(newsPosition, R.id.webViewPrimary);
                /* FIXME if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }*/
                break;
            case 1:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                uploadNews(newsPosition, R.id.webViewSegundary);

                break;
            case 2:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                uploadNews(newsPosition, R.id.webViewThree);
                break;
            case 3:
                if (newsList.size()>postion)
                    newsPosition =  newsList.get(postion);
                else
                    newsPosition =  newsList.get(newsList.size()-1);

                uploadNews(newsPosition, R.id.webViewFour);
                break;
            default:
                newsPosition =  newsList.get(newsList.size()-1);

                uploadNews(newsPosition, R.id.webViewFive);
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
        mTracker.setScreenName("Publicidad");
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Cerrar Publicidad")
                .setAction("Publicidad")
                .build());
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
        for (int i=0; i<leght; i++){
            switch (i){
                case 1:
                    uploadImagensNews(newsSegundary, R.id.imageViewDetailSegundary);

                   /* uploadNews(newsSegundary, R.id.imageViewDetailSegundary, 0,
                            0,0, 0,0, R.id.viewSegundary );*/
                    newsList.add(newsSegundary);
                    break;
                case 2:
                    uploadImagensNews(newsThree, R.id.imageViewDetailThree);
                    /*uploadNews(newsThree, R.id.imageViewDetailThree, 0,
                            0,0,0, 0, R.id.viewThree);*/
                    newsList.add(newsThree);
                    break;
                case 3:
                    uploadImagensNews(newsFour, R.id.imageViewDetailFour);
                   /* uploadNews(newsFour, R.id.imageViewDetailFour,0,
                            0,0, 0, 0, R.id.viewFour);*/
                    newsList.add(newsFour);
                    break;
                case 4:
                    uploadImagensNews(newsFive, R.id.imageViewDetailFive);
                    /*uploadNews(newsFive, R.id.imageViewDetailFive,0,
                            0,0, 0, 0, R.id.viewFive);*/
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


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void uploadImagensNews(News objNews, int imageViewDetail) {
        setImage(objNews.getImage(), imageViewDetail);
    }

}
