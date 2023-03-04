package com.example.exercisesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    // -------------------- attributes -------------------- //
    private final RVInterface rvInterface;
    Context context;
    ArrayList<ExInfoModel> exInfoModels;
    String clickedFrom;

    /**
     * constructor for recyclerView adapter
     * @param context activity context
     * @param exInfoModels list of exercise objects
     * @param rvInterface recycler view interface instance
     * @param clickedFrom activity user was previously on
     */
    public RVAdapter(Context context, ArrayList<ExInfoModel> exInfoModels, RVInterface rvInterface, String clickedFrom) {
        this.context = context;
        this.exInfoModels = exInfoModels;
        this.rvInterface = rvInterface;
        this.clickedFrom = clickedFrom;
    }

    /**
     * on creation of view holder
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return inflated view holder
     */
    @NonNull
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_search_row, parent, false);

        return new RVAdapter.MyViewHolder(view, rvInterface, exInfoModels, clickedFrom);
    }

    /**
     * on binding of view holder to values
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(exInfoModels.get(position).getName());
        holder.tvMuscle.setText(context.getString(R.string.rvRowMuscle, exInfoModels.get(position).getMuscle()));
        holder.tvDiff.setText(context.getString(R.string.rvRowDiff, exInfoModels.get(position).getDifficulty()));
    }

    /**
     * get size of exercise list
     * @return exInfoModels.size()
     */
    @Override
    public int getItemCount() {
        return exInfoModels.size();
    }

    // inner view holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // -------------------- define views -------------------- //

        TextView tvName;
        TextView tvMuscle;
        TextView tvDiff;

        /**
         * constructor for view holder
         * @param itemView view of recycler view row
         * @param rvInterface recycler view interface instance
         * @param exInfoModels list of exercise objects
         * @param clickedFrom activity user was previously on
         */
        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface, ArrayList<ExInfoModel> exInfoModels, String clickedFrom) {
            super(itemView);

            // assign values to views
            tvName = itemView.findViewById(R.id.tvExName);
            tvMuscle = itemView.findViewById(R.id.tvExMuscle);
            tvDiff = itemView.findViewById(R.id.tvExDiff);

            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * on item click, call item click method in recyclerView interface
                 * @param view view clicked
                 */
                @Override
                public void onClick(View view) {
                    if(rvInterface != null) {
                        int position = getAdapterPosition();

                        // if position exists in recyclerView
                        if(position != RecyclerView.NO_POSITION) {
                            rvInterface.onItemClick(position, exInfoModels, clickedFrom);
                        }
                    }
                }
            });
        }
    }
}
