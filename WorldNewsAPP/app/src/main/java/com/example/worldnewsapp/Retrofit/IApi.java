package com.example.worldnewsapp.Retrofit;

import com.example.worldnewsapp.Models.News;
import com.example.worldnewsapp.Models.WebSite;
import com.example.worldnewsapp.Utils.AppConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IApi {
    @GET("v2/sources?language=en&apiKey=" + AppConstants.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);



}
