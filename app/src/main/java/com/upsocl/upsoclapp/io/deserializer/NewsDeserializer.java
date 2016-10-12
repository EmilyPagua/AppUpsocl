package com.upsocl.upsoclapp.io.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.upsocl.upsoclapp.domain.News;
import com.upsocl.upsoclapp.keys.JsonKeys;


import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leninluque on 13-11-15.
 */
public class NewsDeserializer implements JsonDeserializer<News> {

    @Override
    public News deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject news = json.getAsJsonObject();

        return extractNewsFromJsonArray(news);
    }

    private News extractNewsFromJsonArray(JsonObject item) {

        News currentNews = new News();
        currentNews.setId(item.get(JsonKeys.NEWS_ID).getAsString());

        if (item.get(JsonKeys.NEWS_ID).getAsString().equals("445196") ) {
            currentNews.setContent(getJsonValue(item.get(JsonKeys.NEWS_CONTENT)));
            return currentNews;
        }

        if (item.get(JsonKeys.NEWS_ID).getAsString().equals("1039") ) {
            currentNews.setContent(getJsonValue(item.get(JsonKeys.NEWS_CONTENT)));
            return currentNews;
        }

        currentNews.setTitle(getJsonValue(item.get(JsonKeys.NEWS_TITLE))
                .replace("&#8220;", "'").replace("&#8221;", "'")
                .replace("&#8216;", "'").replace("&#8217;", "'").replace("&#8230;", "'"));
        currentNews.setContent(getJsonValue(item.get(JsonKeys.NEWS_CONTENT)));

        String urlImage = item.get(JsonKeys.NEWS_IMAGES_URL).toString();
        currentNews.setImage(urlImage.substring(1, urlImage.length() - 1));
        currentNews.setDate(cenvertToDate(item.get(JsonKeys.NEWS_DATE).toString()));
        currentNews.setAuthor((item.get(JsonKeys.NEWS_AUTHOR_FIRST_NAME) + " " + item.get(JsonKeys.NEWS_AUTHOR_LAST_NAME)).replace("\"", ""));
        currentNews.setLink(item.get(JsonKeys.NEWS_LINK).getAsString());
        String categorias = item.get(JsonKeys.NEWS_CATEGORIES).getAsString();
        currentNews.setCategories(categorias.substring(0, categorias.length() - 1).replace(",IA", "").replace("IA,", ""));

        return currentNews;
    }

    private String getJsonValue(JsonElement jsonElement) {
        String valor =  ((JsonObject) jsonElement).get(JsonKeys.RENDERED).toString();
        return  valor.substring(1, valor.length()-1);
    }

    private String cenvertToDate(String dateInString ){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");

        Date date =  null;
        try {

            date = formatter.parse(dateInString.substring(1, 11 ));
            return formatter2.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
