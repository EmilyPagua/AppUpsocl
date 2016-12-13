package com.upsocl.upsoclapp.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ${user} on ${date}.
 */

public class Category {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("isCheck")
        private Boolean isCheck;
        @SerializedName("nameCategory")
        private int image;


        public Category(int id, String name, int image, boolean isCheck) {
            this.id = id;
            this.name = name;
            this.image =  image;
            this.isCheck = isCheck;
        }

        public Category() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getCheck() {
            return isCheck;
        }

        public void setCheck(Boolean check) {
            isCheck = check;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }



}
