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

public class TrophyCabinetActivity extends AppCompatActivity {
    private ImageView trophyImage, trophyImage2, faImage, leagueImage;
    private CheckBox check, check2, fa_check, league_check;
    private MyDatabaseHelper dbHelper;
    private long seasonId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trophy_cabinet);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
        setSupportActionBar(topAppBar); // Set your MaterialToolbar as the support action bar

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                    @Override
                    public void handleOnBackPressed() {
                        Intent i = new Intent(TrophyCabinetActivity.this, MySeasonsActivity.class);
                        startActivity(i);
                    }
                };

                getOnBackPressedDispatcher().addCallback(TrophyCabinetActivity.this, callback);

                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.premier_league_layout), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
        });

        dbHelper = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        String seasonIdString = intent.getStringExtra("ID");
        seasonId = Long.parseLong(seasonIdString);

        // Retrieve checkbox states from the database
        boolean isChampVisible = dbHelper.isChampTrophyVisible(seasonId);
        boolean isPremVisible = dbHelper.isPremTrophyVisible(seasonId);
        boolean isFaVisible = dbHelper.isFaTrophyVisible(seasonId);
        boolean isLeagueVisible = dbHelper.isLeagueTrophyVisible(seasonId);

        // Initialize views
        faImage = findViewById(R.id.fa_Image);
        leagueImage = findViewById(R.id.league_Image);
        trophyImage = findViewById(R.id.champ_Image);
        trophyImage2 = findViewById(R.id.prem_Image);


        fa_check = findViewById(R.id.fa_box);
        league_check = findViewById(R.id.league_box);
        check = findViewById(R.id.champ_box);
        check2 = findViewById(R.id.premier_box);


        // Set checkbox states
        fa_check.setChecked(isFaVisible);
        league_check.setChecked(isLeagueVisible);
        check.setChecked(isChampVisible);
        check2.setChecked(isPremVisible);

        // Set visibility of trophy images based on checkbox states
        faImage.setVisibility(isFaVisible ? View.VISIBLE : View.GONE);
        leagueImage.setVisibility(isLeagueVisible ? View.VISIBLE : View.GONE);
        trophyImage.setVisibility(isChampVisible ? View.VISIBLE : View.GONE);
        trophyImage2.setVisibility(isPremVisible ? View.VISIBLE : View.GONE);


        fa_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateFaTrophyVisibility(seasonId, b ? 1 : 0);
            faImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });

        league_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateLeagueTrophyVisibility(seasonId, b ? 1 : 0);
            leagueImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });


        // Add listeners to save checkbox states in the database
        check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateChampTrophyVisibility(seasonId, b ? 1 : 0);
            trophyImage.setVisibility(b ? View.VISIBLE : View.GONE);
        });

        check2.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updatePremTrophyVisibility(seasonId, b ? 1 : 0);
            trophyImage2.setVisibility(b ? View.VISIBLE : View.GONE);
        });




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
