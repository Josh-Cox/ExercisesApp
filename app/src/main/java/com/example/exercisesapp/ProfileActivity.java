package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements RVInterface {

    // refresh boolean
    static boolean refreshed = true;


    NavigationBarView navigationBarView;
    RecyclerView recyclerView;
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<>();
    Context context = ProfileActivity.this;
    TextView tvActionBar;
    RVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // top action bar
        ImageView menuIcon = findViewById(R.id.menuOrAddIcon);
        tvActionBar = findViewById(R.id.pageTitle);
        tvActionBar.setText(R.string.profile_page);

        // side menu
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.menu_view);
        View header = navigationView.getHeaderView(0);
        ImageView backFromMenuIcon = header.findViewById(R.id.backFromMenu);
        TextView sideMenuText = header.findViewById(R.id.pageTitle);
        sideMenuText.setText(R.string.profile_side_menu);
        Menu menu = navigationView.getMenu();

        // set attributes
        recyclerView = findViewById(R.id.rvSavedList);

        // get saved exercises from file
        savedExInfoModels = ExerciseInfoActivity.getSavedEx(ProfileActivity.this);

        // put the items in the list view
        adapter = new RVAdapter(ProfileActivity.this, savedExInfoModels, ProfileActivity.this, "profile");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));


        // top action bar listener
        menuIcon.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));


        // side menu listeners
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.END);

            switch(id) {

                case R.id.findGyms:
                    // build intent
                    Uri location = Uri.parse("https://www.google.com/maps/search/gyms+near+me/");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    Intent chooser = Intent.createChooser(mapIntent, "Maps");

                    try {
                        startActivity(chooser);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No suitable app found", Toast.LENGTH_SHORT).show();
                    }


                default:
                    return true;
            }
        });

        backFromMenuIcon.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.END));


        // bottom nav bar
        navigationBarView = findViewById(R.id.bottom_nav);
        navigationBarView.setSelectedItemId(R.id.profile);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
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

    // override onResume to re-create activity (to refresh recycleView)
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
}