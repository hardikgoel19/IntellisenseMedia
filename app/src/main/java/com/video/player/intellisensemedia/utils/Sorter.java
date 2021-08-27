package com.video.player.intellisensemedia.utils;

import android.content.Context;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.interfaces.OnCompleteList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class Sorter {

    private Context context;

    public Sorter(Context context) {
        this.context = context;
    }

    public void doArrange(Map<String, Set<Video>> map, OnCompleteList onCompleteList) {

        Set<String> tags = map.keySet();
        ArrayList<Video> finalList = new ArrayList<>();

        for (String tag : tags) {
            if (map.get(tag) == null) continue;
            ArrayList<Video> list = new ArrayList<>(map.get(tag));
            Collections.sort(list, new Comparator<Video>() {
                @Override
                public int compare(Video video, Video t1) {
                    return (int) (t1.getStamp() - video.getStamp());
                }
            });
            finalList.addAll(list);
        }

        ArrayList<Video> deleteIndex = new ArrayList<>();
        for (int i = 0; i < finalList.size(); i++) {
            for (int j = i + 1; j < finalList.size(); j++) {
                if (finalList.get(i).getId().equalsIgnoreCase(finalList.get(j).getId())) {
                    deleteIndex.add(finalList.get(j));
                }
            }
        }

        for(Video index : deleteIndex){
            finalList.remove(index);
        }

        onCompleteList.onComplete(finalList);
    }
}
