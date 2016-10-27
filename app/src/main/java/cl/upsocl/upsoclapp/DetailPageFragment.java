package cl.upsocl.upsoclapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.upsocl.upsoclapp.AnalyticsApplication;
import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.Preferences;

/**
 * Created by upsocl on 21-10-16.
 */

public class DetailPageFragment extends Fragment {

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
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private ImageButton  bookmark;

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
        final ViewGroup rootView;
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container,false);
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.viewDetailLinear);

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
                        loadWebView(layout);
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
                Log.e("comeBack", "comeback");
                getActivity().onBackPressed();
            }
        });

        ImageButton  shareFacebook = (ImageButton) rootView.findViewById(R.id.shareFacebookButton);
        shareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookShare();
                Log.e("shareFacebook", "shareFacebook");
            }
        });

        ImageButton  share = (ImageButton) rootView.findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareIntent();
                Log.e("share", "share");
            }
        });
    }

    public void loadWebView(final LinearLayout layout) {

        progresNew = new ProgressBar(DetailPageFragment.this.getContext());
        layout.addView(progresNew);

        webViewNew = new WebView(DetailPageFragment.this.getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10,0,10,0);

        webViewNew.setLayoutParams(layoutParams);

        webViewNew.clearHistory();
        webViewNew.clearCache(true);
        webViewNew.clearFormData();

        webViewNew.getSettings().setJavaScriptEnabled(true);
        webViewNew.getSettings().setLoadWithOverviewMode(true);
        webViewNew.getSettings().setUseWideViewPort(true);
        webViewNew.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webViewNew.getSettings().setSaveFormData(false);
        webViewNew.getSettings().getLoadsImagesAutomatically();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewNew.enableSlowWholeDocumentDraw();
        }
        if (news!=null) {
            sendReportGoogleAnalytics(news.getLink(), "ClickPost");

            String html = createContentHTML();
            html = html.replace("\\\"", "\"").replace("\\n", "\n");
            webViewNew.loadDataWithBaseURL("http://api.instagram.com/oembed", html, "text/html", "UTF-8", null);
            layout.addView(webViewNew);
            webViewNew.setVisibility(View.GONE);
        }
    }

    @NonNull
    public String createContentHTML() {
        String topHTML = "<html><header> " +
                "<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'>" +
                "<link href='http://fonts.googleapis.com/css?family=Raleway:400,600' rel='stylesheet' type='text/css'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'>" +
                "<link rel='stylesheet' type='text/css' media='all' href='http://www.upsocl.com/wp-content/themes/upso3/style.css'>" +
                "</header><body>";
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

        return new StringBuilder()
                .append(topHTML)
                .append(imageMainHTML)
                .append(titleHTML)
                .append(authorHTML)
                .append(categoryHTML)
                .append(lineHTML)
                .append(contentHTML)
                .append(bottomHTML)
                .toString();
    }

    private class loadAdMob  extends AsyncTask <LinearLayout, Integer, Boolean> {

        private AdView adView;
        private AdRequest adRequest;
        private LinearLayout layout;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adRequest = new AdRequest.Builder().build();
            adView = new AdView(DetailPageFragment.this.getContext());
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId("ca-mb-app-pub-7682123866908966/7102497723");
            adView.setPadding(0,0,0,8);
        }

        @Override
        protected Boolean doInBackground(LinearLayout... voids) {
            layout = voids[0];

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (adView!=null && adRequest!=null) {
                        adView.loadAd(adRequest);
                        adView.setAdListener(new AdListener() {

                            @Override
                            public void onAdFailedToLoad(int i) {
                                Log.e("loadAdMob onAdFailedToLoad", String.valueOf(i));
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
        NetworkInfo[] redes = connec.getAllNetworkInfo();
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

            if (news.getId().equals("0")==false)
            {
                flag_bookmarks = true;
                Toast.makeText(getContext().getApplicationContext(), "Salvado como preferido", Toast.LENGTH_SHORT).show();
                bookmark.setImageResource(R.drawable.ic_bookmark_white_24dp);
            }
        }catch (Exception e){
            Log.e("savePreferences", e.getMessage());
        }
    }

    private void uploadPreferences() {

        String objeto = manager.getPreference(Preferences.BOOKMARKS, news.getId());
        if (objeto!=null){
            bookmark.setImageResource(R.drawable.ic_bookmark_white_24dp);
            flag_bookmarks = true;
        }
        else{
            bookmark.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
        }
    }

}
