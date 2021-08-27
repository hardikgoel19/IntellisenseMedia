package com.video.player.intellisensemedia.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Date;

public class Utils {

    public static String getFormattedDuration(long duration) {
        int temp = (int) (duration / 1000);
        int minutes = temp / 60;
        int seconds = temp % 60;
        return String.format("%02d : %02d", minutes, seconds);
    }

    public static String getFormattedStamp(long stamp) {
        Date date = new Date();
        long time = date.getTime() - stamp;
        int seconds = (int) (time / 1000);
        int minutes = seconds / 60;
        int hrs = minutes / 60;
        int days = hrs / 24;
        if (minutes == 0 || minutes == 1)
            return "Just Now";
        else if (minutes >= 2 && minutes <= 59)
            return minutes + " min ago";
        else if (hrs >= 1 && hrs <= 23)
            return hrs + " hours ago";
        else
            return days + " days ago";
    }

    public static long getCurrentStamp() {
        return System.currentTimeMillis();
    }

    public static String getFormattedTag(String path) {
        path = path.trim().toLowerCase();
        String fina = "";
        for (char ch : path.toCharArray()) fina = fina + ch + "/";
        return fina;
    }
}
