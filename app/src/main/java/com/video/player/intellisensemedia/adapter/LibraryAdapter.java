package com.video.player.intellisensemedia.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.player.intellisensemedia.LibraryItemsActivity;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteLibrary;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteVideos;
import com.video.player.intellisensemedia.fragments.LibraryFragment;
import com.video.player.intellisensemedia.interfaces.OnInput;
import com.video.player.intellisensemedia.utils.DialogHelper;
import com.video.player.intellisensemedia.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {

    private ArrayList<SQLiteLibrary> libraries = new ArrayList<>();
    private LibraryFragment fragment;

    public LibraryAdapter(LibraryFragment fragment) {
        this.fragment = fragment;
    }

    public void addLibrary(SQLiteLibrary library) {
        libraries.add(library);
        notifyDataSetChanged();
    }

    public void setLibraries(ArrayList<SQLiteLibrary> libraries) {
        this.libraries = libraries;
        notifyDataSetChanged();
    }

    public void removeAll() {
        libraries.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(fragment.getContext()).inflate(R.layout.item_library, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //SET NAME
        holder.name.setText(libraries.get(position).getName());
        holder.name.setSelected(true);

        //SET STAMP
        holder.stamp.setText(Utils.getFormattedStamp(libraries.get(position).getId()));
        holder.stamp.setSelected(true);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Are You Sure Want to Delete Library - " + libraries.get(position).getName() + "?";

                //CONFIRMATION DIALOG
                DialogHelper helper = new DialogHelper(fragment.getContext());
                helper.confirmationDialog(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteLibrary(position);
                    }
                });
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EDIT DIALOG
                DialogHelper helper = new DialogHelper(fragment.getContext());
                helper.showInputDialog("Update", new OnInput() {
                    @Override
                    public void onInput(String data) {
                        updateLibrary(data, position);
                    }
                });
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(fragment.getContext(), LibraryItemsActivity.class);
                intent.putExtra(LibraryItemsActivity.LIBRARY_OBJECT, libraries.get(position));
                fragment.startActivity(intent);

            }
        });
    }

    private void deleteLibrary(int position) {
        SQLiteLibrary liteLibrary = libraries.get(position);
        SQLiteLibrary.delete(fragment.getContext(), liteLibrary.getId());

        SQLiteVideos.deleteByLibrary(fragment.getContext(), liteLibrary.getId());

        fragment.doReload();

        Toast.makeText(fragment.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
    }

    private void updateLibrary(String data, int position) {
        //CHECK CONSTRAINTS
        if (data == null) {
            Toast.makeText(fragment.getContext(), "Error While Input...", Toast.LENGTH_SHORT).show();
            return;
        } else if (data.isEmpty()) {
            Toast.makeText(fragment.getContext(), "Empty Name...", Toast.LENGTH_SHORT).show();
            return;
        } else if (data.equals(libraries.get(position).getName())) {
            Toast.makeText(fragment.getContext(), "Same Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        //---------------

        SQLiteLibrary liteLibrary = libraries.get(position);
        liteLibrary.setName(data);
        liteLibrary.update(fragment.getContext());

        fragment.doReload();

        Toast.makeText(fragment.getContext(), "Updated Successfully...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return libraries == null ? 0 : libraries.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView stamp, name;
        ImageView delete, edit;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            stamp = itemView.findViewById(R.id.stamp);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }
    }

}
