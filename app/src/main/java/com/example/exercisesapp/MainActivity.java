package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnGetExMuscle, btnGetExByName, btnGetExByMuscle;
    EditText etDataInput;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign values for views
        btnGetExMuscle = findViewById(R.id.btnGetExMuscle);
        btnGetExByName = findViewById(R.id.btnGetExByName);
        btnGetExByMuscle = findViewById(R.id.btnGetExByMuscle);
        recyclerView = findViewById(R.id.rvSearchList);
        etDataInput = findViewById(R.id.etDataInput);

        ExerciseDataService exerciseDataService = new ExerciseDataService(MainActivity.this);

        // btn listeners
        btnGetExMuscle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exerciseDataService.getExInfoByName(etDataInput.getText().toString(), new ExerciseDataService.ExInfoByNameResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ArrayList<ExInfoModel> exInfoModels) {

                        // put the items in the list view
                        rvResultsAdapter adapter = new rvResultsAdapter(MainActivity.this, exInfoModels);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    }
                });

            }
        });

        btnGetExByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You typed: " + etDataInput.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnGetExByMuscle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You clicked me 3", Toast.LENGTH_SHORT).show();
            }
        });
    }

}