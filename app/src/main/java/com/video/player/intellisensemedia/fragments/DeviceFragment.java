package com.video.player.intellisensemedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.adapter.MediaAdapter;
import com.video.player.intellisensemedia.entity.main.Media;
import com.video.player.intellisensemedia.interfaces.OnReload;
import com.video.player.intellisensemedia.utils.DeviceFetcher;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceFragment extends Fragment implements OnReload {

    private ProgressBar progressBar;
    private TextView errorText;
    private RecyclerView recyclerView;
    private MediaAdapter adapter;

    public DeviceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_device, container, false);
        initViews(view);
        doReload();
        return view;
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MediaAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void doReload() {

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        DeviceFetcher deviceFetcher = new DeviceFetcher(getContext());
        ArrayList<Media> media = deviceFetcher.fetchMedia();

        progressBar.setVisibility(View.GONE);

        if (media == null || media.size() == 0) {
            errorText.setVisibility(View.VISIBLE);
            adapter.removeAll();
        } else {
            adapter.setVideos(media);
        }

    }
}
