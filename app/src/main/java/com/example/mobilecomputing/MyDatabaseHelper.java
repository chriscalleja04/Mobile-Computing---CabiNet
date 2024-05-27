package com.example.mobilecomputing;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Cabinet.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_careers";

    private static final String TABLE_NAME_ALL_LEAGUES = "all_leagues";

    private static final String TABLE_NAME_SEASONS = "my_seasons";
    private static final String TABLE_NAME_PREMIER = "premier_trophy_cabinet";

    private static final String TABLE_NAME_CHAMPIONSHIP = "championship_trophy_cabinet";

    private static final String TABLE_NAME_PLAYOFF = "playoff_trophy_cabinet";
    private static final String TABLE_NAME_TROPHIES = "trophy_cabinet";

    private static final String COLUMN_ID = "_id";

    //my careers
    private static final String COLUMN_NAME = "team_name";
    private static final String COLUMN_ABBR = "team_abbr";
    private static final String COLUMN_SEASON = "op_season";
    private static final String COLUMN_LEAGUE = "_league";

    //my seasons
    private static final String COLUMN_CAREERID = "career_id";


    //premier trophy cabinet
    private static final String COLUMN_SEASONID = "season_id";
    private static final String COLUMN_ISHIDDEN_PREM = "is_hidden_prem";
    private static final String COLUMN_ISHIDDEN_CHAMP = "is_hidden_champ";

    //parent trophy cabinet
    private static final String COLUMN_ISHIDDEN_FA = "is_hidden_fa";
    public static final String COLUMN_ISHIDDEN_LEAGUE = "is_hidden_league";

    //championship trophy cabinet
    private static final String COLUMN_ISHIDDEN_CHAMPIONSHIP = "is_hidden_championship";

    //playoff/league1/league2
    private static final String COLUMN_ISHIDDEN_PLAYOFF = "is_hidden_playoff";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getTableNameSeasons() {
        return TABLE_NAME_SEASONS;
    }

    public static String getTableNamePremier() {
        return TABLE_NAME_PREMIER;
    }

    public static String getID() {
        return COLUMN_ID;
    }


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create my_careers table
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ABBR + " TEXT); ");

        // Create my_seasons table
        db.execSQL("CREATE TABLE " + TABLE_NAME_SEASONS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CAREERID + " INTEGER, " +
                COLUMN_SEASON + " TEXT, " +
                COLUMN_LEAGUE + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_CAREERID + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ") "+ "ON DELETE CASCADE" + ");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_PREMIER +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SEASONID + " INTEGER, " +
                COLUMN_ISHIDDEN_PREM + " INTEGER, " +
                COLUMN_ISHIDDEN_CHAMP + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_SEASONID + ") REFERENCES " + TABLE_NAME_SEASONS + "(" + COLUMN_ID + ")"+ "ON DELETE CASCADE" + ");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_TROPHIES +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SEASONID + " INTEGER, " +
                COLUMN_ISHIDDEN_FA + " INTEGER, " +
                COLUMN_ISHIDDEN_LEAGUE + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_SEASONID + ") REFERENCES " + TABLE_NAME_SEASONS + "(" + COLUMN_ID + ")"+ "ON DELETE CASCADE" + ");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_CHAMPIONSHIP +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SEASONID + " INTEGER, " +
                COLUMN_ISHIDDEN_CHAMPIONSHIP + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_SEASONID + ") REFERENCES " + TABLE_NAME_SEASONS + "(" + COLUMN_ID + ")"+ "ON DELETE CASCADE" + ");");

        db.execSQL("CREATE TABLE " + TABLE_NAME_PLAYOFF +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SEASONID + " INTEGER, " +
                COLUMN_ISHIDDEN_PLAYOFF + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_SEASONID + ") REFERENCES " + TABLE_NAME_SEASONS + "(" + COLUMN_ID + ")"+ "ON DELETE CASCADE" + ");");


       }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SEASONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PREMIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TROPHIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CHAMPIONSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLAYOFF);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_ALL_LEAGUES);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    long addCareer(String name, String abbr, String season, String league) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_ABBR, abbr);

        long careerId = db.insert(TABLE_NAME, null, cv);
        if (careerId == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            if (season != null && league != null) {
                addSeason(careerId, season, league);
            }
        } else {
            Toast.makeText(context, "Career Added Successfully", Toast.LENGTH_SHORT).show();
        }


        return careerId;
    }


    void addSeason(long career_id, String season, String league) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        career_id = (int) career_id;
        cv.put(COLUMN_CAREERID, career_id);
        cv.put(COLUMN_SEASON, season);
        cv.put(COLUMN_LEAGUE, league);
        long result = db.insert(TABLE_NAME_SEASONS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(context, "Season Added Successfully", Toast.LENGTH_SHORT).show();
            initializeDefaultTrophyVisibility(result);
            initializeDefaultParentTrophyVisibility(result);
            initializeDefaultChampionshipTrophyVisibility(result);
            initializeDefaultPlayoffTrophyVisibility(result);

        }
    }

    public void initializeDefaultTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEASONID, seasonId);
        cv.put(COLUMN_ISHIDDEN_PREM, 0); // Default value for is_hidden_prem
        cv.put(COLUMN_ISHIDDEN_CHAMP, 0); // Default value for is_hidden_champ
        long result = db.insert(TABLE_NAME_PREMIER, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Failed to initialize trophy visibility", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Trophy visibility initialized successfully", Toast.LENGTH_SHORT).show();
