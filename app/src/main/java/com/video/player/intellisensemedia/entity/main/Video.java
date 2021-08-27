package com.video.player.intellisensemedia.entity.main;

import java.io.Serializable;

public class Video implements Serializable {

    private String path, title, rootTag, otherTags, id, description;
    private long stamp, views, length,watched;

    public Video() {
    }

    public Video(String path, String title, String rootTag, String otherTags, String id, String description, long stamp, long views, long length, long watched) {
        this.path = path;
        this.title = title;
        this.rootTag = rootTag;
        this.otherTags = otherTags;
        this.id = id;
        this.description = description;
        this.stamp = stamp;
        this.views = views;
        this.length = length;
        this.watched = watched;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRootTag() {
        return rootTag;
    }

    public void setRootTag(String rootTag) {
        this.rootTag = rootTag;
    }

    public String getOtherTags() {
        return otherTags;
    }

    public void setOtherTags(String otherTags) {
        this.otherTags = otherTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getWatched() {
        return watched;
    }

    public void setWatched(long watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Video{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", rootTag='" + rootTag + '\'' +
                ", otherTags='" + otherTags + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", stamp=" + stamp +
                ", views=" + views +
                ", length=" + length +
                ", watched=" + watched +
                '}';
    }
}
