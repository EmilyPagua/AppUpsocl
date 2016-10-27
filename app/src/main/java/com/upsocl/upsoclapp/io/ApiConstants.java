package com.upsocl.upsoclapp.io;

/**
 * Created by ${emily.pagua} on ${11-09-16}.
 */
public class ApiConstants {

    public static final String BASE_URL = "http://upsocl.com/wp-json/wp/v2";
    public static final String LIST_POSTS = "/posts";
    public static final String PAGE = "filter[paged]";
    public static final String FILTER_WORD = "filter[s]";
    public static final String FILTER_CATEGORY_NAME = "filter[category_name]";

    public static final String FILTER_POST = "/posts/{id}";
    public static final String FILTER_PAGES = "/pages/{id}";

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

}

//http://www.upsocl.com/wp-json/oembed/1.0/embed?url=http%3A%2F%2Fwww.upsocl.com%2Fprivacidad-app%2F&#038;format=xml
//http://upsocl.com/wp-json/wp/v2/posts?filter[category_name]=privacidad-app/filter[category_name]=verde
//http://upsocl.com/wp-json/wp/v2/posts/442250

//Sender ID: 149087864796
//Server API key AIzaSyDqPOHOJq0dwmEewLpTuOqP0Y3U95-QB9g
//filter[category_name]=creatividad&filter[category_name]=verde

//http://quiz.upsocl.com/dev/wp-json/wp/v2/customers?name=emily&last_name=pagua&email=emily@gmail.com&birthday=1990-02-01&location=caracas&social_network_login=dsf&registration_id=3

//SHA1: F2:A5:02:B0:46:92:AA:42:F0:F4:01:97:A9:FE:B1:96:2A:FA:2C:D3
//      F2:A5:02:B0:46:92:AA:42:F0:F4:01:97:A9:FE:B1:96:2A:FA:2C:D3

//keytool -exportcert -alias upsocl -keystore /Users/upsocl/AndroidStudioProjects/debug.keystore | openssl sha1 -binary | openssl base64


/*8qUCsEaSqkLw9AGXqf6xlir6LNM=


       //2jmj7l5rSw0yVb/vlWAYkK/YBwk=
f5CtnhpBy+NovSO2fz6MvXV3aSk (funciona en ambos)


qbVxOyyMZIx6QIq4XGpeX0WxgbE= debug

D7LVXU0B0BQ9ltnLkNPykti4Jus=   release




        keytool -exportcert -alias **myaliasname** -keystore **home/comp-1/Desktop/mykeystore.jks** | openssl sha1 -binary | openssl base64
        keytool -exportcert -alias upsocl -keystore /Users/upsocl/AndroidStudioProjects/upsocl.jks | openssl sha1 -binary | openssl base64
        */

//xqK5q3iPRu1qt8xbF1LIbEovu7A=

//keytool -exportcert -keystore /Users/upsocl/AndroidStudioProjects/upsocl.jks -list -v