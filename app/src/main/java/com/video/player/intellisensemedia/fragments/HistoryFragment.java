package com.video.player.intellisensemedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.adapter.VideoAdapter;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteHistory;
import com.video.player.intellisensemedia.interfaces.OnCompleteList;
import com.video.player.intellisensemedia.interfaces.OnReload;
import com.video.player.intellisensemedia.utils.FirebaseFetcher;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends Fragment implements OnReload {

    private ProgressBar progressBar;
    private TextView errorText;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_history, container, false);
        initViews(view);
        doReload();
        return view;
    }


    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void doReload() {

        adapter.removeAll();

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        final ArrayList<SQLiteHistory> histories = SQLiteHistory.getAllByStampAsc(getContext());

        if (histories.size() == 0) {
            errorText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        final String lastID = histories.get(histories.size() - 1).getVideoID();
        FirebaseFetcher firebaseFetcher = new FirebaseFetcher(getContext());
        for (final SQLiteHistory h : histories) {
            firebaseFetcher.fetchVideo(h.getVideoID(),h.getTag(), new OnCompleteList() {
                @Override
                public void onComplete(ArrayList<Video> videos) {
                    if (!(videos == null || videos.size() == 0)) {
                        Video video = videos.get(0);
                        video.setWatched(h.getWatched());
                        adapter.addVideo(video);
                        if (lastID.equals(videos.get(0).getId())) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
