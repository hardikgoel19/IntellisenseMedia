package com.video.player.intellisensemedia.entity.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class SQLiteLibrary implements SQLiteDetails, Serializable {

    private String name;
    private long id;

    public static void add(Context context, SQLiteLibrary sqLiteLibrary) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(LIBRARY_ID, sqLiteLibrary.getId());
                contentValues.put(LIBRARY_NAME, sqLiteLibrary.getName());
                database.insert(TABLE_LIBRARY, null, contentValues);
            }
        }
    }

    public void update(Context context) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(LIBRARY_NAME, getName());
                database.update(TABLE_LIBRARY, contentValues, LIBRARY_ID + " = ?", new String[]{String.valueOf(getId())});
            }
        }
    }

    public static ArrayList<SQLiteLibrary> getAll(Context context) {
        ArrayList<SQLiteLibrary> list = new ArrayList<>();
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_LIBRARY, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteLibrary library = new SQLiteLibrary();
                        library.setId(cursor.getLong(cursor.getColumnIndex(LIBRARY_ID)));
                        library.setName(cursor.getString(cursor.getColumnIndex(LIBRARY_NAME)));
                        list.add(library);
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    public static void delete(Context context, long libraryID) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_LIBRARY, LIBRARY_ID + " = ?", new String[]{String.valueOf(libraryID)});
            }
        }
    }

    public SQLiteLibrary() {
    }

    public SQLiteLibrary(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SQLiteLibrary{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
