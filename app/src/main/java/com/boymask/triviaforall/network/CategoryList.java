package com.boymask.triviaforall.network;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryList {

    @SerializedName("trivia_categories")
    private List<Category> categories;



    public List<Category> getCategories() {
        categories = categories.stream()
                .sorted(Comparator.comparing(Category::getName))
                .collect(Collectors.toList());
        return categories;
    }

}
