package com.video.player.intellisensemedia.entity.main;

import java.io.Serializable;

public class Media implements Serializable {

    private String title,path;
    private long length,stamp;

    public Media() {
    }

    public Media(String title, String path, long length, long stamp) {
        this.title = title;
        this.path = path;
        this.length = length;
        this.stamp = stamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

}
