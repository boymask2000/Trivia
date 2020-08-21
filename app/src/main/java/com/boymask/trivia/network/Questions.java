package com.boymask.trivia.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Questions {
    @SerializedName("results")
    private List<Map> results;

    public List<Map> getQuestions() {
        return results;
    }
}

