package com.boymask.triviaforall.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroFoto {


    public void setEmbed(String embed) {
        this.embed = embed;
    }

    @SerializedName("ll")
    private List<?> ll;

    @SerializedName("embed")
    private String embed;
    @SerializedName("title")
    private String title;
    @SerializedName("videos")
    private List<?> videos;

    public int getTotal_count() {
        return total_count;
    }

    @SerializedName("total_count")
    private int total_count;


    public RetroFoto(String embed, String title, int total_count, List<?> videos) {
        this.embed = embed;
        this.title = title;
        this.total_count = total_count;
        this.videos = videos;
    }

    public String getEmbed() {
        return embed;
    }

    public List<?> getVideos() {
        return videos;
    }
}