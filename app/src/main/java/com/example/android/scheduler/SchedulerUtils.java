package com.example.android.scheduler;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {
    public static Long calcLastDate(int daysAgo){
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) - daysAgo;
    }
}
