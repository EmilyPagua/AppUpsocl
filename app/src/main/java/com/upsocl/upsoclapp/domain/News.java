package com.upsocl.upsoclapp.domain;

import com.google.gson.annotations.SerializedName;


/**
 * Created by leninluque on 09-11-15.
 */
public class News {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("image")
    private String image;
    @SerializedName("author")
    private String author;
    @SerializedName("date")
    private String date;
    @SerializedName("link")
    private String link;
    @SerializedName("categoriesName")
    private String categories;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public News() {
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
