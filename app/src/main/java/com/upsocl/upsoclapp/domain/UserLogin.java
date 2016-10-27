package com.upsocl.upsoclapp.domain;

/**
 * Created by emily.pagua on 26-07-16.
 */
public class UserLogin {

    private int id;
    private String email ;
    private String firstName;
    private String lastName ;
    private String birthday ;
    private String location ;
    private String token;
    private String imagenURL;
    private String socialNetwork;

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public UserLogin() {
    }

    public UserLogin(String email, String firstName, String lastName, String birthday,
                     String location, String token, int id , String imagenURL, String socialNetwork) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.location = location;
        this.id =  id;
        this.token = token;
        this.imagenURL = imagenURL;
        this.socialNetwork = socialNetwork;

    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
