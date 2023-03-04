package com.example.exercisesapp;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ExViewModel extends AndroidViewModel {

    // -------------------- attributes -------------------- //
    private final MutableLiveData<ArrayList<ExInfoModel>> exerciseArray = new MutableLiveData<>();

    /**
     * constructor
     * @param application application context
     */
    public ExViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * get exercises
     * @return list of exercise objects
     */
    public LiveData<ArrayList<ExInfoModel>> getEx() {
        return this.exerciseArray;
    }

    /**
     * Send API request
     * @param exerciseDataService data service to send request
     * @param etDataInput exercise name input from user
     * @param selected_filters user selected filter options
     */
    public void searchAPI(ExerciseDataService exerciseDataService, EditText etDataInput, String[] selected_filters) {

        exerciseDataService.getExInfoByName(etDataInput.getText().toString(), selected_filters, new ExerciseDataService.ExInfoByNameResponse() {
            /**
             * catches and returns error with API service communication
             * @param message error message
             */
            @Override
            public void onError(String message) {
                System.out.print("ERROR:" + message);
            }

            /**
             * creates adapter for recycleView  on API service response
             * @param exInfoModels list of exercises
             */
            @Override
            public void onResponse(ArrayList<ExInfoModel> exInfoModels) {
                exerciseArray.setValue(exInfoModels);
            }
        });

    }
}
