package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RVInterface {

    // -------------------- define views -------------------- //

    // main content
    RVAdapter adapter;
    RecyclerView recyclerView;
    Button btnSearch;
    Button btnFilters;
    EditText etDataInput;

    // top action bar
    ImageView filterIcon;
    TextView tvActionBar;

    // bottom nav bar
    NavigationBarView navigationBarView;

    // filter menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View header;
    ImageView backFromMenuIcon;
    TextView sideMenuText;
    AutoCompleteTextView autoCompleteDiff;
    AutoCompleteTextView autoCompleteMuscle;
    AutoCompleteTextView autoCompleteType;
    ArrayAdapter<String> adapterDiff;
    ArrayAdapter<String> adapterMuscle;
    ArrayAdapter<String> adapterType;

    // -------------------- filter options -------------------- //

    String[] filter_difficulty = {"Any", "Beginner","Intermediate" ,"Expert"};
    String[] filter_muscle = {"Any", "Abdominals", "Abductors", "Adductors", "Biceps", "Calves", "Chest", "Forearms", "Glutes", "Hamstrings",
    "Lats", "Lower Back", "Middle Back", "Neck", "Quadriceps", "Traps", "Triceps"};
    String[] filter_type = {"Any", "Cardio", "Olympic Weight Lifting", "Plyometrics", "Powerlifting", "Strength", "Stretching", "Strongman"};

    String[] selected_filters = {"", "", ""};




    /**
     * on activity creation
     * @param savedInstanceState current instance of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -------------------- main setup -------------------- //

        // view model
        ExViewModel viewModel = new ViewModelProvider(MainActivity.this).get(ExViewModel.class);
        viewModel.getEx().observe(this, exerciseArray -> {
                // put the items in the list view
                adapter = new RVAdapter(MainActivity.this, exerciseArray, MainActivity.this, "main");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        });

        // assign values for views
        init();

        // define instance of service for communication with API
        ExerciseDataService exerciseDataService = new ExerciseDataService(MainActivity.this);

        // btn listeners
        btnSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick listener for the search button
             * @param view button view to set the on click listener to
             */
            @Override
            public void onClick(View view) {
                viewModel.searchAPI(exerciseDataService, etDataInput, selected_filters);
                closeKeyboard();
            }
        });

        btnFilters.setOnClickListener(view -> {
            viewModel.searchAPI(exerciseDataService, etDataInput, selected_filters);
            drawerLayout.closeDrawer(GravityCompat.END);
        });


        // -------------------- filter menu -------------------- //

        // filter menu adapters
        adapterDiff = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_difficulty);
        adapterMuscle = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_muscle);
        adapterType = new ArrayAdapter<>(this, R.layout.filter_dropdown_item, filter_type);

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

        // -------------------- action bar -------------------- //

        // top action bar listener
        filterIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));
        backFromMenuIcon.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.END));

        // -------------------- nav bar -------------------- //

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
                    case (R.id.profile):
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case (R.id.timer):
                        startActivity(new Intent(getApplicationContext(), TimerActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case (R.id.home):
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * close the keyboard
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
     * on activity lifecycle pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        autoCompleteDiff.setText("", false);
        autoCompleteMuscle.setText("", false);
        autoCompleteType.setText("", false);
    }

    /**
     * initialize values
     */
    public void init() {
        btnSearch = findViewById(R.id.btnSearch);
        btnFilters = findViewById(R.id.applyFilters);
        recyclerView = findViewById(R.id.rvSearchList);
        etDataInput = findViewById(R.id.etDataInput);

        // filter menu
        autoCompleteDiff = findViewById(R.id.autoCompleteDiff);
        autoCompleteMuscle = findViewById(R.id.autoCompleteMuscle);
        autoCompleteType = findViewById(R.id.autoCompleteType);

        // top action bar
        filterIcon = findViewById(R.id.menuOrAddIcon);
        filterIcon.setImageResource(R.drawable.ic_filter);
        tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.home_page);

        // side menu
        drawerLayout = findViewById(R.id.filter_drawer_layout);
        navigationView = findViewById(R.id.filter_view);
        header = navigationView.getHeaderView(0);
        backFromMenuIcon = header.findViewById(R.id.backFromMenu);
        sideMenuText = header.findViewById(R.id.pageTitle);
        sideMenuText.setText(R.string.main_side_menu);

        // bottom nav bar
        navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.home);
    }

}