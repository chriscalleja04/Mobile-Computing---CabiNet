package com.example.mobilecomputing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class MyCareersActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    ArrayList<String> id, teamName, teamAbbr;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_careers);
        recyclerView = findViewById(R.id.recyclerView_careers);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar); // Find your MaterialToolbar
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed(); // Emulate back button behavior
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_careers_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        myDB = new MyDatabaseHelper(MyCareersActivity.this);
        id = new ArrayList<>();
        teamName = new ArrayList<>();
        teamAbbr = new ArrayList<>();


        storeDataInArrays();

        customAdapter = new CustomAdapter(MyCareersActivity.this, this, id, teamName, teamAbbr, this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyCareersActivity.this));

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(MyCareersActivity.this, MainActivity.class);
                startActivity(i);

            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                teamName.add(cursor.getString(1));
                teamAbbr.add(cursor.getString(2));

            }
        }
    }

    @Override
    public void onItemClick(int position) {
        String careerId = id.get(position);
        String teamAbbr = this.teamAbbr.get(position);
        String teamName = this.teamName.get(position);
        Intent i = new Intent(MyCareersActivity.this, MySeasonsActivity.class);
        i.putExtra("CAREER_ID", careerId);
        i.putExtra("NAME", teamName);
        i.putExtra("ABBR", teamAbbr);
        startActivity(i);

    }
    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void newCareer(View v) {
        Intent i = new Intent(this, NewCareerActivity.class);
        startActivity(i);
    }


}