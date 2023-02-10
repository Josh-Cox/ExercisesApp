package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVSearchInterface {

    Button btnGetExMuscle, btnGetExByName, btnGetExByMuscle;
    EditText etDataInput;
    RecyclerView recyclerView;
    NavigationBarView navigationBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bottom nav bar
        navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.home);

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home:
                        return true;

                }
                return false;
            }
        });

        // assign values for views
        btnGetExMuscle = findViewById(R.id.btnGetExMuscle);
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