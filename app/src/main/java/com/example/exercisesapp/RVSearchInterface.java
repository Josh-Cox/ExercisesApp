package com.example.exercisesapp;

import android.content.Context;

import java.util.ArrayList;

public interface RVSearchInterface {
    void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom);
}
