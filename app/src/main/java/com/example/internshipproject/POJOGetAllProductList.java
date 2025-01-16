package com.example.internshipproject;

public class POJOGetAllProductList {

    String id, categoryName, dishCategory, dishImage, dishName, dishPrice, dishRating, dishOffer, dishDescription;

    public POJOGetAllProductList(String id, String categoryName, String dishCategory, String dishImage, String dishName, String dishPrice, String dishRating, String dishOffer, String dishDescription) {
        this.id = id;
        this.categoryName = categoryName;
        this.dishCategory = dishCategory;
        this.dishImage = dishImage;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishRating = dishRating;
        this.dishOffer = dishOffer;
        this.dishDescription = dishDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDishCategory() {
        return dishCategory;
    }

    public void setDishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(String dishPrice) {
        this.dishPrice = dishPrice;
    }

    public String getDishRating() {
        return dishRating;
    }

    public void setDishRating(String dishRating) {
        this.dishRating = dishRating;
    }

    public String getDishOffer() {
        return dishOffer;
    }

    public void setDishOffer(String dishOffer) {
        this.dishOffer = dishOffer;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }
}
