package cl.upsocl.upsoclapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.AnalyticsApplication;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.ui.ViewConstants;

import org.neotech.library.retainabletasks.providers.TaskActivityCompat;

import java.util.Objects;

import static java.lang.Thread.sleep;


public class NotificationActivity extends TaskActivityCompat {
    private static final String TAG = "NotificationActivity";
    private Gson gs = new Gson();
    private WebView webView;
    private MenuItem item_bookmark;
    private News news;
    private boolean flag_bookmarks;
    private ProgressBar progresNew;
    private Handler handler =  new Handler(Looper.getMainLooper());

    boolean isBookmarks = false;
    // [START shared_tracker]
    ReportAnalytics analytics;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setToolBar();
        isBookmarks = getIntent().getBooleanExtra("isBookmarks", false);

        if (isBookmarks){
            news =  gs.fromJson(getIntent().getStringExtra("new"), News.class);
        }else{
            SharedPreferences prefs =  getSharedPreferences(Preferences.NOTIFICATIONS, Context.MODE_PRIVATE);
            news = gs.fromJson(prefs.getString(Preferences.NOTI_DATA,null), News.class);
        }
        progresNew = (ProgressBar) findViewById(R.id.progressNotification);

        if (news!=null) {

            //Content Faceboook
            FacebookSdk.sdkInitialize(getApplicationContext());
            CallbackManager callbackManager = CallbackManager.Factory.create();
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, resultFacebookCallback);
            //End content facebook

            loadAdView(R.id.adViewNotificationUp);
            loadAdView(R.id.adViewNotification);

            loadWebView(news);

            handler.postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    loadImageView(news);
                    loadDetail(news);
                }
            });

            // [START shared_tracker]
            AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
            analytics = new ReportAnalytics(application, this);
            analytics.sendReportGoogleAnalytics(news.getLink(), "ClickPost");
        }
    }

    private void loadAdView(final int adViewNotification) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final AdView adView = (AdView)  findViewById(adViewNotification);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("Error adView1", String.valueOf(i));
                    }
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                            adView.setVisibility(View.VISIBLE);
                            progresNew.setVisibility(View.GONE);
                            webView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notification);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.app_notifications);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBookmarks)
                    onBackPressed();
                else
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


    private void loadDetail(News news) {
        TextView title = (TextView) findViewById(R.id.detailTextView);
        title.setText(news.getTitle());

        TextView detail = (TextView) findViewById(R.id.detail);
        String author = "Por: " + news.getAuthor();
        String date = "El: " + news.getDate();
        String category = news.getCategories();

        if (category!=null && !category.equals(""))
            category = "Categorias: " + category;
        else
            category = "Categorias: " + "Popular";

        SpannableStringBuilder sBauthor = setStyleText(author, 4,author.length());
        SpannableStringBuilder sBdetail = setStyleText(date, 3,date.length());
        SpannableStringBuilder sBcategory = setStyleText(category, 10, category.length());

        Spanned spanned = (Spanned) TextUtils.concat(sBauthor, ". ", sBdetail, ". ",sBcategory);
        SpannableStringBuilder result = new SpannableStringBuilder(spanned);

        detail.setText(result, TextView.BufferType.SPANNABLE);
    }

    private void loadImageView(News news) {

        try{
            ImageView imageview = (ImageView) findViewById(R.id.imageViewDetail);
            if (news!=null &&  news.getImage()!=null){
                String urlImagen = news.getImage();

                Picasso.with(getApplicationContext())
                        .load(urlImagen)
                        .into(imageview);
            }else{
                imageview.setVisibility(View.GONE);
            }

        }catch (Exception e){
            Log.e(TAG + "loadImageView ", e.getMessage());
        }
    }

    public void loadWebView(final News news) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView = (WebView) findViewById(R.id.webView);

                String topHTML = "<html><header> " + ViewConstants.HTML_HEAD+ "</header><body>";

                String html = topHTML + news.getContent() +" </body></html>";
                html =  html.replace("\\\"","\"").replace("\\n","\n");
                html =  html.replaceAll("(class)[=][\"](wp-image-)\\d{6}[\"]","class=\\\"wp-image-511029 size-full\\\" ");

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                webView.getSettings().setSaveFormData(false);
                webView.getSettings().setDomStorageEnabled(true);

                webView.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", "");
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.e("NotificationActivity webView", error.toString());
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progresNew.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }
                });


                try
                {
                    progresNew.setVisibility(View.VISIBLE);
                    sleep(1000);
                    webView.setVisibility(View.VISIBLE);
                }
                catch (InterruptedException e)
                {
                    Log.e(TAG, "Try del progress bar");
                }
            }
        });
    }

    private SpannableStringBuilder setStyleText(String text, int i, int length) {
        SpannableStringBuilder result = new SpannableStringBuilder(text);
        result.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), i, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        item_bookmark = menu.findItem(R.id.menu_item_bookmarks);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        uploadPreferences(news.getId());
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
        String objeto = prefs2.getString(id,null);
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
        String target = gS.toJson(news);

        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();

        prefs.getAll().size();
        if (!Objects.equals(news.getId(), "0"))
        {
            flag_bookmarks = true;
            Toast.makeText(this, "Salvado como preferido", Toast.LENGTH_SHORT).show();
            editor.putString(String.valueOf(news.getId()), target);
            editor.apply();
            item.setIcon(R.drawable.ic_bookmark_white_36dp);
        }
    }

    private void createShareIntent(){
        analytics.sendReportGoogleAnalytics(news.getLink(),"CompartirOtrosMedios" );

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, news.getLink());
        this.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +news.getTitle()));
    }

    private void removePreference(MenuItem item) {
        flag_bookmarks = false;
        Toast.makeText(this, "No esta como preferido", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs =  getSharedPreferences(Preferences.BOOKMARKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.remove(String.valueOf(news.getId())).apply();
        item.setIcon(R.drawable.ic_bookmark_border_white_36dp);
    }

    public void facebookShare() {

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            analytics.sendReportGoogleAnalytics(news.getLink(), "CompartirBotonFacebook");

            ShareDialog shareDialog = new ShareDialog(this);

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(news.getTitle())
                    .setImageUrl(Uri.parse(news.getImage()))
                    .setContentDescription(getString(R.string.msg_detail_facebook))
                    .setContentUrl(Uri.parse(news.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }
}
