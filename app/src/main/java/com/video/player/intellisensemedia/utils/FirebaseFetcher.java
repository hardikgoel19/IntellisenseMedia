package com.video.player.intellisensemedia.utils;

import android.content.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.video.player.intellisensemedia.entity.main.Video;
import com.video.player.intellisensemedia.interfaces.OnCompleteList;
import com.video.player.intellisensemedia.interfaces.OnCompleteMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import androidx.annotation.NonNull;

public class FirebaseFetcher {

    private Context context;

    public FirebaseFetcher(Context context) {
        this.context = context;
    }

    public void fetchInOrder(ArrayList<String> tags, final OnCompleteMap onComplete) {
        final HashMap<String, Set<Video>> map = new HashMap<>();

        tags = new ArrayList<>(new HashSet<>(tags));

        final String lastTag = tags.get(tags.size() - 1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        for (final String tag : tags) {
            if (tag != null && !tag.trim().isEmpty()) {
                reference.child(Utils.getFormattedTag(tag))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Set<Video> set = new LinkedHashSet<>();
                                if (map.containsKey(tag)) {
                                    set = map.get(tag);
                                }
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (set != null)
                                        set.add(ds.getValue(Video.class));
                                }
                                map.put(tag, set);

                                if (tag.equalsIgnoreCase(lastTag)) {
                                    onComplete.onComplete(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                onComplete.onComplete(new HashMap<String, Set<Video>>());
                            }
                        });
            }
        }
    }

    public void fetchVideo(final String id, String tag, final OnCompleteList onCompleteList) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Utils.getFormattedTag(tag))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            try {
                                if (snapshot1 != null && snapshot1.getKey() != null && snapshot1.getKey().equalsIgnoreCase(id)) {
                                    ArrayList<Video> videos = new ArrayList<>();
                                    videos.add(snapshot1.getValue(Video.class));
                                    onCompleteList.onComplete(videos);
                                }
                            } catch (Exception ignore) {
                                onCompleteList.onComplete(null);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onCompleteList.onComplete(null);
                    }
                });
    }

}
