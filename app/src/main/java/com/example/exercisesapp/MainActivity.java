package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVInterface {

    RVAdapter adapter;
    RecyclerView recyclerView;
    private final String STATE_ITEMS = "items";
    private final String STATE_SAVED = "state";

    // options for filters
    String[] filter_difficulty = {"Any", "Beginner","Intermediate" ,"Expert"};
    String[] filter_muscle = {"Any", "Abdominals", "Abductors", "Adductors", "Biceps", "Calves", "Chest", "Forearms", "Glutes", "Hamstrings",
    "Lats", "Lower Back", "Middle Back", "Neck", "Quadriceps", "Traps", "Triceps"};
    String[] filter_type = {"Any", "Cardio", "Olympic Weight Lifting", "Plyometrics", "Powerlifting", "Strength", "Stretching", "Strongman"};

    // store filter selection
    String[] selected_filters = {"", "", ""};

    AutoCompleteTextView autoCompleteDiff;
    AutoCompleteTextView autoCompleteMuscle;
    AutoCompleteTextView autoCompleteType;

    ArrayAdapter<String> adapterDiff;
    ArrayAdapter<String> adapterMuscle;
    ArrayAdapter<String> adapterType;


    /**
     * on activity creation
     * @param savedInstanceState current instance of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // assign values for views
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnFilters = findViewById(R.id.applyFilters);
        recyclerView = findViewById(R.id.rvSearchList);
        EditText etDataInput = findViewById(R.id.etDataInput);

        // define instance of service for communication with API
        ExerciseDataService exerciseDataService = new ExerciseDataService(MainActivity.this);

        // filter menu
        autoCompleteDiff = findViewById(R.id.autoCompleteDiff);
        autoCompleteMuscle = findViewById(R.id.autoCompleteMuscle);
        autoCompleteType = findViewById(R.id.autoCompleteType);

        adapterDiff = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_difficulty);
        adapterMuscle = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_muscle);
        adapterType = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_type);

        // top action bar
        ImageView filterIcon = findViewById(R.id.menuOrAddIcon);
        filterIcon.setImageResource(R.drawable.ic_filter);
        TextView tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.home_page);

        // side menu
        DrawerLayout drawerLayout = findViewById(R.id.filter_drawer_layout);
        NavigationView navigationView = findViewById(R.id.filter_view);
        View header = navigationView.getHeaderView(0);
        ImageView backFromMenuIcon = header.findViewById(R.id.backFromMenu);
        TextView sideMenuText = header.findViewById(R.id.pageTitle);
        sideMenuText.setText(R.string.main_side_menu);

        // bottom nav bar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.home);

        //filter menu listener
        autoCompleteDiff.setAdapter(adapterDiff);
        autoCompleteMuscle.setAdapter(adapterMuscle);
        autoCompleteType.setAdapter(adapterType);

        // listeners for filters
        autoCompleteDiff.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            if(!item.equals("Any")) {
                selected_filters[0] = item.toLowerCase();
            }
            else {
                selected_filters[0] = "";
            }

        });

        autoCompleteMuscle.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            if(!item.equals("Any")) {
                selected_filters[1] = item.toLowerCase();
            }
            else {
                selected_filters[1] = "";
            }
        });

        autoCompleteType.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            if(!item.equals("Any")) {
                selected_filters[2] = item.toLowerCase();
            }
            else {
                selected_filters[2] = "";
            }
        });

        // top action bar listener
        filterIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));


        backFromMenuIcon.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.END));

        // bottom nav bar listeners
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
             * @param view button view to set the on click listener to
             */
            @Override
            public void onClick(View view) {
                searchAPI(exerciseDataService, etDataInput);
            }
        });

        btnFilters.setOnClickListener(view -> {
            searchAPI(exerciseDataService, etDataInput);
            drawerLayout.closeDrawer(GravityCompat.END);
        });

    }

    /**
     * on item click in recyclerView
     * @param position position of item clicked in the array
     * @param exInfoModels list of exercises
     * @param clickedFrom activity that the user was on
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
     * @param outState bundle of saved state
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        // check if adapter is empty
        if(adapter != null) {
            // send items
            outState.putSerializable(STATE_ITEMS, adapter.getItems());

            // send boolean to say that items were saved
            outState.putBoolean(STATE_SAVED, true);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * restore the state of the instance of the activity (recyclerView)
     * @param savedInstanceState current state of activity
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // check if items were in adapter
        if(savedInstanceState.getBoolean(STATE_SAVED)) {

            // put the items in the recycler view
            adapter = new RVAdapter(MainActivity.this, (ArrayList<ExInfoModel>) savedInstanceState.getSerializable(STATE_ITEMS), MainActivity.this, "main");
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }
    }

    public void searchAPI(ExerciseDataService exerciseDataService, EditText etDataInput) {

        exerciseDataService.getExInfoByName(etDataInput.getText().toString(), selected_filters, new ExerciseDataService.ExInfoByNameResponse() {
            /**
             * catches and returns error with API service communication
             * @param message error message
             */
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            /**
             * creates adapter for recycleView  on API service response
             * @param exInfoModels list of exercises
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


}