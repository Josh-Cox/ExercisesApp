package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnGetExMuscle, btnGetExByName, btnGetExByMuscle;
    EditText etDataInput;
    ListView lvExList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign values for views
        btnGetExMuscle = findViewById(R.id.btnGetExMuscle);
        btnGetExByName = findViewById(R.id.btnGetExByName);
        btnGetExByMuscle = findViewById(R.id.btnGetExByMuscle);

        etDataInput = findViewById(R.id.etDataInput);
        lvExList = findViewById(R.id.lvExList);

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
                    public void onResponse(List<ExInfoModel> exInfoModelList) {

                        // put the items in the list view
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, exInfoModelList);
                        lvExList.setAdapter(arrayAdapter);
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