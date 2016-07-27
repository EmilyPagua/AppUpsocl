package com.upsocl.appupsocl.io;


import com.upsocl.appupsocl.domain.Customer;
import com.upsocl.appupsocl.domain.News;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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

    @GET(ApiConstants.LIST_POSTS)
    void getListNotifications(@Query(ApiConstants.FILTER_APPNOTIFICATION) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.FILTER_POST)
    void getPost(@Path("id") String page, Callback<News> response);




    @POST(ApiConstants.CUSTOMER)
    void saveCustomer(@Query(ApiConstants.CUSTOMER_NAME) String name,
                      @Query(ApiConstants.CUSTOMER_LAST_NAME) String lastName,
                      @Query(ApiConstants.CUSTOMER_EMAIL) String email,
                      @Query(ApiConstants.CUSTOMER_BIRTHDAY) String birthday,
                      @Query(ApiConstants.CUSTOMER_LOCATION) String location,
                      @Query(ApiConstants.CUSTOMER_SOCIAL_NETWORK) String socialNetwork,
                      @Query(ApiConstants.CUSTOMER_REGISTRATION) String registrationID,
                      Callback<Integer> response );

    @GET(ApiConstants.CUSTOMER)
    void getCustomerById(@Query(ApiConstants.CUSTOMER_ID) Integer idUser, Callback<Customer> response );

}
