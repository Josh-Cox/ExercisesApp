package com.example.exercisesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements RVInterface {

    // -------------------- global variables -------------------- //
    static boolean refreshed = true;
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<>();
    Context context = ProfileActivity.this;

    // -------------------- define views -------------------- //

    // main content
    NavigationBarView navigationBarView;
    RecyclerView recyclerView;
    TextView tvActionBar;
    RVAdapter adapter;

    // action bar
    ImageView menuIcon;

    // side menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View header;
    ImageView backFromMenuIcon;
    TextView sideMenuText;
    Menu menu;


    /**
     * on activity creation
     * @param savedInstanceState current instance of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // -------------------- main setup -------------------- //

        //initialize views
        init();

        // get saved exercises from file
        savedExInfoModels = ExerciseInfoActivity.getSavedEx(ProfileActivity.this);

        // put the items in the list view
        adapter = new RVAdapter(ProfileActivity.this, savedExInfoModels, ProfileActivity.this, "profile");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));

        // -------------------- action bar -------------------- //

        // top action bar listener
        menuIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));

        // -------------------- side menu -------------------- //

        // side menu listeners
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.END);

            if (id == R.id.findGyms) {

                    // build intent
                    Uri location = Uri.parse("https://www.google.com/maps/search/gyms+near+me/");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    Intent chooser = Intent.createChooser(mapIntent, "Maps");

                    try {
                        startActivity(chooser);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No suitable app found", Toast.LENGTH_SHORT).show();
                    }
            }

            return true;
        });

        backFromMenuIcon.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.END));

        // -------------------- nav bar -------------------- //

        navigationBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.profile):
                    return true;

                case (R.id.timer):
                    startActivity(new Intent(getApplicationContext(), TimerActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case (R.id.home):
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }

    /**
     * on recycler view item click
     * @param position position of exercise object
     * @param exInfoModels list of exercise objects
     * @param clickedFrom activity user was previously on
     */
    @Override
    public void onItemClick(int position, ArrayList<ExInfoModel> exInfoModels, String clickedFrom) {
        Intent intent = new Intent(this, ExerciseInfoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise", Parcels.wrap(exInfoModels.get(position)));
        bundle.putParcelable("clickedFrom", Parcels.wrap(clickedFrom));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    /**
     * on activity lifecycle resume (refresh recycler view)
     */
    @Override
    protected void onResume() {

        // if it needs refreshing
        if(!refreshed) {
            recreate();
            refreshed = true;
        }

        // still call onResume
        super.onResume();
    }

    /**
     * initialize variables
     */
    public void init() {
        //main content
        recyclerView = findViewById(R.id.rvSavedList);

        // top action bar
        menuIcon = findViewById(R.id.menuOrAddIcon);
        tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.profile_page);

        // side menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.menu_view);
        header = navigationView.getHeaderView(0);
        backFromMenuIcon = header.findViewById(R.id.backFromMenu);
        sideMenuText = header.findViewById(R.id.pageTitle);
        sideMenuText.setText(R.string.profile_side_menu);
        menu = navigationView.getMenu();

        //nav bar
        navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.profile);
    }
}