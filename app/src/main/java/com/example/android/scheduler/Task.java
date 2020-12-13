package com.example.android.scheduler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Task implements Serializable {

    private String title;
    private long lastCompleted;
    private long lastPostponed;
    private ArrayList<Integer> historyLast10;

    public Task(String title){
        this.title = title;
        lastCompleted = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        lastPostponed = lastCompleted;
        historyLast10 = new ArrayList<>();
    }
    public Task(String title, long lastCompleted, long lastPostponed){
        this.title = title;
        this.lastPostponed = lastPostponed;
        this.lastCompleted = lastCompleted;
        historyLast10 = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public int daysSinceLastAttempt() {
        return SchedulerUtils.calcDaysAgo(Math.max(lastCompleted, lastPostponed));
    }

    public void updateHistory(int result){
        historyLast10.add(0, result);
        if (historyLast10.size() > 10) historyLast10.remove(historyLast10.size() - 1);
    }

    public void updateHistoryUndo(){
        historyLast10.remove(0);
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

    public long getLastCompleted() {return lastCompleted;}

    public long getLastPostponed() {return lastPostponed;}

    public ArrayList<Integer> getHistoryLast10(){return historyLast10;}
}
