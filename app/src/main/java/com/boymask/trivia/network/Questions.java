package com.boymask.trivia.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Questions {

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

}

