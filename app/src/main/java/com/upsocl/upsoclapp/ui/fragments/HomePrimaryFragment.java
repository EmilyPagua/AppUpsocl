package com.upsocl.upsoclapp.ui.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.upsocl.upsoclapp.R;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.io.ApiConstants;
import com.upsocl.upsoclapp.io.WordpressApiAdapter;
import com.upsocl.upsoclapp.ui.adapters.NewsAdapter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomePrimaryFragment extends Fragment implements Callback<ArrayList<News>> {

    private RecyclerView newsList;
    private NewsAdapter adapter;
    private Integer page;
    private LinearLayoutManager llm;
    private ProgressBar spinner;
    private TextView header_news;
    private String categoryName;
    private Boolean isHome;

    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_primary, container, false);


        uploadView(root);

        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uploadView(root);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.color_accent_home,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return root;
    }

    private void uploadView(View root) {
        page = 1;
        loadPosts(page);
        header_news = (TextView) root.findViewById(R.id.header_food);
        newsList = (RecyclerView) root.findViewById(R.id.news_list);
        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(getActivity(), isHome);
        newsList.setAdapter(adapter);
        spinner = (ProgressBar) getActivity().findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);

        newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int size = llm.getItemCount();
                if (size == llm.findLastCompletelyVisibleItemPosition() + 4) {
                    page = page + 1;
                    spinner.setVisibility(View.VISIBLE);
                    loadPosts(page);
                }
            }
        });
    }

    @Override
    public void success(ArrayList<News> newses, Response response) {
        header_news.setVisibility(View.GONE);
        if (newses.size()==0){
            header_news.setText("No se encontraron resultados");
            header_news.setVisibility(View.VISIBLE);
        }

        adapter.addAll(newses);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void failure(RetrofitError error) {
        //System.out.println("HomePrimaryFragment "+error.getMessage() );
        try{
            Toast.makeText(HomePrimaryFragment.this.getContext(), "Ha ocurrido un error, verifique su conexión a red", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("HomePrimaryFragment", "Error en failure");
        }
        //adapter.addAll(new ArrayList<News>());
    }

    public void loadPosts(Integer paged){
        if (isConnect())
            WordpressApiAdapter.getApiService(ApiConstants.BASE_URL).getListByCategoryName(categoryName, paged, this);
        else
            Toast.makeText(getContext(), "Verifique su conexión a red", Toast.LENGTH_SHORT).show();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setHome(Boolean home) {
        isHome = home;
    }

    private boolean isConnect() {

        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}

