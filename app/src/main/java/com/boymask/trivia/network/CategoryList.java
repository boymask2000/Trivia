package com.boymask.trivia.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList {

    @SerializedName("trivia_categories")
    private List<Category> categories;



    public List<Category> getCategories() {
        return categories;
    }

}
