package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVSearchInterface {

    Button btnGetExMuscle, btnGetExByName, btnGetExByMuscle;
    EditText etDataInput;
    RecyclerView recyclerView;
    NavigationBarView navigationBarView;
    TextView tvActionBar;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign values for views
        btnGetExMuscle = findViewById(R.id.btnGetExMuscle);
        recyclerView = findViewById(R.id.rvSearchList);
        etDataInput = findViewById(R.id.etDataInput);
        backIcon = findViewById(R.id.backIcon);

        // top action bar
        tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.home_page);


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
                        RVSearchAdapter adapter = new RVSearchAdapter(MainActivity.this, exInfoModels, MainActivity.this, "main");
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    }
                });

            }
        });

    }

    @Override
    public void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom) {
        Intent intent = new Intent(this, ExerciseInfoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise", Parcels.wrap(exInfoModels.get(position)));
        bundle.putParcelable("clickedFrom", Parcels.wrap(clickedFrom));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public static ArrayList<ExInfoModel> getSavedEx(Context context) {
        // create file
        String filename = "saved";
        File file = new File(context.getFilesDir(), filename);

        ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<ExInfoModel>();

        // get saved exercises from file
        try {
            file.createNewFile();
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedExInfoModels = (ArrayList<ExInfoModel>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return savedExInfoModels;
    }
}