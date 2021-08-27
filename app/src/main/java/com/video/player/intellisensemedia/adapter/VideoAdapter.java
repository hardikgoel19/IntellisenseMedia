package com.video.player.intellisensemedia.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.video.player.intellisensemedia.PlayerActivity;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteLibrary;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteVideos;
import com.video.player.intellisensemedia.utils.DetailsDialog;
import com.video.player.intellisensemedia.utils.DialogHelper;
import com.video.player.intellisensemedia.utils.Utils;
import com.video.player.intellisensevideoview.utils.VideoViewParams;
import com.video.player.intellisensevideoview.utils.VideoViewSettings;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private ArrayList<Video> entities = new ArrayList<>();
    private Context context;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    public void addVideo(Video video) {
        entities.add(video);
        notifyDataSetChanged();
    }

    public void setVideos(ArrayList<Video> videos) {
        entities = videos;
        notifyDataSetChanged();
    }

    public void removeAll() {
        entities.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //SET TITLE
        holder.title.setText(entities.get(position).getTitle());

        //SET STAMP
        holder.stamp.setText(Utils.getFormattedStamp(entities.get(position).getStamp()));

        //SET DURATION
        holder.duration.setText(Utils.getFormattedDuration(entities.get(position).getLength()));

        //SET THUMBNAIL
        Glide.with(context).load(entities.get(position).getPath()).into(holder.thumbnail);

        //LOAD PROGRESS
        if (entities.get(position).getLength() == 0 || entities.get(position).getWatched() == 0) {
            holder.progress.setVisibility(View.GONE);
        } else {
            int percent = (int) ((entities.get(position).getWatched() * 100) / entities.get(position).getLength());
            holder.progress.setMax(100);
            holder.progress.setProgress(percent);
            holder.progress.setVisibility(View.VISIBLE);
        }

        //SHOW POPUP MENU
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_video_more_type_online, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.add_to_library:
                                addToLibrary(position);
                                break;
                            case R.id.details:
                                showDetails(position);
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //SET ON CLICK LISTENER
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(VideoViewParams.VIDEO_PATH_URI,entities.get(position).getPath());
                intent.putExtra(VideoViewParams.VIDEO_SEEK_TO,entities.get(position).getWatched());
                intent.putExtra(VideoViewParams.VIDEO_TITLE,entities.get(position).getTitle());
                intent.putExtra(VideoViewParams.VIDEO_VIEW_SETTINGS,getSettings());
                intent.putExtra(PlayerActivity.IDENTIFIER_OBJECT, entities.get(position));
                intent.putExtra(PlayerActivity.IDENTIFIER_TYPE, PlayerActivity.TYPE_SAVE);
                context.startActivity(intent);

            }
        });
    }

    private VideoViewSettings getSettings() {
        //TODO : RETURN SETTINGS
        return new VideoViewSettings();
    }

    private void showDetails(int position) {
        DetailsDialog detailsDialog = new DetailsDialog(context);
        detailsDialog.showDetails(entities.get(position));
    }

    private void addToLibrary(final int position) {

        final ArrayList<SQLiteLibrary> liteLibraries = SQLiteLibrary.getAll(context);
        ArrayList<String> names = new ArrayList<>();
        for (SQLiteLibrary liteLibrary : liteLibraries) names.add(liteLibrary.getName());

        DialogHelper helper = new DialogHelper(context);
        helper.showListDialog(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SQLiteVideos videos = new SQLiteVideos();
                videos.setVideoID(entities.get(position).getId());
                videos.setLibraryID(liteLibraries.get(i).getId());
                videos.setTag(entities.get(position).getRootTag());
                SQLiteVideos.add(context, videos);

                Toast.makeText(context, "Added Successfully...", Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return entities != null ? entities.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        ImageView thumbnail, more;
        TextView title, duration, stamp;
        ProgressBar progress;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            stamp = itemView.findViewById(R.id.stamp);
            more = itemView.findViewById(R.id.more);
            item = itemView.findViewById(R.id.item);
            progress = itemView.findViewById(R.id.progress);
        }
    }

}
