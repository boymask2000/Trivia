package com.boymask.triviaforall.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
   // /country/italy/status/confirmed?from=2020-03-01T00:00:00Z&to=2020-04-01T00:00:00Z

    @GET("/api/v2/video/search/?query=teen&per_page=1&page=2&thumbsize=big&order=top-weekly&gay=1&lq=1&format=json")
    Call<List<RetroFoto>> getAllPhotos();

    @GET("/api_token.php?command=request")
    Call<SessionToken> getSessionToken();

    @GET("/api_category.php")
    Call<CategoryList> getArguments();

    //difficulty=easy
    // https://opentdb.com/api.php?amount=10&category=23
    @GET("/api.php?encode=base64")
    Call<Questions> getQuestions(@Query("amount") String amount, @Query("category") int category, @Query("difficulty") String difficulty ,@Query("token") String token);

/*    @GET("/country/italy/status/confirmed/live?from=2020-03-01T00:00:00Z")
    Call<List<CovidData>> getConfirmed(@Query("to") String maxData);

    @GET("/country/italy/status/recovered/live?from=2020-03-01T00:00:00Z")
    Call<List<CovidData>> getRecovered(@Query("to") String maxData);

    @GET("/country/italy/status/deaths/live?from=2020-03-01T00:00:00Z")
    Call<List<CovidData>> getDeaths(@Query("to") String maxData);*/
}
