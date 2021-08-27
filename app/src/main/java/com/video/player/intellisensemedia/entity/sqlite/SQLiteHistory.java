package com.video.player.intellisensemedia.entity.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLiteHistory implements SQLiteDetails {

    private String videoID,tag;
    private long stamp, watched;

    public static boolean isVideoExist(Context context, String videoID) {
        boolean exists = false;
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_HISTORY + " where " + HISTORY_VIDEO_ID + " = ?", new String[]{videoID});
                exists = cursor != null && cursor.moveToNext();
                if (cursor != null)
                    cursor.close();
            }
        }
        return exists;
    }

    public static void addOrUpdate(Context context, SQLiteHistory history) {
        if (isVideoExist(context, history.getVideoID())) {

            try (SQLite sqLite = new SQLite(context)) {
                try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(HISTORY_VIDEO_ID, history.getVideoID());
                    contentValues.put(HISTORY_WATCHED, history.getWatched());
                    contentValues.put(HISTORY_STAMP, history.getStamp());
                    contentValues.put(HISTORY_TAG, history.getTag());
                    database.update(TABLE_HISTORY, contentValues, HISTORY_VIDEO_ID + " = ?", new String[]{history.getVideoID()});
                }
            }

        } else {

            try (SQLite sqLite = new SQLite(context)) {
                try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(HISTORY_VIDEO_ID, history.getVideoID());
                    contentValues.put(HISTORY_WATCHED, history.getWatched());
                    contentValues.put(HISTORY_STAMP, history.getStamp());
                    contentValues.put(HISTORY_TAG, history.getTag());
                    database.insert(TABLE_HISTORY, null, contentValues);
                }
            }

        }

    }

    public static void deleteAll(Context context) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_HISTORY, null, null);
            }
        }
    }

    public static void delete(Context context, String VideoID) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_HISTORY, " where " + HISTORY_VIDEO_ID + " = ?", new String[]{VideoID});
            }
        }
    }

    public static ArrayList<SQLiteHistory> getAllByStampAsc(Context context) {
        ArrayList<SQLiteHistory> list = new ArrayList<>();
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_HISTORY + " order by " + HISTORY_STAMP + " ASC", null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteHistory tags = new SQLiteHistory();
                        tags.setVideoID(cursor.getString(cursor.getColumnIndex(HISTORY_VIDEO_ID)));
                        tags.setWatched(cursor.getInt(cursor.getColumnIndex(HISTORY_VIDEO_ID)));
                        tags.setStamp(cursor.getInt(cursor.getColumnIndex(HISTORY_STAMP)));
                        tags.setTag(cursor.getString(cursor.getColumnIndex(HISTORY_TAG)));
                        list.add(tags);
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    public SQLiteHistory() {
    }

    public SQLiteHistory(String videoID, String tag, long stamp, long watched) {
        this.videoID = videoID;
        this.tag = tag;
        this.stamp = stamp;
        this.watched = watched;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public long getWatched() {
        return watched;
    }

    public void setWatched(long watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "SQLiteHistory{" +
                "videoID='" + videoID + '\'' +
                ", tag='" + tag + '\'' +
                ", stamp=" + stamp +
                ", watched=" + watched +
                '}';
    }
}

