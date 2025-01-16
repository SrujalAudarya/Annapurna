package com.example.internshipproject;

public class POJOGetAllCategoryDetails {
    //POJO = Plain Old Java Object
    // reuseability
    // pojo multiple data get and set

    String id, categoryImage, categoryName;

    public POJOGetAllCategoryDetails(String sid, String scategoryImage, String scategoryName) {
        this.id = sid;
        this.categoryImage = scategoryImage;
        this.categoryName = scategoryName;
    }

    public POJOGetAllCategoryDetails(String id, String categoryName, String dishCategory, String dishImage, String dishName, String dishPrice, String dishRating, String dishOffer, String dishDescription) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}