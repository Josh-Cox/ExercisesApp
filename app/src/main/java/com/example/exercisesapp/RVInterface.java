package com.example.exercisesapp;

import java.util.ArrayList;


public interface RVInterface {
    /**
     * call item click method in focused activity
     * @param position
     * @param exInfoModels
     * @param clickedFrom
     */
    void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom);
}
