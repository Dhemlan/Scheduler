package com.example.android.scheduler;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    private ScheduleAdaptor mAdaptor;

    public SwipeCallback(ScheduleAdaptor adaptor){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdaptor = adaptor;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            mAdaptor.taskCompleted(pos,viewHolder);
        }
        else if (direction == ItemTouchHelper.LEFT){
            mAdaptor.taskPostponed(pos, viewHolder);
        }
    }
}
