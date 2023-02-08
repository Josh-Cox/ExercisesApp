package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVSearchInterface {

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
                        RVSearchAdapter adapter = new RVSearchAdapter(MainActivity.this, exInfoModels, MainActivity.this);
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

    @Override
    public void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels) {
        Intent intent = new Intent(this, ExerciseInfoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise", Parcels.wrap(exInfoModels.get(position)));
        intent.putExtras(bundle);

        startActivity(intent);

    }
}