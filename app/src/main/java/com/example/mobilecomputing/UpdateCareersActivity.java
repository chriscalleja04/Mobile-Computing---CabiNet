package com.example.mobilecomputing;

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

public class UpdateCareersActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    EditText team_name_input, team_abbr_input;
    ArrayAdapter arrayAdapter;
    Button update_button;


    String id, name, abbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_careers);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
        setSupportActionBar(topAppBar); // Set your MaterialToolbar as the support action bar

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                    @Override
                    public void handleOnBackPressed() {
                        Intent i = new Intent(UpdateCareersActivity.this, MyCareersActivity.class);
                        startActivity(i);
                    }
                };

                getOnBackPressedDispatcher().addCallback(UpdateCareersActivity.this, callback);

                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        team_name_input = findViewById(R.id.teamName2);
        team_abbr_input = findViewById(R.id.teamAbbriv2);
        update_button = findViewById(R.id.update_buttom);


        getAndSetIntentData();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve updated values from input fields
                String updatedName = team_name_input.getText().toString().trim();
                String updatedAbbr = team_abbr_input.getText().toString().trim().toUpperCase();
                if (updatedName.isEmpty()) {
                    team_name_input.setError("This is a required field");
                    return; // Prevent further execution
                }
                if(updatedName.length()>17){
                    team_name_input.setError("Cannot exceed 17 characters");
                    return;
                }
                if (updatedAbbr.isEmpty()) {
                    team_abbr_input.setError("This is a required field");
                    return; // Prevent further execution
                }
                if(updatedAbbr.length()<3) {
                    team_abbr_input.setError("Cannot be less than 3 characters");
                    return;
                }
                if(updatedAbbr.length()>4){
                    team_abbr_input.setError("Cannot exceed 4 characters");
                    return;
                }

                // Call the updateData() method with updated values
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateCareersActivity.this);
                myDB.updateData(id, updatedName, updatedAbbr);
                Intent i = new Intent(UpdateCareersActivity.this, MyCareersActivity.class);
                startActivity(i);
            }
        });


    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("teamName") && getIntent().hasExtra("teamAbbr")) {
            //Getting Data From Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("teamName");
            abbr = getIntent().getStringExtra("teamAbbr");


            //Setting intent data
            team_name_input.setText(name);
            team_abbr_input.setText(abbr);


        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Update the league variable with the selected league

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Handle the case when nothing is selected in the spinner
    }

    public void myCareers(View v) {


        Intent i = new Intent(UpdateCareersActivity.this, MyCareersActivity.class);
        startActivity(i);


    }

    public void goHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

