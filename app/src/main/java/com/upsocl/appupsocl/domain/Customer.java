package com.upsocl.appupsocl.domain;

import com.google.gson.annotations.SerializedName;
import com.upsocl.appupsocl.keys.Preferences;

/**
 * Created by upsocl on 25-07-16.
 */
public class Customer {
    @SerializedName("id")
    private String id;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("location")
    private String location;
    @SerializedName("email")
    private String email;

    public Customer() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
