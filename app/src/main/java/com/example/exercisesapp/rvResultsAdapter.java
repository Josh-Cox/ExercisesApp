package com.example.exercisesapp;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class rvResultsAdapter extends RecyclerView.Adapter<rvResultsAdapter.MyViewHolder> {
    Context context;
    ArrayList<ExInfoModel> exInfoModels;


    public rvResultsAdapter(Context context, ArrayList<ExInfoModel> exInfoModels) {
        this.context = context;
        this.exInfoModels = exInfoModels;
    }

    // inflate layout
    @NonNull
    @Override
    public rvResultsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_search_row, parent, false);

        return new rvResultsAdapter.MyViewHolder(view);
    }

    // bind values to each item
    @Override
    public void onBindViewHolder(@NonNull rvResultsAdapter.MyViewHolder holder, int position) {
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvExName);
            tvMuscle = itemView.findViewById(R.id.tvExMuscle);
            tvDiff = itemView.findViewById(R.id.tvExDiff);

        }
    }
}
