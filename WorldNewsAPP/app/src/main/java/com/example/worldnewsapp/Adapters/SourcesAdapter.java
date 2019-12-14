package com.example.worldnewsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worldnewsapp.Models.Source;
import com.example.worldnewsapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder> {

    private Context context;
    private List<Source> sourceLists;

    public SourcesAdapter(Context context, List<Source> sourceList){
        this.context = context;
        this.sourceLists = sourceList;
    }

    @NonNull
    @Override
    public SourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_source, parent, false);
        return new SourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourcesViewHolder holder, int position) {
        holder.textView.setText(sourceLists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return sourceLists.size();
    }

    public  class SourcesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.source_image)
        CircleImageView circleImageView;

        @BindView(R.id.source_name)
        TextView textView;
        public SourcesViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
