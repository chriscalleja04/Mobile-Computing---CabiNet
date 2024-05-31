package com.example.mobilecomputing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.Objects;

public class UpdateSeasonsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        EditText season_input;
        Activity activity;
        ArrayAdapter arrayAdapter;
        Button update_button;
        Spinner spinner;
        String[] leagues = {"Premier League", "Championship", "League One", "League Two"};

        String id, season, league;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_update_seasons);

            // Initialising top app bar and setting click listener for back button
            MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
            setSupportActionBar(topAppBar);
            topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click to navigate back to MySeasonsActivity
                    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            Intent i = new Intent(UpdateSeasonsActivity.this, MySeasonsActivity.class);
                            startActivity(i);
                        }
                    };

                    getOnBackPressedDispatcher().addCallback(UpdateSeasonsActivity.this, callback);

                    getOnBackPressedDispatcher().onBackPressed();
                }
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.league2_layout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize views and button
            season_input = findViewById(R.id.editTextDate_season2);
            update_button = findViewById(R.id.update_season);

            spinner = findViewById(R.id.dropDown_season);
            spinner.setOnItemSelectedListener(this);

            // Populate spinner with league options
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, leagues);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

            // Retrieve and set data passed via Intent
            getAndSetIntentData();

            // Set click listener for the update button
            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateSeasonsActivity.this);
                    String updatedSeason = season_input.getText().toString();
                    // Validate the input fields
                    if (updatedSeason.isEmpty()) {
                        season_input.setError("This is a required field");
                        return;
                    }

                    // Validate the season format
                    if (!updatedSeason.matches("\\d{4}/\\d{2}") && !updatedSeason.matches("\\d{4}/\\d{4}") && !updatedSeason.matches("\\d{2}/\\d{2}")) {
                        season_input.setError("Please enter the season in the format YYYY/YY, YYYY/YYYY or YY/YY. Kindly include the '/'");
                        return; // Prevent further execution
                    }


                    String[] years = updatedSeason.split("/");

                    // Get last two digits
                    int startYear = Integer.parseInt(years[0]) % 100;
                    int nextYear = Integer.parseInt(years[1]) % 100;

                    int yearDifference = nextYear - startYear;

                    if (yearDifference != 1) {
                        season_input.setError("The latter half of the season should begin precisely one year after the commencement of the opening half (2023/24, 2023/2024 or 23/24)");
                        return;
                    }

                    // Standardize the season format to YYYY/YY
                    updatedSeason = standardizeSeasonFormat(updatedSeason);

                    // Check if the season already exists in the database
                    if (!updatedSeason.equals(season)) {
                        // If the updated season is different, check if it already exists in the database
                        if (myDB.isSeasonExistsUpdate(updatedSeason)) {
                            season_input.setError("This season already exists");
                            return;
                        }
                    }
                        //update season data and clear trophy visibilities
                        myDB.updateDataSeasons(id, updatedSeason, league);
                        myDB.clearPlayoffTrophyVisibility(Long.parseLong(id));
                        myDB.clearTrophyVisibility(Long.parseLong(id));
                        myDB.clearChampionshipTrophyVisibility(Long.parseLong(id));
                        myDB.clearPremTrophyVisibility(Long.parseLong(id));

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedSeason", updatedSeason);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                }
            });





        }
    private String standardizeSeasonFormat(String updatedSeason) {
        // Split season once to get start and next years
        String[] years = updatedSeason.split("/");
        int startYear = Integer.parseInt(years[0]);
        int nextYear = Integer.parseInt(years[1]);

        // Convert start year and next year to 4-digit format if necessary
        if (startYear < 100) {
            startYear += 2000; // Convert to 4-digit format (e.g., 23 becomes 2023)
        }
        if (nextYear < 100) {
            nextYear += 2000; // Convert to 4-digit format (e.g., 24 becomes 2024)
        }

        return startYear + "/" + nextYear % 100; // Return season in YYYY/YY format
    }
    // Method to retrieve and set data passed via Intent
    void getAndSetIntentData(){
            if(getIntent().hasExtra("id") && getIntent().hasExtra("season") && getIntent().hasExtra("league")){
                //Getting Data From Intent
                id = getIntent().getStringExtra("id");
                season = getIntent().getStringExtra("season");
                league = getIntent().getStringExtra("league");

                // Set data to input fields and spinner
                season_input.setText(season);
                int spinnerPosition = arrayAdapter.getPosition(league);
                spinner.setSelection(spinnerPosition);
            }else{
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            }
        }

        //handle item selection in the spinner
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            league = leagues[i];

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    //Navigate back to MySeasonsActivity
    public void mySeasons(View view){
            Intent i = new Intent(this, MySeasonsActivity.class);
            startActivity(i);
            finish();
    }

    //Navigate back to MainActivity
    public void goHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}