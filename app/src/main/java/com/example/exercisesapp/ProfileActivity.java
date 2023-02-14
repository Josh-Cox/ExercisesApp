package com.example.exercisesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements RVInterface {

    // refresh boolean
    static boolean refreshed = true;

    NavigationBarView navigationBarView;
    RecyclerView recyclerView;
    ArrayList<ExInfoModel> savedExInfoModels = new ArrayList<ExInfoModel>();
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
        NavigationView navigationView = findViewById(R.id.side_nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView backFromMenuIcon = header.findViewById(R.id.backFromMenu);
        TextView sideMenuText = header.findViewById(R.id.pageTitle);
        sideMenuText.setText("Customize");


        // set attributes
        recyclerView = findViewById(R.id.rvSavedList);

        String filename = "saved";

        // get saved exercises from file
        savedExInfoModels = ExerciseInfoActivity.getSavedEx(ProfileActivity.this);

        // put the items in the list view
        adapter = new RVAdapter(ProfileActivity.this, savedExInfoModels, ProfileActivity.this, "profile");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));


        // top action bar listener
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // side menu listeners
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.END);

                switch(id) {
                    case R.id.item1:
                        Toast.makeText(context, "Item 1 pressed", Toast.LENGTH_SHORT).show();
                    case R.id.item2:
                        Toast.makeText(context, "Item 2 pressed", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }
            }
        });

        backFromMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });


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

    public void refreshActivity() {
        Intent i = new Intent(ProfileActivity.this, ProfileActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
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