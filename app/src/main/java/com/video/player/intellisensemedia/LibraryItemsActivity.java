package com.video.player.intellisensemedia;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.video.player.intellisensemedia.adapter.VideoAdapter;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteLibrary;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteVideos;
import com.video.player.intellisensemedia.interfaces.OnCompleteList;
import com.video.player.intellisensemedia.utils.FirebaseFetcher;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryItemsActivity extends AppCompatActivity {

    public static final String LIBRARY_OBJECT = "library";

    private ProgressBar progressBar;
    private TextView errorText;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;

    private SQLiteLibrary liteLibrary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_items);

        liteLibrary = (SQLiteLibrary) getIntent().getSerializableExtra(LIBRARY_OBJECT);

        initViews();

        if (liteLibrary != null)
            loadFragment();
        else {
            errorText.setVisibility(View.VISIBLE);
            adapter.removeAll();
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(this);
        recyclerView.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //SET ACTION BAR ICON
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.icon);
            //SET ACTION BAR TITLE
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> " + liteLibrary.getName() + " </font>"));
        }
    }

    private void loadFragment() {

        ArrayList<SQLiteVideos> videos = SQLiteVideos.getAllByLibrary(this, liteLibrary.getId());

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        if (videos.size() == 0) {
            errorText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            adapter.removeAll();
            return;
        }

        final String lastID = videos.get(videos.size() - 1).getVideoID();
        FirebaseFetcher firebaseFetcher = new FirebaseFetcher(this);
        for (SQLiteVideos v : videos) {
            firebaseFetcher.fetchVideo(v.getVideoID(),v.getTag(), new OnCompleteList() {
                @Override
                public void onComplete(ArrayList<Video> videos) {
                    if (!(videos == null || videos.size() == 0)) {
                        adapter.addVideo(videos.get(0));
                        if (lastID.equals(videos.get(0).getId())) {
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
