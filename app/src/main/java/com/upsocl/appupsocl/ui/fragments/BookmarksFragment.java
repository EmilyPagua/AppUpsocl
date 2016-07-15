package com.upsocl.appupsocl.ui.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upsocl.appupsocl.R;
import com.upsocl.appupsocl.domain.News;
import com.upsocl.appupsocl.io.model.JsonKeys;
import com.upsocl.appupsocl.ui.adapters.NewsAdapter;


import java.util.ArrayList;
import java.util.Map;


public class BookmarksFragment extends Fragment {

    private RecyclerView newsList;
    private NewsAdapter adapter;
    private Integer page;
    private LinearLayoutManager llm;
    private ProgressBar spinner;
    private TextView header_news;
    private SharedPreferences preferences;

    public BookmarksFragment(SharedPreferences preferences) {
        this.preferences=preferences;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceStates){
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        page = 1;
        uploadPreferences();

        loadPosts();

        header_news = (TextView) root.findViewById(R.id.header_news);
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
                    page = page + 1;
                    spinner.setVisibility(View.VISIBLE);
                    loadPosts();
                }
            }
        });

        return root;
    }

    public void loadPosts(){

        Map<String, ?> map = preferences.getAll();
        map.size();

        ArrayList<News> newses= new ArrayList<>();

        for (Map.Entry<String, ?> e: map.entrySet()) {
            //System.out.println("["+e.getKey() + "=" + e.getValue()+"]");
            System.out.println("["+e.getKey());
            JsonObject root = new JsonParser().parse(e.getValue().toString()).getAsJsonObject();

            News news = new News();
            news.setId(root.get(JsonKeys.NEWS_ID).getAsString());
            news.setId(root.get(JsonKeys.NEWS_CONTENT).getAsString());
            news.setId(root.get(JsonKeys.NEWS_TITLE).getAsString());
            news.setId(root.get(root.get(JsonKeys.NEWS_AUTHOR_FIRST_NAME) +" "+root.get(JsonKeys.NEWS_AUTHOR_LAST_NAME)).getAsString());
            newses.add(news);

        }

        adapter.addAll(newses);
        spinner.setVisibility(View.GONE);




        /*


            if (newses.size()==0)
            header_news.setText("No se encontraron resultados");

        adapter.addAll(newses);
        spinner.setVisibility(View.GONE);

        adapter.addAll(new ArrayList<News>());*/

    }

    private void uploadPreferences() {


    }
}
