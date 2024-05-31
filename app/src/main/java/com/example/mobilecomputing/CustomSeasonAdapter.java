package com.example.mobilecomputing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomSeasonAdapter extends RecyclerView.Adapter<CustomSeasonAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;

    Activity activity;
    private ArrayList id,season, league;

    public CustomSeasonAdapter(Activity activity,Context context,
                                ArrayList id,
                               ArrayList season,
                               ArrayList league,
                               RecyclerViewInterface recyclerViewInterface) {
       this.activity = activity;
        this.context = context;
        this.id = id;
        this.season = season;
        this.league = league;
        this.recyclerViewInterface = recyclerViewInterface;



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout for each row
        View view = inflater.inflate(R.layout.season_my_row, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }



    @Override
    public void onBindViewHolder(@NonNull CustomSeasonAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to views
        holder.new_season.setText(String.valueOf(season.get(position)));
        holder.new_league.setText(String.valueOf(league.get(position)));

        // Set click listeners for edit and delete buttons
        holder.edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start UpdateSeasonsActivity with data
                Intent i = new Intent(context, UpdateSeasonsActivity.class);
                i.putExtra("id", String.valueOf(id.get(position)));
                i.putExtra("season", String.valueOf(season.get(position)));
                i.putExtra("league", String.valueOf(league.get(position)));

                activity.startActivityForResult(i, 1);
            }
        });
        holder.delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(position);
            }
        });
    }


        @Override

        public int getItemCount() {

        return id.size();
        }

    // Confirmation dialog for delete
    void confirmDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete " + season.get(position) + " season ?");
        builder.setMessage("Are you sure you want to delete the " + season.get(position) + " season ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
            myDB.deleteOneRowSeason(String.valueOf(id.get(position)));
            Intent intent = new Intent(context, MySeasonsActivity.class);
            activity.startActivityForResult(intent, 1);

        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView new_season, new_league;

        FloatingActionButton edit2, delete2;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            // Initialize views
            new_season = itemView.findViewById(R.id.new_season);
            new_league = itemView.findViewById(R.id.new_league);
            edit2 = itemView.findViewById(R.id.edit2);
            delete2 = itemView.findViewById(R.id.delete2);

            // Set click listener for item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
            }

        }
    }

