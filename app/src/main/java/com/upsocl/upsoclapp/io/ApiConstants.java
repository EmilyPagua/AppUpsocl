package com.upsocl.upsoclapp.io;

/**
 * Created by ${emily.pagua} on ${11-09-16}.
 */
public class ApiConstants {

    public static final String BASE_URL = "http://162.242.198.179/wp-json/wp/v2";
    public static final String LIST_POSTS = "/posts";
    public static final String PAGE = "page";
    public static final String FILTER_WORD = "filter[s]";
   // public static final String FILTER_CATEGORY_NAME = "filter[category_name]";
    public static final String FILTER_CATEGORY_NAME = "categories";

    public static final String FILTER_POST = "/posts/{id}";
    public static final String FILTER_PAGES = "/pages/{id}";

    //public static final String BASE_URL_CUSTOMER = "http://quiz.upsocl.com/dev/wp-json/wp/v2";
    public static final String BASE_URL_CUSTOMER = "http://104.130.164.44/dev/wp-json/wp/v2";
    public static final String CUSTOMER = "/customers";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_LAST_NAME = "last_name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_BIRTHDAY = "birthday";
    public static final String CUSTOMER_LOCATION = "location";
    public static final String CUSTOMER_SOCIAL_NETWORK = "social_network_login";
    public static final String CUSTOMER_REGISTRATION = "registration_id";
    public static final String CUSTOMER_ID = "id";


}