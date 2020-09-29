package com.example.android.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdaptor extends RecyclerView.Adapter<ScheduleAdaptor.TaskViewHolder> {

    private int mItemCount;
    final private ListItemClickListener mOnClickListener;

    public ScheduleAdaptor(int itemCount, ListItemClickListener listener){
        mItemCount = itemCount;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener{
        void ListItemClicked(int clickedItemIndex);
    }

    @Override
    public ScheduleAdaptor.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_list_item, parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScheduleAdaptor.TaskViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }



    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTaskTitleText;

        public TaskViewHolder(View taskView){
            super(taskView);
            mTaskTitleText = (TextView) taskView.findViewById(R.id.task_title_tv);
            taskView.setOnClickListener(this);
        }

        void bind (int listIndex){
            mTaskTitleText.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.ListItemClicked(clickedPosition);
        }
    }
}
