package com.example.kevinwang.showerfy;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by josep on 9/17/2016.
 */
public class IndividualRecord {
    public String date, songTitle, timeStr, points, timeNum;

    public IndividualRecord(String d, String s, String t, String p) {
        date = d;

        songTitle = s;
        int ms = Integer.parseInt(t);
        timeNum = t;
        timeStr = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
        );

        points = p + "pts";
    }
}
