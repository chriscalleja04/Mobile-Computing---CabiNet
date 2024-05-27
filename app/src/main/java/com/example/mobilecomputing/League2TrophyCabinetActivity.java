package com.example.mobilecomputing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class League2TrophyCabinetActivity extends AppCompatActivity {

    private ImageView playoffImage, faImage, leagueImage;
    private CheckBox playoff_check, fa_check, league_check;
    private MyDatabaseHelper dbHelper;
    private long seasonId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_league2_trophy_cabinet);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
        setSupportActionBar(topAppBar); // Set your MaterialToolbar as the support action bar

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Emulate back button behavior
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.league2_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        String seasonIdString = intent.getStringExtra("ID");
        seasonId = Long.parseLong(seasonIdString);

        // Retrieve checkbox states from the database
        boolean isPlayoffVisible = dbHelper.isPlayoffTrophyVisible(seasonId);
        boolean isFaVisible = dbHelper.isFaTrophyVisible(seasonId);
        boolean isLeagueVisible = dbHelper.isLeagueTrophyVisible(seasonId);

        // Initialize views

        playoffImage = findViewById(R.id.league2_image);
        faImage = findViewById(R.id.fa_Image);
        leagueImage = findViewById(R.id.league_Image);



        playoff_check = findViewById(R.id.league2_box);
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
            // If Playoff checkbox is checked, uncheck Championship checkbox

        });

        fa_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateFaTrophyVisibility(seasonId, b ? 1 : 0);
            faImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });

        league_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateLeagueTrophyVisibility(seasonId, b ? 1 : 0);
            leagueImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });


        // Add listeners to save checkbox states in the database





        String abbrev = intent.getStringExtra("ABBR");
        ((TextView) findViewById(R.id.abbr)).setText("(" + abbrev + ")");

        String season = intent.getStringExtra("SEASON");
        ((TextView) findViewById(R.id.season)).setText(season);

        String league = intent.getStringExtra("LEAGUE");
        ((TextView) findViewById(R.id.league)).setText(league);



    }


    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
