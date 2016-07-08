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
    private Integer tabSelected;

    @SuppressLint("ValidFragment")
    public NewsFragment(Integer tabSelected) {
        this.tabSelected = tabSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        page = 1;
        loadPosts(page, tabSelected);

        newsList = (RecyclerView) root.findViewById(R.id.news_list);
        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(getActivity());
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
                Log.d(getTag(), "SI load more findLastCompletelyVisibleItemPosition->" + llm.findLastCompletelyVisibleItemPosition() + "---size:" + size);
                page = page + 1;
                spinner.setVisibility(View.VISIBLE);
                loadPosts(page,tabSelected);
            } else {
                Log.d(getTag(), "NO load more findLastCompletelyVisibleItemPosition->" + llm.findLastCompletelyVisibleItemPosition() + "---size:" + size);
            }
            }
        });

        return root;
    }

    @Override
    public void success(ArrayList<News> newses, Response response) {
        adapter.addAll(newses);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void failure(RetrofitError error) {
        adapter.addAll(new ArrayList<News>());
    }

    public void loadPosts(Integer paged, Integer tabSelected){
        switch (tabSelected){
            case 0:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListNews(paged, this);
                break;}
            case 1:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListGreen(paged, this);
                break;}
            case 2:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListFood(paged, this);
                break;}
            case 3:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListCreativity(paged, this);
                break;}
            case 4:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListWomen(paged, this);
                break;}
            case 5:{
                WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListPopulary(paged, this);
                break;}
        }

    }


}
