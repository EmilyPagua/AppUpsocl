package com.upsocl.appupsocl.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.io.ApiConstants;
import com.upsocl.appupsocl.io.WordpressApiAdapter;
import com.upsocl.appupsocl.ui.adapters.NewsAdapter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by leninluque on 09-11-15.
 */
public class NewsFragment extends Fragment implements Callback<ArrayList<News>> {
    private RecyclerView newsList;
    private NewsAdapter adapter;
    private Integer page;
    private LinearLayoutManager llm;
    private ProgressBar spinner;
    private String word;
    private TextView header_news;

    @SuppressLint("ValidFragment")
    public NewsFragment(String word) {
        this.word = word;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        page = 1;
        loadPosts(page);

        header_news = (TextView) root.findViewById(R.id.header_news_search);
        newsList = (RecyclerView) root.findViewById(R.id.news_list_search);
        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(getActivity(), false);
        newsList.setAdapter(adapter);
        spinner = (ProgressBar) getActivity().findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);

        newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int size = llm.getItemCount();
            if (size == llm.findLastCompletelyVisibleItemPosition() + 1) {
                page = page + 1;
                spinner.setVisibility(View.VISIBLE);
                loadPosts(page);
            }
            }
        });

        return root;
    }

    @Override
    public void success(ArrayList<News> newses, Response response) {
        if (newses.size()==0)
            header_news.setText("No se encontraron resultados para la busqueda: " + word);

        adapter.addAll(newses);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void failure(RetrofitError error) {
        adapter.addAll(new ArrayList<News>());
    }

    public void loadPosts(Integer page){

        WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListWord(word,page, this);

    }


}
