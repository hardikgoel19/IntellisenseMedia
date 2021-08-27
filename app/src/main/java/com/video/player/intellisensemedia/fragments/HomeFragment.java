package com.video.player.intellisensemedia.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.adapter.VideoAdapter;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteTags;
import com.video.player.intellisensemedia.interfaces.OnCompleteList;
import com.video.player.intellisensemedia.interfaces.OnCompleteMap;
import com.video.player.intellisensemedia.interfaces.OnReload;
import com.video.player.intellisensemedia.utils.FirebaseFetcher;
import com.video.player.intellisensemedia.utils.Sorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements OnReload {

    private static final String DEFAULT_TAG = "@";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        initViews(view);
        doReload();
        return view;
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void doReload() {

        adapter.removeAll();

        progressBar.setVisibility(View.VISIBLE);

        //FETCH MAXIMUM TAG HITS
        ArrayList<SQLiteTags> tags = SQLiteTags.getAllByHitsDesc(getContext());

        ArrayList<String> tagsString = new ArrayList<>();
        for (SQLiteTags t : tags) tagsString.add(t.getTag());

        if (tagsString.size() == 0) {
            tagsString.add(DEFAULT_TAG);
        }
        Log.d("hardik", "doReload: ");

        //FETCH ALL VIDEOS FROM MAXIMUM TAG HITS
        FirebaseFetcher firebaseFetcher = new FirebaseFetcher(getContext());
        firebaseFetcher.fetchInOrder(tagsString, new OnCompleteMap() {
            @Override
            public void onComplete(HashMap<String, Set<Video>> map) {

                //SORT
                Sorter sort = new Sorter(getContext());
                sort.doArrange(map, new OnCompleteList() {
                    @Override
                    public void onComplete(ArrayList<Video> videos) {

                        progressBar.setVisibility(View.GONE);

                        //NOW CHECK FOR CONDITIONS AND SET DATA TO ADAPTER
                        if (videos == null || videos.size() == 0) {
                            Toast.makeText(getContext(), "No Videos Fetched", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.setVideos(videos);
                        }

                    }
                });

            }
        });

    }
}
