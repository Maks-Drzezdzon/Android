package com.example.worldnewsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.worldnewsapp.Adapters.SourcesAdapter;
import com.example.worldnewsapp.Models.Source;
import com.example.worldnewsapp.Models.WebSite;
import com.example.worldnewsapp.Retrofit.ApiClient;
import com.example.worldnewsapp.Retrofit.IApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewMainActivity)
    RecyclerView recyclerView;
//    @BindView(R.id.swipeRefresh)
//    SwipeRefreshLayout swipeRefreshLayout;

    IApi apiInterface;
    List<Source> listSources;
    SourcesAdapter adapter;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // newsapi.org
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind views above to app with ButterKnife
        ButterKnife.bind(this);
        init();

        loadWebSiteSources();
    }

    private void loadWebSiteSources() {
        dialog.show();
        apiInterface.getSources().enqueue(new Callback<WebSite>() {
            @Override
            public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                dialog.dismiss();
                WebSite webSite = response.body();
                if(webSite != null && response.body().getSources().size() > 0){
                    listSources.clear();
                    listSources.addAll(webSite.getSources());
                }else {
                    Toast.makeText(MainActivity.this,"no sources found", Toast.LENGTH_LONG).show();
                }
                // loads adapter data again automatically
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<WebSite> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });
    }


    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Loading").setCancelable(false).build();
        apiInterface = ApiClient.getApiClient().create(IApi.class);
        listSources = new ArrayList<>();
        adapter = new SourcesAdapter(this, listSources);
        recyclerView.setAdapter(adapter);
    }
}
