package com.example.mobilecomputing;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialising top app bar and setting title with styled text
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        SpannableString spannableString = new SpannableString("CabiNet");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        topAppBar.setTitle(spannableString);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handling back button press to close all activities and exit the app
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                finishAffinity();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Start NewCareerActivity when the 'Create New Career' button is clicked
    public void newCareer(View v) {
        Intent i = new Intent(this, NewCareerActivity.class);
        startActivity(i);
    }
    // Start MyCareersActivity when the 'MyCareersActivity' button is clicked

    public void myCareers(View v) {
        Intent i = new Intent(this, MyCareersActivity.class);
        startActivity(i);
    }



}