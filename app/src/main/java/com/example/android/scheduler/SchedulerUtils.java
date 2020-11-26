package com.example.android.scheduler;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SchedulerUtils {
    public static Long calcLastDate(int daysAgo){
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) - daysAgo;
    }

    public static int calcDaysAgo(long lastDate){
        return (int) (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) - lastDate);
    }


    public static int updateList(ArrayList<Task> tasks, Task task){
        tasks.remove(task);
        int pos = 0;
        for (Task t : tasks){
            int taskLastAttempt = task.daysSinceLastAttempt();
            if (taskLastAttempt > t.daysSinceLastAttempt()) break;
            else {
                if (taskLastAttempt == t.daysSinceLastAttempt() && task.daysSinceLastCompleted() > t.daysSinceLastCompleted())
                    break;
                pos++;
                continue;
            }
        }
        tasks.add(pos, task);
        return pos;
    }
}
