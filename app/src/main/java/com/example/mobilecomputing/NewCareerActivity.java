package com.example.mobilecomputing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;


public class NewCareerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    MyDatabaseHelper myDb;
    SQLiteDatabase sqLiteDatabase;
    Spinner spinner;

    String[] leagues = {"Premier League", "Championship", "League One", "League Two"};
    EditText teamName, teamAbbr, editTextDate;


    Button submit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_career);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
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
        teamName = findViewById(R.id.teamName);
        teamAbbr = findViewById(R.id.teamAbbriv);
        editTextDate = findViewById(R.id.editTextDate_season2);
        spinner = findViewById(R.id.dropDown_season2);
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, leagues);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        submit = findViewById(R.id.update_season);
        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(NewCareerActivity.this);
                String season = editTextDate.getText().toString().trim();
                String name = teamName.getText().toString().trim();
                String abbr = teamAbbr.getText().toString().trim().toUpperCase();
                // Check if the input is null or empty
                if (name.isEmpty()) {
                    teamName.setError("This is a required field");
                    return; // Prevent further execution
                }
                if(name.length()>17){
                    teamName.setError("Cannot exceed 17 characters");
                    return;
                }
                if (abbr.isEmpty()) {
                    teamAbbr.setError("This is a required field");
                    return; // Prevent further execution
                }
                if(abbr.length()<3) {
                    teamAbbr.setError("Cannot be less than 3 characters");
                    return;
                }
                if(abbr.length()>4){
                    teamAbbr.setError("Cannot exceed 4 characters");
                    return;
                }
                if (season.isEmpty()) {
                    editTextDate.setError("This is a required field");
                    return; // Prevent further execution
                }

                // Check if the input has the correct format of YYYY/YY
                if (!season.matches("\\d{4}/\\d{2}") && !season.matches("\\d{4}/\\d{4}") && !season.matches("\\d{2}/\\d{2}") ) {
                    editTextDate.setError("Please enter the season in the format YYYY/YY, YYYY/YYYY or YY/YY. Kindly include the '/'");
                    return; // Prevent further execution
                }

                // Get the start year and next year
                String[] years = season.split("/");
                int startYear = Integer.parseInt(years[0]) % 100; // Get last two digits
                int nextYear = Integer.parseInt(years[1]) % 100; // Get last two digits

                // Calculate the difference between start and next year
                int yearDifference = nextYear - startYear;

                // Check if the difference is exactly one year
                if (yearDifference != 1) {
                    // Difference is not exactly one year, show error message
                    editTextDate.setError("The latter half of the season should begin precisely one year after the commencement of the opening half (2023/24, 2023/2024 or 23/24)");
                    return; // Prevent further execution
                }

                season = standardizeSeasonFormat(season);

                String league = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();


                long careerId = myDB.addCareer(name,
                        abbr,
                        season, league);
                myDB.addSeason(careerId, season, league);


                Intent i = new Intent(NewCareerActivity.this, MyCareersActivity.class);
                startActivity(i);



            }

        });
    }
    private String standardizeSeasonFormat(String season) {
        // Split season once to get start and next years
        String[] years = season.split("/");
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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void trophyCabinet(View view) {
        Intent i = new Intent(this, TrophyCabinetActivity.class);
        String name = ((EditText) findViewById(R.id.teamName)).getText().toString();
        i.putExtra("NAME", name);

        String abbrev = ((EditText) findViewById(R.id.teamAbbriv)).getText().toString();
        i.putExtra("ABBREV", abbrev);

        startActivity(i);
    }
    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void home(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}