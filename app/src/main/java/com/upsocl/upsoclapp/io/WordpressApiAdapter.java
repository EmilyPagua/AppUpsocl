package com.upsocl.upsoclapp.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.io.deserializer.NewsDeserializer;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


/**
 * Created by emily.pagua on 11-11-15.
 */

public class WordpressApiAdapter {

    private static WordpressService API_SERVICE_CUSTOMER;
    private static WordpressService API_SERVICE;


    public static WordpressService getApiService(String url){

        try {
            if(API_SERVICE == null){
                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(url)
                        .setLogLevel(RestAdapter.LogLevel.BASIC)
                        .setConverter(builWordpressApiGsonConverter())
                        .build();

                API_SERVICE = adapter.create(WordpressService.class);
            }
        }catch (Exception e){
            return null;
        }
        return API_SERVICE;
    }

    private static GsonConverter builWordpressApiGsonConverter(){
        Gson gsonConf = new GsonBuilder()
                .registerTypeAdapter(News.class, new NewsDeserializer())
                .create();

        return new GsonConverter(gsonConf);
    }

    public static WordpressService getApiServiceCustomer(String url){

        if(API_SERVICE_CUSTOMER == null){

            RestAdapter adapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setEndpoint(url).build();

            API_SERVICE_CUSTOMER = adapter.create(WordpressService.class);
        }



        return API_SERVICE_CUSTOMER;
    }


}
