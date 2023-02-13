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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVInterface {

    RVAdapter adapter;
    RecyclerView recyclerView;
    private final String STATE_ITEMS = "items";

    /**
     * on activity creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign values for views
        Button btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.rvSearchList);
        EditText etDataInput = findViewById(R.id.etDataInput);

        // define instance of service for communication with API
        ExerciseDataService exerciseDataService = new ExerciseDataService(MainActivity.this);

        // top action bar
        TextView tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.home_page);

        // bottom nav bar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.home);

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Switches the activity to the selected icon
             * @param item The selected item
             * @return true if a switch case is taken
             */
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

        // btn listeners
        btnSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick listener for the search button
             * @param view
             */
            @Override
            public void onClick(View view) {

                exerciseDataService.getExInfoByName(etDataInput.getText().toString(), new ExerciseDataService.ExInfoByNameResponse() {
                    /**
                     * catches and returns error with API service communication
                     * @param message
                     */
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * creates adapter for recycleView  on API service response
                     * @param exInfoModels
                     */
                    @Override
                    public void onResponse(ArrayList<ExInfoModel> exInfoModels) {

                        // put the items in the list view
                        adapter = new RVAdapter(MainActivity.this, exInfoModels, MainActivity.this, "main");
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    }
                });

            }
        });

    }

    /**
     * on item click in recyclerView
     * @param position
     * @param exInfoModels
     * @param clickedFrom
     */
    @Override
    public void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom) {
        // create intent to move to exercise details activity
        Intent intent = new Intent(this, ExerciseInfoActivity.class);

        // create bundle and add the exercise pressed, and activity user was previously on
        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise", Parcels.wrap(exInfoModels.get(position)));
        bundle.putParcelable("clickedFrom", Parcels.wrap(clickedFrom));
        intent.putExtras(bundle);

        // start intent to move to exercise details activity
        startActivity(intent);
    }

    /**
     * save the state of the instance of the activity (recyclerView)
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(adapter != null) {
            outState.putSerializable(STATE_ITEMS, adapter.getItems());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * restore the state of the instance of the activity (recyclerView)
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // put the items in the list view
        adapter = new RVAdapter(MainActivity.this, (ArrayList<ExInfoModel>) savedInstanceState.getSerializable(STATE_ITEMS), MainActivity.this, "main");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
}