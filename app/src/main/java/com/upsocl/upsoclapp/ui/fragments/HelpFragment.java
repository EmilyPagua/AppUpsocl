package com.upsocl.upsoclapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.io.ApiConstants;
import com.upsocl.upsoclapp.io.WordpressApiAdapter;
import com.upsocl.upsoclapp.ui.ViewConstants;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by emily.pagua on 13-07-16.
 */

public class HelpFragment extends Fragment {

    News news = new News();
    WebView webViewNew;
    ProgressBar bar;
    public HelpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        bar = (ProgressBar) root.findViewById(R.id.progress_bar_help);
        bar.setVisibility(View.VISIBLE);

        webViewNew = (WebView) root.findViewById(R.id.webViewContent);
        load();
        return root;
    }

    public void load() {

        String page = "1039";
        WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getTerminos(page, new Callback<News>() {

            @Override
            public void success(News newsContent, Response response) {
                System.out.println("Response");
                news = newsContent;

                webViewNew.getSettings().setJavaScriptEnabled(true);
                webViewNew.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webViewNew.getSettings().setLoadWithOverviewMode(true);
                webViewNew.getSettings().setUseWideViewPort(true);
                webViewNew.getSettings().setAllowUniversalAccessFromFileURLs(true);
                webViewNew.getSettings().setSaveFormData(false);

                String html = ViewConstants.HTML_HEAD  + news.getContent();
                html = html.replace("\\\"", "\"").replace("\\n", "\n");

                webViewNew.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
               Log.e("HelpFragment",error.getMessage());
            }
        });

    }
}