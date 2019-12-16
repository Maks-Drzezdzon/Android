package com.example.worldnewsapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.worldnewsapp.Models.Article;
import com.example.worldnewsapp.R;
import com.example.worldnewsapp.Utils.AppConstants;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class StackAdapter extends BaseAdapter {

    private Context context;
    private List<Article> arrayListArticles;


    public StackAdapter(Context context, List<Article> arrayListArticles) {
        this.context = context;
        this.arrayListArticles = arrayListArticles;
    }

    @Override
    public int getCount() {
        return arrayListArticles.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_layout, parent,false);
        }
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
        TextView textViewTitle = (TextView)convertView.findViewById(R.id.newsTitle);
        TextView textViewDescription =(TextView)convertView.findViewById(R.id.newsDescription);
        TextView textViewUrl =(TextView)convertView.findViewById(R.id.newsUrl);
        TextView textViewAuthor =(TextView)convertView.findViewById(R.id.websiteNameTxt);
        ImageView imageViewNews = convertView.findViewById(R.id.imageNews);
        RelativeTimeTextView textViewTime = (RelativeTimeTextView)convertView.findViewById(R.id.newsTime);
        ImageView imageViewShare = convertView.findViewById(R.id.shareNews);

        if(arrayListArticles.get(position).getUrlToImage() != null && arrayListArticles.get(position).getUrlToImage().length() >0){
            Picasso.get().load(arrayListArticles.get(position).getUrlToImage()).into(imageViewNews, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        textViewAuthor.setText(arrayListArticles.get(position).getAuthor());

        if(arrayListArticles.get(position).getTitle().length() > 65){
            textViewTitle.setText(arrayListArticles.get(position).getTitle().substring(0,65)+ "....");
        }else{
            textViewTitle.setText(arrayListArticles.get(position).getTitle());
        }

        if(arrayListArticles.get(position).getPublishedAt() != null){
            Date date =null;
            try{
                date = AppConstants.parse(arrayListArticles.get(position).getPublishedAt());
                textViewTime.setReferenceTime(date.getTime());
            }catch (Exception e){
                Log.d("StackAdapter"," "+e);
            }
        }

        textViewDescription.setText(arrayListArticles.get(position).getDescription());
        textViewUrl.setText(arrayListArticles.get(position).getUrl());

        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add social media share
                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}
