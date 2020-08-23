package com.boymask.triviaforall.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Questions implements Serializable {


    private String diffic;
    private Category category;

    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("results")
    private List<Map> results;

    public List<Map> getQuestions() {
        return results;
    }

    public int getResponseCode() {
        return responseCode;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public String getDiffic() {
        return diffic;
    }

    public void setDiffic(String diffic) {
        this.diffic = diffic;
    }

}

