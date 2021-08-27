package com.video.player.intellisensemedia.interfaces;

import com.video.player.intellisensemedia.entity.main.Video;
import java.util.HashMap;
import java.util.Set;

public interface OnCompleteMap {

    public void onComplete(HashMap<String, Set<Video>> map);

}
