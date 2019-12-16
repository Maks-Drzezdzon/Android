package com.example.worldnewsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.worldnewsapp.Adapters.StackAdapter;
import com.example.worldnewsapp.Models.Article;
import com.example.worldnewsapp.Models.News;
import com.example.worldnewsapp.R;
import com.example.worldnewsapp.Retrofit.ApiClient;
import com.example.worldnewsapp.Retrofit.IApi;
import com.example.worldnewsapp.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.fls.swipestack.SwipeStack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListStackActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener {

    @BindView(R.id.swipeSptack)
    SwipeStack swipeStack;
    @BindView(R.id.relativeLayoutNoFound)
    RelativeLayout relativeLayoutNoFound;
    IApi apiInterface;
    List<Article> arrayListArticles;
    String sourceId;
    String webHotUrl;

    StackAdapter stackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stack);
        ButterKnife.bind(this);
        init();

        getDataFromPrevious();

    }

    private void getDataFromPrevious() {
        sourceId = getIntent().getStringExtra("sourceId");

        if(!sourceId.isEmpty()){
            loadNews(sourceId);
        }
    }

    private void loadNews(String sourceId) {
        apiInterface.getNewestArticles(ApiClient.getApiUrl(sourceId, AppConstants.API_KEY)).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                arrayListArticles.clear();
                webHotUrl = response.body().getArticles().get(0).getUrl();
                arrayListArticles.addAll(response.body().getArticles());

                stackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(ListStackActivity.this,"Error:"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        apiInterface = ApiClient.getApiClient().create(IApi.class);
        arrayListArticles = new ArrayList<>();
        stackAdapter = new StackAdapter(this,arrayListArticles);
        swipeStack.setAdapter(stackAdapter);
        swipeStack.setListener(this);
    }

    @Override
    public void onViewSwipedToLeft(int position) {

    }

    @Override
    public void onViewSwipedToRight(int position) {

    }

    @Override
    public void onStackEmpty() {
        swipeStack.setVisibility(View.GONE);
        relativeLayoutNoFound.setVisibility(View.VISIBLE);

    }
}
