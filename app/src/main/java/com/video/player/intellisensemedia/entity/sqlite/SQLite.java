package com.video.player.intellisensemedia.entity.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper implements SQLiteDetails {

    public SQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY1 = "create table " + TABLE_LIBRARY
                + " ( " + LIBRARY_ID + " integer PRIMARY KEY, "
                + "  " + LIBRARY_NAME + " text);";

        String QUERY2 = "create table " + TABLE_VIDEOS
                + " ( " + VIDEOS_LIBRARY_ID + " integer,"
                + " " + VIDEOS_TAG + " text,"
                + " " + VIDEOS_VIDEO_ID + " text);";

        String QUERY3 = "create table " + TABLE_TAGS
                + " ( " + TAGS_TAG + " text PRIMARY KEY,"
                + "  " + TAGS_HITS + " integer);";

        String QUERY4 = "create table " + TABLE_HISTORY
                + " ( " + HISTORY_VIDEO_ID + " text PRIMARY KEY,"
                + "  " + HISTORY_STAMP + " integer,"
                + "  " + HISTORY_TAG + " text,"
                + "  " + HISTORY_WATCHED + " integer);";


        db.execSQL(QUERY1);
        db.execSQL(QUERY2);
        db.execSQL(QUERY3);
        db.execSQL(QUERY4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String QUERY1 = "drop table if exists " + TABLE_LIBRARY;
        db.execSQL(QUERY1);
        String QUERY2 = "drop table if exists " + TABLE_VIDEOS;
        db.execSQL(QUERY2);
        String QUERY3 = "drop table if exists " + TABLE_TAGS;
        db.execSQL(QUERY3);
        String QUERY4 = "drop table if exists " + TABLE_HISTORY;
        db.execSQL(QUERY4);
        onCreate(db);
    }
}
