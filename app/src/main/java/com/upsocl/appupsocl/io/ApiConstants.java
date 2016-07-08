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
    public static final String FILTER_WOMEN = "filter[category_name]=mujer&filter[paged]";
    public static final String FILTER_POPULARY = "filter[category_name]=mas-populares&filter[paged]";
    public static final String FILTER_WORD = "filter[s]";

    //http://upsocl.com/wp-json/wp/v2/posts?filter[category_name]=verde&filter[paged]=1
    //http://upsocl.com/wp-json/wp/v2/posts?filter[s]=Wordpress

}
