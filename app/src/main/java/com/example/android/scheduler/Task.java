package com.example.android.scheduler;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class Task implements Serializable {

    private String title;
    private long lastCompleted;
    private long lastPostponed;

    public Task(String title){
        this.title = title;
        lastCompleted = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        lastPostponed = lastCompleted;
    }
    public Task(String title, long lastCompleted, long lastPostponed){
        this.title = title;
        this.lastPostponed = lastPostponed;
        this.lastCompleted = lastCompleted;
    }

    public String getTitle() {
        return title;
    }

    public int daysSinceLastAttempt() {
        return SchedulerUtils.calcDaysAgo(Math.max(lastCompleted, lastPostponed));
    }

    public int daysSinceLastCompleted() {
        return SchedulerUtils.calcDaysAgo(lastCompleted);
    }

    public long daysSinceLastPostponed() {
        return SchedulerUtils.calcDaysAgo(lastPostponed);
    }

    public void setLastCompleted(long newTime) {lastCompleted = newTime;}

    public void setLastPostponed(long newTime) {lastPostponed = newTime;}

    public void setTitle(String newTitle){ title = newTitle;}

}
