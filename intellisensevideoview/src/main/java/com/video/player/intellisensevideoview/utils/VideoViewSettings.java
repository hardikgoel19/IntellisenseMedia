package com.video.player.intellisensevideoview.utils;

import java.io.Serializable;

public class VideoViewSettings implements Serializable {

    //READ ONLY VALUES (NO UPDATE)
    public int MIN_PLAYBACK_START_BUFFER = 1500;
    public int MIN_PLAYBACK_RESUME_BUFFER = 5000;

    private int minimumVideoLoadBuffer = 3000;
    private int maximumVideoLoadBuffer = 5000;
    private int pipWindowHeight = 180;
    private int pipWindowWidth = 240;
    private float eyesThreshold = 0.4f;
    private String pipWindowColor = "#00BFA5";
    private boolean isFaceDetection = true;
    private boolean isEyesDetection = true;
    private boolean isPIP = true;
    private boolean isTilt = true;

    public boolean isTilt() {
        return isTilt;
    }

    public void setTilt(boolean tilt) {
        isTilt = tilt;
    }

    public int getMinimumVideoLoadBuffer() {
        return minimumVideoLoadBuffer;
    }

    public void setMinimumVideoLoadBuffer(int minimumVideoLoadBuffer) {
        this.minimumVideoLoadBuffer = minimumVideoLoadBuffer;
    }

    public int getMaximumVideoLoadBuffer() {
        return maximumVideoLoadBuffer;
    }

    public void setMaximumVideoLoadBuffer(int maximumVideoLoadBuffer) {
        this.maximumVideoLoadBuffer = maximumVideoLoadBuffer;
    }

    public boolean isPIP() {
        return isPIP;
    }

    public void setPIP(boolean PIP) {
        isPIP = PIP;
    }

    public int getPipWindowHeight() {
        return pipWindowHeight;
    }

    public void setPipWindowHeight(int pipWindowHeight) {
        this.pipWindowHeight = pipWindowHeight;
    }

    public int getPipWindowWidth() {
        return pipWindowWidth;
    }

    public void setPipWindowWidth(int pipWindowWidth) {
        this.pipWindowWidth = pipWindowWidth;
    }

    public String getPipWindowColor() {
        return pipWindowColor;
    }

    public void setPipWindowColor(String pipWindowColor) {
        this.pipWindowColor = pipWindowColor;
    }

    public boolean isFaceDetection() {
        return isFaceDetection;
    }

    public void setFaceDetection(boolean faceDetection) {
        isFaceDetection = faceDetection;
    }

    public boolean isEyesDetection() {
        return isEyesDetection;
    }

    public void setEyesDetection(boolean eyesDetection) {
        isEyesDetection = eyesDetection;
    }

    public float getEyesThreshold() {
        return eyesThreshold;
    }

    public void setEyesThreshold(float eyesThreshold) {
        this.eyesThreshold = eyesThreshold;
    }
}
