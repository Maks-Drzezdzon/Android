package com.example.worldnewsapp.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements Serializable {
    public static final String url_template = "https://newsapi.org";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if(retrofit == null){
            Gson gson = new GsonBuilder().setLenient().create();
            // convert gson obj to retrofit
            retrofit = new Retrofit.Builder().baseUrl(url_template).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }

        return retrofit;
    }

    public static String getApiUrl(String source, String apiKey){
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source).append("&apiKey=").append(apiKey).toString();
    }

}
