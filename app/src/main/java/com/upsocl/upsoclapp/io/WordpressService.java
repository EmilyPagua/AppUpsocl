package com.upsocl.upsoclapp.io;


import com.google.gson.JsonObject;
import com.upsocl.upsoclapp.domain.Customer;
import com.upsocl.upsoclapp.domain.News;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by emily.pagua on 11-11-15.
 */

public interface WordpressService {

    @GET(ApiConstants.LIST_POSTS)
    void getListWord(@Query(ApiConstants.FILTER_WORD) String word, @Query(ApiConstants.PAGE) int page, Callback<ArrayList<News>> response);

    @GET(ApiConstants.FILTER_POST)
    void getPost(@Path("id") String page, Callback<News> response);

    @GET(ApiConstants.FILTER_PAGES)
    void getTerminos(@Path("id")String page, Callback<News> response);

    @GET(ApiConstants.LIST_POSTS)
    void getListByCategoryName(@Query(ApiConstants.FILTER_CATEGORY_NAME) String categoryName, @Query(ApiConstants.PAGE) int page, Callback<ArrayList<News>> response);

    @POST(ApiConstants.CUSTOMER)
    void saveCustomer(@Query(ApiConstants.CUSTOMER_NAME) String name,
                      @Query(ApiConstants.CUSTOMER_LAST_NAME) String lastName,
                      @Query(ApiConstants.CUSTOMER_EMAIL) String email,
                      @Query(ApiConstants.CUSTOMER_BIRTHDAY) String birthday,
                      @Query(ApiConstants.CUSTOMER_LOCATION) String location,
                      @Query(ApiConstants.CUSTOMER_SOCIAL_NETWORK) String socialNetwork,
                      @Query(ApiConstants.CUSTOMER_REGISTRATION) String registrationID,
                      Callback<JsonObject> response );

    @GET(ApiConstants.CUSTOMER)
    void getCustomerById(@Query(ApiConstants.CUSTOMER_ID) Integer idUser, Callback<Customer> response );

    //http://upsocl.com/wp-json/wp/v2/pages/445196
}
