package com.video.player.intellisensemedia.entity.sqlite;

public interface SQLiteDetails {

    //DB STUFF
    String DB_NAME = "libraryDatabase";
    int DB_VERSION = 1;

    //TABLE NAMES
    String TABLE_LIBRARY = "library";
    String TABLE_VIDEOS = "videos";
    String TABLE_TAGS = "tags";
    String TABLE_HISTORY = "history";

    //COLUMN NAMES (LIBRARY)
    String LIBRARY_NAME = "name";
    String LIBRARY_ID = "id";

    //COLUMN NAMES (VIDEOS)
    String VIDEOS_LIBRARY_ID = "libraryID";
    String VIDEOS_VIDEO_ID = "videoID";
    String VIDEOS_TAG = "tag";

    //COLUMN NAMES (TAGS)
    String TAGS_TAG = "tag";
    String TAGS_HITS = "hits";

    //COLUMN NAMES (HISTORY)
    String HISTORY_VIDEO_ID = "videoID";
    String HISTORY_WATCHED = "watched";
    String HISTORY_STAMP = "stamp";
    String HISTORY_TAG = "tag";

}
