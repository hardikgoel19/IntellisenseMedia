package com.video.player.intellisensemedia.entity.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLiteTags implements SQLiteDetails {

    private String tag;
    private int hits;

    public static void add(Context context, SQLiteTags sqLiteTags) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TAGS_TAG, sqLiteTags.getTag());
                contentValues.put(TAGS_HITS, sqLiteTags.getHits());
                database.insert(TABLE_TAGS, null, contentValues);
            }
        }
    }

    public static boolean isTagExist(Context context, String tag) {
        boolean exists = false;
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_TAGS + " where " + TAGS_TAG + " = ?", new String[]{tag});
                exists = cursor != null && cursor.moveToNext();
                if (cursor != null)
                    cursor.close();
            }
        }
        return exists;
    }

    private static int getHits(Context context, String tag) {
        int hits = 0;
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_TAGS + " where " + TAGS_TAG + " = ?", new String[]{tag});
                if (cursor != null && cursor.moveToNext()) {
                    hits = cursor.getInt(cursor.getColumnIndex(TAGS_HITS));
                    cursor.close();
                }
            }
        }
        return hits;
    }

    public static void addOrUpdate(Context context, String tag) {
        if (isTagExist(context, tag)) {

            int oldHits = getHits(context, tag);

            try (SQLite sqLite = new SQLite(context)) {
                try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TAGS_HITS, oldHits + 1);
                    database.update(TABLE_TAGS, contentValues, TAGS_TAG + " = ?", new String[]{tag});
                }
            }

        } else {

            try (SQLite sqLite = new SQLite(context)) {
                try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TAGS_TAG, tag);
                    contentValues.put(TAGS_HITS, 1);
                    database.insert(TABLE_TAGS, null, contentValues);
                }
            }

        }
    }

    public static ArrayList<SQLiteTags> getAllByHitsDesc(Context context) {
        ArrayList<SQLiteTags> list = new ArrayList<>();
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_TAGS + " order by " + TAGS_HITS + " DESC", null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteTags tags = new SQLiteTags();
                        tags.setTag(cursor.getString(cursor.getColumnIndex(TAGS_TAG)));
                        tags.setHits(cursor.getInt(cursor.getColumnIndex(TAGS_HITS)));
                        list.add(tags);
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    public static void delete(Context context, SQLiteTags sqLiteTags) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_TAGS, TAGS_TAG + " = ? and " + TAGS_HITS + " = ?", new String[]{sqLiteTags.getTag(), String.valueOf(sqLiteTags.getHits())});
            }
        }
    }

    public SQLiteTags() {
    }

    public SQLiteTags(String tag, int hits) {
        this.tag = tag;
        this.hits = hits;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "SQLiteTags{" +
                "tag='" + tag + '\'' +
                ", hits=" + hits +
                '}';
    }
}
