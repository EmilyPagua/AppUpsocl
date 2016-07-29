package com.upsocl.appupsocl.io;

/**
 * Created by leninluque on 11-11-15.
 */
public class ApiConstants {

    public static final String BASE_URL = "http://upsocl.com/wp-json/wp/v2";
    public static final String LIST_POSTS = "/posts";
    public static final String PAGE = "filter[paged]";
    public static final String FILTER_GREEN = "filter[category_name]=verde&filter[paged]";
    public static final String FILTER_FOOD = "filter[category_name]=comida&filter[paged]";
    public static final String FILTER_CREATIVITY = "filter[category_name]=creatividad&filter[paged]";
    public static final String FILTER_LASTNEWS = "filter[category_name]";
    public static final String FILTER_WOMEN = "filter[category_name]=mujer&filter[paged]";
    public static final String FILTER_APPNOTIFICATION = "filter[category_name]=creatividad&filter[order]=DESC&filter[paged]";
    public static final String FILTER_POPULARY = "filter[category_name]=mas-populares&filter[paged]";
    public static final String FILTER_WORD = "filter[s]";
    public static final String FILTER_CATEGORY_NAME = "filter[category_name]";

    public static final String FILTER_POST = "/posts/{id}";


    public static final String BASE_URL_CUSTOMER = "http://quiz.upsocl.com/dev/wp-json/wp/v2";
    public static final String CUSTOMER = "/customers";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_LAST_NAME = "last_name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_BIRTHDAY = "birthday";
    public static final String CUSTOMER_LOCATION = "location";
    public static final String CUSTOMER_SOCIAL_NETWORK = "social_network_login";
    public static final String CUSTOMER_REGISTRATION = "registration_id";
    public static final String CUSTOMER_ID = "id";


    //http://upsocl.com/wp-json/wp/v2/posts?filter[category_name]=creatividad/filter[category_name]=verde
    //http://upsocl.com/wp-json/wp/v2/posts/16558

   // "categories": [723, 52], [723, 52],

    //Sender ID: 149087864796
    //Server API key AIzaSyDqPOHOJq0dwmEewLpTuOqP0Y3U95-QB9g
    //filter[category_name]=creatividad&filter[category_name]=verde
    //http://quiz.upsocl.com/dev/wp-json/wp/v2/customers?name=emily&last_name=pagua&email=emily@gmail.com&birthday=1990-02-01&location=caracas&social_network_login=dsf&registration_id=3

    //http://quiz.upsocl.com/dev/wp-json/wp/v2/customers?name=Mily&last_name=pagua&email=emily@gmail.com&birthday=1990-02-01&location=caracas&social_network_login=dsf&registration_id=3/?name
    //http://quiz.upsocl.com/dev/wp-json/wp/v2/customers?name=Mily&last_name=Pagua&email=emilyfpg%40gmail.com&birthday=1990-06-07&location=Caracas%2C+Venezuela&registration_id=asdfasfdasfgvbn436345673657-456745784578-
    //http://wpruebas.dev/wp-json/posts?filter[cat]=5
}

