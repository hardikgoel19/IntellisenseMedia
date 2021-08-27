package com.video.player.intellisensemedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.adapter.LibraryAdapter;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteLibrary;
import com.video.player.intellisensemedia.interfaces.OnInput;
import com.video.player.intellisensemedia.interfaces.OnReload;
import com.video.player.intellisensemedia.utils.DialogHelper;
import com.video.player.intellisensemedia.utils.Utils;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryFragment extends Fragment implements OnReload, View.OnClickListener {

    private ProgressBar progressBar;
    private TextView errorText;
    private RecyclerView recyclerView;
    private LibraryAdapter adapter;
    private FloatingActionButton floatingActionButton;

    public LibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_library, container, false);
        initViews(view);
        doReload();
        return view;
    }


    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.addLibrary);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibraryAdapter(this);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void doReload() {

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        //FETCH LIBRARIES NAME
        ArrayList<SQLiteLibrary> liteLibraries = SQLiteLibrary.getAll(getContext());

        progressBar.setVisibility(View.GONE);

        //NOW CHECK FOR CONDITIONS AND SET DATA TO ADAPTER
        if (liteLibraries.size() == 0) {
            errorText.setVisibility(View.VISIBLE);
            adapter.removeAll();
        } else {
            adapter.setLibraries(liteLibraries);
        }
    }

    @Override
    public void onClick(View view) {
        DialogHelper helper = new DialogHelper(getContext());
        helper.showInputDialog("Add", new OnInput() {
            @Override
            public void onInput(String data) {
                addLibrary(data);
            }
        });
    }

    private void addLibrary(String data) {
        //CHECK CONSTRAINTS
        if (data == null) {
            Toast.makeText(getContext(), "Error While Input...", Toast.LENGTH_SHORT).show();
            return;
        } else if (data.isEmpty()) {
            Toast.makeText(getContext(), "Empty Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        //---------------

        SQLiteLibrary liteLibrary = new SQLiteLibrary();
        liteLibrary.setName(data);
        liteLibrary.setId(Utils.getCurrentStamp());
        SQLiteLibrary.add(getContext(),liteLibrary);

        doReload();

        Toast.makeText(getContext(), "Added Successfully...", Toast.LENGTH_SHORT).show();

    }
}
