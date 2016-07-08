package com.upsocl.appupsocl.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by leninluque on 09-11-15.
 */
public class News {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    private String image;
    private String author;
    private String datePublicaded;

    public News() {
    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublicaded() {
        return datePublicaded;
    }

    public void setDatePublicaded(String datePublicaded) {
        this.datePublicaded = datePublicaded;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

}
