package com.example.exercisesapp;

import java.util.ArrayList;


public interface RVInterface {
    /**
     * call item click method in focused activity
     * @param position position of view clicked
     * @param exInfoModels list of exercise objects
     * @param clickedFrom activity user was previously on
     */
    void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom);
}
