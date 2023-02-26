package com.example.exercisesapp;

import android.app.Application;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ExViewModel extends AndroidViewModel {

    private ExerciseDataService exerciseDataService;
    private MutableLiveData<ArrayList<ExInfoModel>> exerciseArray = new MutableLiveData<>();


    public ExViewModel(@NonNull Application application) {
        super(application);

        exerciseDataService = new ExerciseDataService(application);
    }

    public LiveData<ArrayList<ExInfoModel>> getEx() {
        return this.exerciseArray;
    }

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
