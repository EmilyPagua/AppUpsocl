package cl.upsocl.upsoclapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.upsocl.upsoclapp.AnalyticsApplication;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.Preferences;
import com.upsocl.upsoclapp.ui.ViewConstants;

/**
 * Created by emily.pagua on 21-10-16.
 */

public class DetailPageFragment extends Fragment {

    private static final String TAG = "DetailPageFragment";
    private static final String ARG_NEWS = "news";
    private PreferencesManager manager;
    private boolean flag_bookmarks;
    private News news;

    private Gson gs = new Gson();

    private AdView mAdView;
    private ProgressBar progresNew;
    private WebView webViewNew;

    private String html = "";

    //Element Facebook
    CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private ImageButton  bookmark;
    private AdView adViewUp;
    private Boolean  loadAdViewUp = false;

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

    // [START shared_tracker]
    private Tracker mTracker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isConnect()){
            if (getArguments() != null) {
                news = gs.fromJson(getArguments().getString(ARG_NEWS), News.class);
            }
        }else{
            Toast.makeText(DetailPageFragment.this.getActivity(), "Error: No tiene conexión a red!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DetailPageFragment.this.getActivity(), MenuHomeActivity.class);
            startActivity(intent);
            DetailPageFragment.this.getActivity().finish();
        }
    }

    public static DetailPageFragment newInstance(@NonNull  News newsItem){

        DetailPageFragment fragment = new DetailPageFragment();
        Gson gs = new Gson();
        Bundle bundle =  new Bundle();
        String target = gs.toJson(newsItem);
        bundle.putString(ARG_NEWS,target);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container,false);
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.viewDetailLinear);

        progresNew = (ProgressBar) rootView.findViewById(R.id.progressBarPost);
        progresNew.setVisibility(View.VISIBLE);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadImageView(news,layout);
                loadDetail(news, rootView);
            }
        });


        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainerPost);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG + " swipeContainer", "refesh");
                webViewNew.reload();
                swipeContainer.setRefreshing(false);
            }
        });
        //Content Faceboook
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, resultFacebookCallback);
        //End content facebook

        // [START shared_tracker]
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        flag_bookmarks = false;
        manager =  new PreferencesManager(getContext().getApplicationContext());

        //END PUBLICITY
        bookmark = (ImageButton) rootView.findViewById(R.id.bookmarkButton);
        uploadPreferences();
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_bookmarks){
                    removePreference();
                } else {
                    savePreferences();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                new loadAdMob().execute(layout);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AdRequest adRequest = new AdRequest.Builder().build();
                        adViewUp = new AdView(DetailPageFragment.this.getContext());
                        adViewUp.setAdSize(AdSize.MEDIUM_RECTANGLE);
                        adViewUp.setAdUnitId(getString(R.string.banner_up));
                        adViewUp.setPadding(0, 0, 0, 0);
                        adViewUp.loadAd(adRequest);
                        adViewUp.setVisibility(View.GONE);
                        adViewUp.setAdListener(new AdListener() {
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

                                loadAdViewUp =  true;
                            }
                        });

                        layout.addView(adViewUp);

                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        loadWebView(layout);
                        }catch (Exception e){
                            Log.e(TAG + " comeBack", e.getMessage());
                        }
                    }
                });
            }
        }).start();
        loadButton(rootView);

        return  rootView;
    }

    public void loadButton(ViewGroup rootView) {

        ImageButton comeBack = (ImageButton) rootView.findViewById(R.id.comeBackButton);
        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ImageButton  shareFacebook = (ImageButton) rootView.findViewById(R.id.shareFacebookButton);
        shareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookShare();
                Log.e(TAG + " shareFacebook", "shareFacebook");
            }
        });

        ImageButton  share = (ImageButton) rootView.findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareIntent();
                Log.e(TAG + " share", "share");

            }
        });
    }

    public void loadWebView(final LinearLayout layout) {

        webViewNew = new WebView(DetailPageFragment.this.getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30,20,30,0);

        webViewNew.setLayoutParams(layoutParams);

        webViewNew.clearHistory();
        webViewNew.clearCache(true);
        webViewNew.clearFormData();

        webViewNew.getSettings().setJavaScriptEnabled(true);
        webViewNew.getSettings().setLoadWithOverviewMode(true);
        webViewNew.getSettings().setUseWideViewPort(true);
        webViewNew.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webViewNew.getSettings().setSaveFormData(false);


        if (news!=null) {
            sendReportGoogleAnalytics(news.getLink(), "ClickPost");

            String html = createContentHTML();
            html = html.replace("\\\"", "\"").replace("\\n", "\n");
            html = html.replaceAll("(class)[=][\"](wp-image-)\\d{6}[\"]","class=\\\"wp-image-511029 size-full\\\" ");

            webViewNew.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", null);
            layout.addView(webViewNew);
            webViewNew.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Log.e(TAG + " DetailPageFragment webView", error.toString());
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.e(TAG + "  webView", "onPageFinished");
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    Log.e(TAG + " webView", description);
                }
            });
            //webViewNew.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            //webViewNew.setLayerType(WebView.LAYER_TYPE_SOFTWARE,null);
            webViewNew.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webViewNew.setVisibility(View.GONE);
        }
    }

    @NonNull
    public String createContentHTML() {
        String topHTML = "<html><header> " + ViewConstants.HTML_HEAD+ "</header><body>";

        String imageMainHTML = "<center><img align='middle' alt='Portada' class='wp-image-480065 size-full' " +
                "height='605' itemprop='contentURL' sizes='(max-width: 728px) 100vw, 728px' src=" + news.getImage() + " width='728' > </center>";

        String titleHTML = "<h2 style='text-align: justify;'><strong> " + news.getTitle() + "</strong></h2>";
        String authorHTML = "<div class='entry-meta socialtop socialextra'>  " +
                "Autor: <font color='#009688'>" + news.getAuthor() + "</font>.  " +
                "El: <font color='#009688'> " + news.getDate() + " </font> ";
        String categoryHTML = "<br/> Categorias: <font color='#009688'>" + news.getCategories() + "</font> </div> ";
        String lineHTML = "<hr  color='#009688' />";
        String contentHTML = news.getContent();
        String bottomHTML = "</body> </html>";

        return topHTML +contentHTML +bottomHTML;
    }

    private class loadAdMob  extends AsyncTask <LinearLayout, Integer, Boolean> {

        private AdView adView;
        private AdRequest adRequest;
        private LinearLayout layout;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                adRequest = new AdRequest.Builder().build();
                adView = new AdView(DetailPageFragment.this.getContext());
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adView.setAdUnitId(getString(R.string.banner_down));
                adView.setPadding(0, 0, 0, 3);
            }
            catch (Exception e){
                Log.e("loadAdMob onAdFailedToLoad", e.getMessage());
                adView = null;
            }
        }

        @Override
        protected Boolean doInBackground(LinearLayout... voids) {
            layout = voids[0];

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        if (adView!=null && adRequest!=null) {
                            adView.loadAd(adRequest);
                            adView.setAdListener(new AdListener() {

                                @Override
                                public void onAdFailedToLoad(int i) {
                                    Log.e(TAG + "  loadAdMob onAdFailedToLoad", String.valueOf(i));
                                    super.onAdFailedToLoad(i);
                                    if (webViewNew.getVisibility() == View.GONE){
                                        webViewNew.setVisibility(View.VISIBLE);
                                        progresNew.setVisibility(View.GONE);
                                    }
                                    new loadAdMob().execute(layout);
                                }
                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                    if (loadAdViewUp)
                                        adViewUp.setVisibility(View.VISIBLE);

                                    webViewNew.setVisibility(View.VISIBLE);
                                    adView.setVisibility(View.VISIBLE);
                                    progresNew.setVisibility(View.GONE);
                                    layout.addView(adView);

                                }
                                @Override
                                public void onAdLeftApplication() {
                                    super.onAdLeftApplication();
                                }

                                @Override
                                public void onAdOpened() {
                                    super.onAdOpened();
                                }
                            });
                        }
                    }catch (Exception e){
                        Log.e(TAG + " loadAdMob onAdFailedToLoad", e.getMessage());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
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
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public boolean isConnect() {

        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation") NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public void facebookShare() {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            // [START custom_event]
            sendReportGoogleAnalytics(news.getLink(), "CompartirBotonFacebook");
            // [END custom_event]

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(news.getTitle())
                    .setImageUrl(Uri.parse(news.getImage()))
                    .setContentDescription(getString(R.string.msg_detail_facebook))
                    .setContentUrl(Uri.parse(news.getLink()))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    public void sendReportGoogleAnalytics(String link, String category) {

        // [START custom_event]

        mTracker.setScreenName(link);
        mTracker.setReferrer(link);
        mTracker.send(new HitBuilders.EventBuilder()
                .setLabel(link)
                .setCategory(link)
                .setAction(category)
                .build());

        GoogleAnalytics.getInstance(this.getActivity()).dispatchLocalHits();
    }


    private void createShareIntent(){

        sendReportGoogleAnalytics(news.getLink(),"CompartirOtrosMedios" );

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, news.getLink());
        this.startActivity(Intent.createChooser(shareIntent,  "Compartir: " +news.getTitle()));
    }

    private void removePreference() {
        flag_bookmarks = false;
        Toast.makeText(getContext().getApplicationContext(), "No esta como preferido", Toast.LENGTH_SHORT).show();
        manager.RemovePreferences(Preferences.BOOKMARKS,
                String.valueOf(news.getId()));
        bookmark.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
    }

    private void savePreferences() {

        try{
            Gson gS = new Gson();
            String target = gS.toJson(news);

            manager.SavePreferencesString(Preferences.BOOKMARKS,news.getId(),target);

            if (!news.getId().equals("0"))
            {
                flag_bookmarks = true;
                Toast.makeText(getContext().getApplicationContext(), "Salvado como preferido", Toast.LENGTH_SHORT).show();
                bookmark.setImageResource(R.drawable.ic_bookmark_white_24dp);
            }
        }catch (Exception e){
            Log.e(TAG + " savePreferences", e.getMessage());
        }
    }

    private void uploadPreferences() {
        if (news != null) {
            String objeto = manager.getPreferenceString(Preferences.BOOKMARKS, news.getId());
            if (objeto!=null){
                bookmark.setImageResource(R.drawable.ic_bookmark_white_24dp);
                flag_bookmarks = true;
            }
            else{
                bookmark.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
    }

    private void loadImageView(News news, ViewGroup rootView ) {

        try{
            ImageView imageview = (ImageView) rootView.findViewById(R.id.imageViewDetail);
            if (news!=null &&  news.getImage()!=null){
                String urlImagen = news.getImage();
                Picasso.with(getActivity().getApplicationContext())
                        .load(urlImagen)
                        .into(imageview);
            }else{
                imageview.setVisibility(View.GONE);
            }

        }catch (Exception e){
            Log.e(TAG + "loadImageView ", e.getMessage());
        }

    }

    private void loadDetail(News news, ViewGroup rootView) {

        TextView title = (TextView) rootView.findViewById(R.id.detailTextView);
        title.setText(news.getTitle());

        TextView detail = (TextView) rootView.findViewById(R.id.detail);
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

    private SpannableStringBuilder setStyleText(String text, int i, int length) {
        SpannableStringBuilder result = new SpannableStringBuilder(text);
        result.setSpan(new ForegroundColorSpan(Color.parseColor("#009688")), i, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return result;
    }
}
