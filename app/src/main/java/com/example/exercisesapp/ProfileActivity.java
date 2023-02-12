package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements RVSearchInterface {

    NavigationBarView navigationBarView;
    RecyclerView recyclerView;
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<ExInfoModel>();
    Context context = ProfileActivity.this;
    TextView tvActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // top action bar
        tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.profile_page);

        // set attributes
        recyclerView = findViewById(R.id.rvSavedList);

        String filename = "saved";

        // get saved exercises from file
        savedExInfoModels = MainActivity.getSavedEx(ProfileActivity.this);

        // put the items in the list view
        RVSearchAdapter adapter = new RVSearchAdapter(ProfileActivity.this, savedExInfoModels, ProfileActivity.this, "profile");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));




        // bottom nav bar
        navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.profile);

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
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


}