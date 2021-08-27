package com.video.player.intellisensemedia.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.video.player.intellisensemedia.R;
import com.video.player.intellisensemedia.entity.main.Media;
import com.video.player.intellisensemedia.entity.main.Video;

public class DetailsDialog {

    private Context context;

    public DetailsDialog(Context context) {
        this.context = context;
    }

    public void showDetails(Video video) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details,null,false);
        LinearLayout table = view.findViewById(R.id.table);

        table.addView(getRow("Title : ",video.getTitle()));
        table.addView(getRow("Description : ",video.getDescription()));
        table.addView(getRow("Length : ",Utils.getFormattedDuration(video.getLength())));
        table.addView(getRow("Date-Time : ",Utils.getFormattedStamp(video.getStamp())));
        table.addView(getRow("Primary Tag : ",video.getRootTag()));
        table.addView(getRow("Secondary Tag : ",video.getOtherTags()));

        ImageView close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }

    public void showDetails(Media media) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details,null,false);
        LinearLayout table = view.findViewById(R.id.table);

        table.addView(getRow("Title : ",media.getTitle()));
        table.addView(getRow("Primary Tag : ",media.getPath()));
        table.addView(getRow("Length : ",Utils.getFormattedDuration(media.getLength())));
        table.addView(getRow("Date-Time : ",Utils.getFormattedStamp(media.getStamp())));

        ImageView close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }

    private View getRow(String head,String content){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details_row,null,false);
        TextView h = view.findViewById(R.id.head);
        TextView c = view.findViewById(R.id.detail);
        h.setText(head);
        c.setText(content);
        return view;
    }
}
