package com.video.player.intellisensemedia;

import android.os.Bundle;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteHistory;
import com.video.player.intellisensemedia.entity.sqlite.SQLiteTags;
import com.video.player.intellisensemedia.utils.Utils;
import com.video.player.intellisensevideoview.view.IntelligentVideoView;
import androidx.annotation.Nullable;

public class PlayerActivity extends IntelligentVideoView {

    public static final String IDENTIFIER_OBJECT = "object";
    public static final String IDENTIFIER_TYPE = "type";

    public static final String TYPE_SAVE = "save";
    public static final String TYPE_NO_SAVE = "no-save";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setClass(this);
    }

    @Override
    protected void onPause() {

        super.onPause();

        //GET WHETHER TO SAVE OR NOT
        String temp = getIntent().getStringExtra(IDENTIFIER_TYPE);
        boolean isSave = temp != null && temp.equals(TYPE_SAVE);

        if (isSave) {
            //GET CURRENTLY PLAYING MEDIA
            Video entity = (Video) getIntent().getSerializableExtra(IDENTIFIER_OBJECT);

            if (entity != null) {

                SQLiteHistory history = new SQLiteHistory();
                history.setStamp(Utils.getCurrentStamp());
                history.setWatched(getCurrentPosition());
                history.setVideoID(entity.getId());
                history.setTag(entity.getRootTag());
                SQLiteHistory.addOrUpdate(this, history);

                SQLiteTags.addOrUpdate(this, entity.getRootTag());

                if (entity.getOtherTags() == null) return;
                String[] tags = entity.getOtherTags().split(",");
                for (String tag : tags) {
                    if (!tag.trim().isEmpty())
                        SQLiteTags.addOrUpdate(this, tag.trim());
                }
            }
        }
    }
}
