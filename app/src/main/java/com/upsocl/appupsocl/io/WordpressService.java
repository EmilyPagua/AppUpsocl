package com.upsocl.appupsocl.io;


import com.upsocl.appupsocl.domain.News;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leninluque on 11-11-15.
 */
public interface WordpressService {

    @GET(ApiConstants.LIST_POSTS)
    void getListNews(@Query(ApiConstants.PAGE) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListGreen(@Query(ApiConstants.FILTER_GREEN) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListFood(@Query(ApiConstants.FILTER_FOOD) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListCreativity(@Query(ApiConstants.FILTER_CREATIVITY) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListWomen(@Query(ApiConstants.FILTER_WOMEN) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListPopulary(@Query(ApiConstants.FILTER_POPULARY) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListWord(@Query(ApiConstants.FILTER_WORD) String word, @Query(ApiConstants.PAGE) int page, Callback<ArrayList<News>> response);


}
