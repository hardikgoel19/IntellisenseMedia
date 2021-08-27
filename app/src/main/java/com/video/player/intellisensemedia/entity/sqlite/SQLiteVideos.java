package com.video.player.intellisensemedia.entity.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class SQLiteVideos implements SQLiteDetails {

    private long libraryID;
    private String videoID,tag;

    public static boolean isExists(Context context,SQLiteVideos sqLiteVideos){
        boolean exists = false;
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery(
                        "Select * from " + TABLE_VIDEOS + " where " + VIDEOS_VIDEO_ID + " = ? and " + VIDEOS_LIBRARY_ID + " = ? and " + VIDEOS_TAG + " = ?",
                        new String[]{sqLiteVideos.getVideoID(), String.valueOf(sqLiteVideos.getLibraryID()),sqLiteVideos.getTag()});
                exists = cursor != null && cursor.moveToNext();
                if (cursor != null)
                    cursor.close();
            }
        }
        return exists;
    }

    public static void add(Context context, SQLiteVideos sqLiteVideos) {
        if(isExists(context,sqLiteVideos)) return;
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(VIDEOS_LIBRARY_ID, sqLiteVideos.getLibraryID());
                contentValues.put(VIDEOS_VIDEO_ID, sqLiteVideos.getVideoID());
                contentValues.put(VIDEOS_TAG, sqLiteVideos.getTag());
                database.insert(TABLE_VIDEOS, null, contentValues);
            }
        }
    }

    public static ArrayList<SQLiteVideos> getAllByLibrary(Context context, long libraryID) {
        ArrayList<SQLiteVideos> list = new ArrayList<>();
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_VIDEOS + " where " + VIDEOS_LIBRARY_ID + " = ?", new String[]{String.valueOf(libraryID)});

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteVideos videos = new SQLiteVideos();
                        videos.setLibraryID(libraryID);
                        videos.setVideoID(cursor.getString(cursor.getColumnIndex(VIDEOS_VIDEO_ID)));
                        videos.setTag(cursor.getString(cursor.getColumnIndex(VIDEOS_TAG)));
                        list.add(videos);
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    public static ArrayList<SQLiteVideos> getAll(Context context) {
        ArrayList<SQLiteVideos> list = new ArrayList<>();
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getReadableDatabase()) {
                Cursor cursor = database.rawQuery("Select * from " + TABLE_VIDEOS, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        SQLiteVideos videos = new SQLiteVideos();
                        videos.setLibraryID(cursor.getInt(cursor.getColumnIndex(VIDEOS_LIBRARY_ID)));
                        videos.setVideoID(cursor.getString(cursor.getColumnIndex(VIDEOS_VIDEO_ID)));
                        videos.setTag(cursor.getString(cursor.getColumnIndex(VIDEOS_TAG)));
                        list.add(videos);
                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    public static void delete(Context context, SQLiteVideos sqLiteVideos) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_VIDEOS, VIDEOS_LIBRARY_ID + " = ? and " + VIDEOS_VIDEO_ID + " = ?", new String[]{String.valueOf(sqLiteVideos.getLibraryID()), sqLiteVideos.getVideoID()});
            }
        }
    }

    public static void deleteByLibrary(Context context,long id) {
        try (SQLite sqLite = new SQLite(context)) {
            try (SQLiteDatabase database = sqLite.getWritableDatabase()) {
                database.delete(TABLE_VIDEOS, VIDEOS_LIBRARY_ID + " = ?", new String[]{String.valueOf(id)});
            }
        }
    }

    public SQLiteVideos() {
    }

    public SQLiteVideos(long libraryID, String videoID, String tag) {
        this.libraryID = libraryID;
        this.videoID = videoID;
        this.tag = tag;
    }

    public long getLibraryID() {
        return libraryID;
    }

    public void setLibraryID(long libraryID) {
        this.libraryID = libraryID;
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

    @Override
    public String toString() {
        return "SQLiteVideos{" +
                "libraryID=" + libraryID +
                ", videoID='" + videoID + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
