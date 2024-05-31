package com.example.mobilecomputing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MySeasonsActivity extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    ArrayList<String> id, season, leagues;

    CustomSeasonAdapter customSeasonAdapter;

    // Member variable to store seasonId
    private String seasonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_seasons);
        recyclerView = findViewById(R.id.sRecyclerView);

        // Initialising top app bar and setting click listener for back button
        MaterialToolbar topAppBar = findViewById(R.id.topAppBarTest); // Find your MaterialToolbar
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click to navigate back to MyCareersActivity
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Intent i = new Intent(MySeasonsActivity.this, MyCareersActivity.class);
                        startActivity(i);
                    }
                };

                getOnBackPressedDispatcher().addCallback(MySeasonsActivity.this, callback);

                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_seasons_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDB = new MyDatabaseHelper(MySeasonsActivity.this);
        id = new ArrayList<>();
        season = new ArrayList<>();
        leagues = new ArrayList<>();

        String career_id = getIntent().getStringExtra("CAREER_ID");

        if (career_id != null) {
            storeDataInArrays(career_id);
        } else {
            //Toast.makeText(this, "Career ID is null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        customSeasonAdapter = new CustomSeasonAdapter(MySeasonsActivity.this, this, id, season, leagues, this);
        recyclerView.setAdapter(customSeasonAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MySeasonsActivity.this));
    }



    // Populate data arrays from the database
    void storeDataInArrays(String career_id) {
        Cursor cursor = myDB.readAllDataSeasons(career_id);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                season.add(cursor.getString(2));
                leagues.add(cursor.getString(3));
            }
        }
    }

    // Navigate to NewSeasonActivity
    public void newSeason(View v) {
        String career_id = getIntent().getStringExtra("CAREER_ID");
        Intent i = new Intent(this, NewSeasonActivity.class);
        i.putExtra("CAREER_ID", career_id);
        startActivity(i);
    }

    //Navigate to relevant cabinet activities
    public void onItemClick(int position) {
        String seasonId = id.get(position);
        String season = this.season.get(position);
        String league = this.leagues.get(position);
        String teamAbbr = getIntent().getStringExtra("ABBR");
        String teamName = getIntent().getStringExtra("NAME");

        switch (league) {
            case "Premier League": {
                Intent i = new Intent(MySeasonsActivity.this, TrophyCabinetActivity.class);

                i.putExtra("ID", seasonId);
                i.putExtra("ABBR", teamAbbr);
                i.putExtra("NAME", teamName);
                i.putExtra("SEASON", season);
                i.putExtra("LEAGUE", league);
                startActivity(i);

                break;
            }
            case "Championship": {
                Intent i = new Intent(MySeasonsActivity.this, ChampionshipTrophyCabinetActivity.class);

                i.putExtra("ID", seasonId);
                i.putExtra("ABBR", teamAbbr);
                i.putExtra("NAME", teamName);
                i.putExtra("SEASON", season);
                i.putExtra("LEAGUE", league);
                startActivity(i);

                break;
            }
            case "League One": {
                Intent i = new Intent(MySeasonsActivity.this, League1TrophyCabinetActivity.class);

                i.putExtra("ID", seasonId);
                i.putExtra("ABBR", teamAbbr);
                i.putExtra("NAME", teamName);
                i.putExtra("SEASON", season);
                i.putExtra("LEAGUE", league);
                startActivity(i);

                break;
            }
            case "League Two": {
                Intent i = new Intent(MySeasonsActivity.this, League2TrophyCabinetActivity.class);

                i.putExtra("ID", seasonId);
                i.putExtra("ABBR", teamAbbr);
                i.putExtra("NAME", teamName);
                i.putExtra("SEASON", season);
                i.putExtra("LEAGUE", league);
                startActivity(i);
                break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Reload data from database when resuming
        id.clear();
        season.clear();
        leagues.clear();
        storeDataInArrays(getIntent().getStringExtra("CAREER_ID"));
        customSeasonAdapter.notifyDataSetChanged();
    }
    //Retrieve updated season data and update UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.hasExtra("updatedSeason")) {
                    String updatedSeason = data.getStringExtra("updatedSeason");
                    int position = id.indexOf(seasonId);
                    if (position != -1) {
                        season.set(position, updatedSeason);
                        customSeasonAdapter.notifyItemChanged(position);
                    }
                }
            }
        }
    }

    //navigate back to the home activity
    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}