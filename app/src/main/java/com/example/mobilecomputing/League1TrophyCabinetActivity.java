package com.example.mobilecomputing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class League1TrophyCabinetActivity extends AppCompatActivity {

    private ImageView playoffImage, faImage, leagueImage;
    private CheckBox playoff_check, fa_check, league_check;
    private MyDatabaseHelper dbHelper;
    private long seasonId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_league1_trophy_cabinet);

        // Initialising top app bar and setting click listener for back button
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    // Handle back button click to navigate back to MySeasonsActivity

                    @Override
                    public void handleOnBackPressed() {
                        Intent i = new Intent(League1TrophyCabinetActivity.this, MySeasonsActivity.class);
                        startActivity(i);
                    }
                };

                getOnBackPressedDispatcher().addCallback(League1TrophyCabinetActivity.this, callback);

                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.league1_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        dbHelper = new MyDatabaseHelper(this);

        // Retrieve season ID from the intent
        Intent intent = getIntent();
        String seasonIdString = intent.getStringExtra("ID");
        seasonId = Long.parseLong(seasonIdString);

        // Retrieve checkbox states from the database
        boolean isPlayoffVisible = dbHelper.isPlayoffTrophyVisible(seasonId);
        boolean isFaVisible = dbHelper.isFaTrophyVisible(seasonId);
        boolean isLeagueVisible = dbHelper.isLeagueTrophyVisible(seasonId);

        // Initialize views
        playoffImage = findViewById(R.id.league1_image);
        faImage = findViewById(R.id.fa_Image);
        leagueImage = findViewById(R.id.league_Image);



        playoff_check = findViewById(R.id.league1_box);
        fa_check = findViewById(R.id.fa_box);
        league_check = findViewById(R.id.league_box);



        // Set checkbox states
        playoff_check.setChecked(isPlayoffVisible);
        fa_check.setChecked(isFaVisible);
        league_check.setChecked(isLeagueVisible);


        // Set visibility of trophy images based on checkbox states
        playoffImage.setVisibility(isPlayoffVisible ? View.VISIBLE : View.GONE);
        faImage.setVisibility(isFaVisible ? View.VISIBLE : View.GONE);
        leagueImage.setVisibility(isLeagueVisible ? View.VISIBLE : View.GONE);


        playoff_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updatePlayoffTrophyVisibility(seasonId, b ? 1 : 0);
            playoffImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });

        fa_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateFaTrophyVisibility(seasonId, b ? 1 : 0);
            faImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });

        league_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateLeagueTrophyVisibility(seasonId, b ? 1 : 0);
            leagueImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });


        // Set team abbreviation, season, and league text views
        String abbrev = intent.getStringExtra("ABBR");
        ((TextView) findViewById(R.id.abbr)).setText("(" + abbrev + ")");

        String season = intent.getStringExtra("SEASON");
        ((TextView) findViewById(R.id.season)).setText(season);

        String league = intent.getStringExtra("LEAGUE");
        ((TextView) findViewById(R.id.league)).setText(league);



    }
    // Navigate back to the main activity when home button is clicked
    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
