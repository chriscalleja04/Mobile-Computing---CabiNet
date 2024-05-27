package com.example.mobilecomputing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toolbar;

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
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        SpannableString spannableString = new SpannableString("CabiNet");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        topAppBar.setTitle(spannableString);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                finishAffinity();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void newCareer(View v) {
        Intent i = new Intent(this, NewCareerActivity.class);
        startActivity(i);
    }

    public void myCareers(View v) {
        Intent i = new Intent(this, MyCareersActivity.class);
        startActivity(i);
    }



}