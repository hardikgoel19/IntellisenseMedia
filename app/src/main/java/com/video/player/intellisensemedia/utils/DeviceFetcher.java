package com.video.player.intellisensemedia.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.video.player.intellisensemedia.entity.main.Media;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class DeviceFetcher {

    private Context context;
    private Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    public DeviceFetcher(Context context) {
        this.context = context;
    }

    public ArrayList<Media> fetchMedia() {
        ArrayList<Media> medias = new ArrayList<>();
        String[] projections = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.DURATION, MediaStore.Video.Media.MINI_THUMB_MAGIC};

        try (Cursor cursor = context.getContentResolver().query(uri, projections, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Media media = new Media();
                    media.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                    media.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                    media.setLength(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))));
                    media.setStamp(System.currentTimeMillis());
                    medias.add(media);
                }
            }
        } catch (Exception ignored) {}
        return medias;
    }

}
