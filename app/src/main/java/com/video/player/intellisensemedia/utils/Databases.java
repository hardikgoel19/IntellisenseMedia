package com.video.player.intellisensemedia.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.video.player.intellisensemedia.entity.sqlite.SQLiteDetails;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteHistory;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteLibrary;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteTags;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteVideos;

import java.util.ArrayList;

public class Databases {

    private Context context;

    public Databases(Context context) {
        this.context = context;
    }

    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] items = {SQLiteDetails.TABLE_VIDEOS,
                SQLiteDetails.TABLE_LIBRARY,
                SQLiteDetails.TABLE_TAGS,
                SQLiteDetails.TABLE_HISTORY};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i){
                    case 0:
                        showVideos();
                        break;
                    case 1:
                        showLibrary();
                        break;
                    case 2:
                        showTags();
                        break;
                    case 3:
                        showHistory();
                        break;
                }


            }
        });
        builder.create().show();
    }

    private void showHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);

        ArrayList<SQLiteHistory> histories = SQLiteHistory.getAllByStampAsc(context);
        ArrayList<String> data = new ArrayList<>();
        for(SQLiteHistory history : histories) data.add(history.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        builder.setView(listView);
        builder.create().show();
    }

    private void showLibrary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);

        ArrayList<SQLiteLibrary> libraries = SQLiteLibrary.getAll(context);
        ArrayList<String> data = new ArrayList<>();
        for(SQLiteLibrary library : libraries) data.add(library.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        builder.setView(listView);
        builder.create().show();
    }

    private void showTags() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);

        ArrayList<SQLiteTags> tags = SQLiteTags.getAllByHitsDesc(context);
        ArrayList<String> data = new ArrayList<>();
        for(SQLiteTags tag : tags) data.add(tag.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        builder.setView(listView);
        builder.create().show();
    }

    private void showVideos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);

        ArrayList<SQLiteVideos> videos = SQLiteVideos.getAll(context);
        ArrayList<String> data = new ArrayList<>();
        for(SQLiteVideos videos1 : videos) data.add(videos1.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        builder.setView(listView);
        builder.create().show();
    }
}
