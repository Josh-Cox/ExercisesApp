package com.example.exercisesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.MyViewHolder> {
    private final RVSearchInterface rvSearchInterface;

    Context context;
    ArrayList<ExInfoModel> exInfoModels;
    String clickedFrom;


    public RVSearchAdapter(Context context, ArrayList<ExInfoModel> exInfoModels, RVSearchInterface rvSearchInterface, String clickedFrom) {
        this.context = context;
        this.exInfoModels = exInfoModels;
        this.rvSearchInterface = rvSearchInterface;
        this.clickedFrom = clickedFrom;
    }

    // inflate layout
    @NonNull
    @Override
    public RVSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_search_row, parent, false);

        return new RVSearchAdapter.MyViewHolder(view, rvSearchInterface, exInfoModels, clickedFrom);
    }

    // bind values to each item
    @Override
    public void onBindViewHolder(@NonNull RVSearchAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(exInfoModels.get(position).getName());
        holder.tvMuscle.setText("Muscle: " + exInfoModels.get(position).getMuscle());
        holder.tvDiff.setText("Difficulty: " + exInfoModels.get(position).getDifficulty());
    }

    // get total number of items
    @Override
    public int getItemCount() {
        return exInfoModels.size();
    }

    // assign views to variables
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvMuscle;
        TextView tvDiff;

        public MyViewHolder(@NonNull View itemView, RVSearchInterface rvSearchInterface, ArrayList<ExInfoModel> exInfoModels, String clickedFrom) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvExName);
            tvMuscle = itemView.findViewById(R.id.tvExMuscle);
            tvDiff = itemView.findViewById(R.id.tvExDiff);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rvSearchInterface != null) {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION) {
                            rvSearchInterface.onItemClick(position, exInfoModels, clickedFrom);
                        }
                    }
                }
            });

        }
    }
}