//        }
    }

    public void initializeDefaultParentTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEASONID, seasonId);
        cv.put(COLUMN_ISHIDDEN_FA, 0);
        cv.put(COLUMN_ISHIDDEN_LEAGUE, 0);
        long result = db.insert(TABLE_NAME_TROPHIES, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Failed to initialise parent trophy visibility", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Parent Trophy visibility initialised successfully", Toast.LENGTH_SHORT).show();
//        }
    }

    public void initializeDefaultChampionshipTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEASONID, seasonId);
        cv.put(COLUMN_ISHIDDEN_CHAMPIONSHIP, 0);
        long result = db.insert(TABLE_NAME_CHAMPIONSHIP, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Failed to initialise parent trophy visibility", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Parent Trophy visibility initialised successfully", Toast.LENGTH_SHORT).show();
//        }
    }

    public void initializeDefaultPlayoffTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEASONID, seasonId);
        cv.put(COLUMN_ISHIDDEN_PLAYOFF, 0);
        long result = db.insert(TABLE_NAME_PLAYOFF, null, cv);
//        if (result == -1) {
//            Toast.makeText(context, "Failed to initialise parent trophy visibility", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Parent Trophy visibility initialised successfully", Toast.LENGTH_SHORT).show();
//        }


    }

    // Inside MyDatabaseHelper class

    // Method to clear checkbox states based on the season ID


    // Helper method to clear checkbox states for a specific table and season ID
    public void clearPlayoffTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set all checkbox states to 0 (unchecked)

        values.put(COLUMN_ISHIDDEN_PLAYOFF, 0);
        // Update the table with the new checkbox states
        db.update(TABLE_NAME_PLAYOFF, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});

    }

    public void clearChampionshipTrophyVisibility(long seasonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set all checkbox states to 0 (unchecked)

        values.put(COLUMN_ISHIDDEN_CHAMPIONSHIP, 0);

        // Update the table with the new checkbox states
        db.update(TABLE_NAME_CHAMPIONSHIP, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});

    }
    public void clearPremTrophyVisibility( long seasonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set all checkbox states to 0 (unchecked)
        values.put(COLUMN_ISHIDDEN_PREM, 0);
        values.put(COLUMN_ISHIDDEN_CHAMP, 0);

        // Update the table with the new checkbox states
        db.update(TABLE_NAME_PREMIER, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }
    public void clearTrophyVisibility( long seasonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set all checkbox states to 0 (unchecked)

        values.put(COLUMN_ISHIDDEN_FA, 0);
        values.put(COLUMN_ISHIDDEN_LEAGUE, 0);

        // Update the table with the new checkbox states
        db.update(TABLE_NAME_TROPHIES, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }



    public boolean isFaTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ISHIDDEN_FA + " FROM " + TABLE_NAME_TROPHIES +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {
            isVisible = cursor.getInt(0) == 1;
        }
        cursor.close();
        return isVisible;

    }

    public boolean isLeagueTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ISHIDDEN_LEAGUE + " FROM " + TABLE_NAME_TROPHIES +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {
            isVisible = cursor.getInt(0) == 1;
        }
        cursor.close();
        return isVisible;


    }

    public boolean isChampTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ISHIDDEN_CHAMP + " FROM " + TABLE_NAME_PREMIER +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {
            isVisible = cursor.getInt(0) == 1;
        }
        cursor.close();
        return isVisible;
    }


    public boolean isPremTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ISHIDDEN_PREM + " FROM " + TABLE_NAME_PREMIER +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {
            isVisible = cursor.getInt(0) == 1;
        }
        cursor.close();
        return isVisible;
    }

    public boolean isChampionshipTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // boolean isPlayoffVisible = isPlayoffTrophyVisible(seasonId);
        String query = "SELECT " + COLUMN_ISHIDDEN_CHAMPIONSHIP + " FROM " + TABLE_NAME_CHAMPIONSHIP +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {

            isVisible = cursor.getInt(0) == 1;
        }

        cursor.close();
        return isVisible;
    }

    public boolean isPlayoffTrophyVisible(long seasonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        //boolean isChampionshipVisible = isChampionshipTrophyVisible(seasonId);
        String query = "SELECT " + COLUMN_ISHIDDEN_PLAYOFF + " FROM " + TABLE_NAME_PLAYOFF +
                " WHERE " + COLUMN_SEASONID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seasonId)});
        boolean isVisible = false;
        if (cursor.moveToFirst()) {

            isVisible = cursor.getInt(0) == 1;

        }
        cursor.close();
        return isVisible;
    }

    public void updateFaTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_FA, visibility);
        db.update(TABLE_NAME_TROPHIES, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});

    }

    public void updateLeagueTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_LEAGUE, visibility);
        db.update(TABLE_NAME_TROPHIES, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});

    }

    public void updateChampTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_CHAMP, visibility);
        db.update(TABLE_NAME_PREMIER, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }

    public void updatePremTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_PREM, visibility);
        db.update(TABLE_NAME_PREMIER, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }

    public void updateChampionshipTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_CHAMPIONSHIP, visibility);
        db.update(TABLE_NAME_CHAMPIONSHIP, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }

    public void updatePlayoffTrophyVisibility(long seasonId, int visibility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISHIDDEN_PLAYOFF, visibility);
        db.update(TABLE_NAME_PLAYOFF, values, COLUMN_SEASONID + " = ?", new String[]{String.valueOf(seasonId)});
    }


    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

    public Cursor readAllDataSeasons(String careerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_SEASONS + " WHERE " + COLUMN_CAREERID + " = ?", new String[]{careerId});
        return cursor;
    }


    Cursor readAllDataPrem() {
        String query = "SELECT * FROM " + TABLE_NAME_PREMIER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

    void updateData(String row_id, String name, String abbr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_ABBR, abbr);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();

        }
    }

    void updateDataSeasons(String row_id, String seasons, String league) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEASON, seasons);
        cv.put(COLUMN_LEAGUE, league);

        long result = db.update(TABLE_NAME_SEASONS, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();

        }
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();


        }
    }

    void deleteOneRowSeason(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_SEASONS, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();


        }
    }

    public boolean isSeasonExistsUpdate(String season) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection query
        String query = "SELECT * FROM "+ TABLE_NAME_SEASONS+" WHERE "+COLUMN_SEASON+" = ?";

        // Define selection arguments
        String[] selectionArgs = {season};

        // Execute the query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Check if the cursor has any rows (i.e., if the season exists)
        boolean exists = cursor.getCount() > 0;

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;
    }

    public boolean isSeasonExists(int careerId, String season) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection query
        String query = "SELECT * FROM "+ TABLE_NAME_SEASONS+" WHERE "+COLUMN_CAREERID+" = ? AND "+COLUMN_SEASON+" = ?";

        // Define selection arguments
        String[] selectionArgs = {String.valueOf(careerId), season};

        // Execute the query
        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Check if the cursor has any rows (i.e., if the season exists)
        boolean exists = cursor.getCount() > 0;

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;
    }

    public List<String> getSeasonById(String career_id) {
        List<String> seasons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ COLUMN_SEASON + " FROM "+ TABLE_NAME_SEASONS+" WHERE " + COLUMN_CAREERID + " = ?", new String[]{String.valueOf(career_id)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String season = cursor.getString(cursor.getColumnIndex(COLUMN_SEASON));
                seasons.add(season);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return seasons;
    }
}






