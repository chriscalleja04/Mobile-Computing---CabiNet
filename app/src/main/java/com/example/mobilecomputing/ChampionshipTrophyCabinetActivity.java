package com.example.mobilecomputing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.MaterialToolbar;


public class ChampionshipTrophyCabinetActivity extends AppCompatActivity {

    private ImageView championshipImage, playoffImage, faImage, leagueImage;
    private CheckBox championship_check, playoff_check, fa_check, league_check;
    private MyDatabaseHelper dbHelper;
    private long seasonId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_championship_trophy_cabinet);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
        setSupportActionBar(topAppBar); // Set your MaterialToolbar as the support action bar

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Intent i = new Intent(ChampionshipTrophyCabinetActivity.this, MySeasonsActivity.class);
                        startActivity(i);
                    }
                };

                getOnBackPressedDispatcher().addCallback(ChampionshipTrophyCabinetActivity.this, callback);

                getOnBackPressedDispatcher().onBackPressed();
            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.championship_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        dbHelper = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        String seasonIdString = intent.getStringExtra("ID");
        seasonId = Long.parseLong(seasonIdString);





        // Retrieve checkbox states from the database
        boolean isChampionshipVisible = dbHelper.isChampionshipTrophyVisible(seasonId);
        boolean isPlayoffVisible = dbHelper.isPlayoffTrophyVisible(seasonId);
        boolean isFaVisible = dbHelper.isFaTrophyVisible(seasonId);
        boolean isLeagueVisible = dbHelper.isLeagueTrophyVisible(seasonId);

        // Initialize views
        championshipImage = findViewById(R.id.championship_Image);
        playoffImage = findViewById(R.id.playoff_image);
        faImage = findViewById(R.id.fa_Image);
        leagueImage = findViewById(R.id.league_Image);


        championship_check = findViewById(R.id.championship_box);
        playoff_check = findViewById(R.id.playoff_box);
        fa_check = findViewById(R.id.fa_box);
        league_check = findViewById(R.id.league_box);



        // Set checkbox states
        championship_check.setChecked(isChampionshipVisible);
        playoff_check.setChecked(isPlayoffVisible);
        fa_check.setChecked(isFaVisible);
        league_check.setChecked(isLeagueVisible);


        // Set visibility of trophy images based on checkbox states
        championshipImage.setVisibility(isChampionshipVisible ? View.VISIBLE : View.GONE);
        playoffImage.setVisibility(isPlayoffVisible ? View.VISIBLE : View.GONE);
        faImage.setVisibility(isFaVisible ? View.VISIBLE : View.GONE);
        leagueImage.setVisibility(isLeagueVisible ? View.VISIBLE : View.GONE);

        championship_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateChampionshipTrophyVisibility(seasonId, b ? 1 : 0);
            championshipImage.setVisibility(b ? View.VISIBLE : View.GONE);
            // If Championship checkbox is checked, uncheck Playoff checkbox
            if (b) {
                playoff_check.setChecked(false);
            }
        });

        playoff_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updatePlayoffTrophyVisibility(seasonId, b ? 1 : 0);
            playoffImage.setVisibility(b ? View.VISIBLE : View.GONE);
            // If Playoff checkbox is checked, uncheck Championship checkbox
            if (b) {
                championship_check.setChecked(false);
            }
        });

        fa_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateFaTrophyVisibility(seasonId, b ? 1 : 0);
            faImage.setVisibility(b ? View.VISIBLE : View.GONE);

        });

        league_check.setOnCheckedChangeListener((compoundButton, b) -> {
            dbHelper.updateLeagueTrophyVisibility(seasonId, b ? 1 : 0);
            leagueImage.setVisibility(b ? View.VISIBLE : View.GONE);

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



