package com.example.worldnewsapp.Retrofit;

import com.example.worldnewsapp.Models.WebSite;
import com.example.worldnewsapp.Utils.AppConstants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApi {
    @GET("v2/sources?language=en&apiKey=" + AppConstants.API_KEY)
    Call<WebSite> getSources();
}
