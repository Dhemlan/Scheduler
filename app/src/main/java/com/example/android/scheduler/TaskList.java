package com.example.android.scheduler;

import java.util.ArrayList;
import java.util.List;

public class  TaskList {
    private ArrayList<Task> mTasks = new ArrayList<Task>();

    public int addTask(Task addedTask){
        int pos = 0;
        for (Task task : mTasks){

        }
        mTasks.add(pos, addedTask);
        return pos;
    }

    public void removeTask(int position){
        mTasks.remove(position);
    }

    public List<Task> getList(){
        return mTasks;
    }

    public int getSize(){
        return mTasks.size();
    }
}
