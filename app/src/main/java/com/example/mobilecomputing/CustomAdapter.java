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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    Activity activity;
    private ArrayList id, teamName, teamAbbr;


    public CustomAdapter(Activity activity, Context context,
                  ArrayList id,
                  ArrayList teamName,
                  ArrayList teamAbbr,

                         RecyclerViewInterface recyclerViewInterface){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.teamName = teamName;
        this.teamAbbr = teamAbbr;
        this.recyclerViewInterface = recyclerViewInterface;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.team_name_txt.setText(String.valueOf(teamName.get(position)));
        holder.team_abbr_txt.setText(String.valueOf(teamAbbr.get(position)));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateCareersActivity.class);
                i.putExtra("id", String.valueOf(id.get(position)));
                i.putExtra("teamName", String.valueOf(teamName.get(position)));
                i.putExtra("teamAbbr", String.valueOf(teamAbbr.get(position)));

                activity.startActivityForResult(i, 1);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
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

    void confirmDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete " + teamName.get(position) + " career ?");
        builder.setMessage("Are you sure you want to delete your " + teamName.get(position) + " career ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
            myDB.deleteOneRow(String.valueOf(id.get(position)));
            Intent intent = new Intent(context, MyCareersActivity.class);
            activity.startActivityForResult(intent, 1);

        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();


    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView team_name_txt, team_abbr_txt;
        FloatingActionButton edit, delete;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            team_name_txt = itemView.findViewById(R.id.team_name_txt);
            team_abbr_txt = itemView.findViewById(R.id.team_abbr_txt);
            edit = itemView.findViewById(R.id.edit2);
            delete = itemView.findViewById(R.id.delete);

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
