package com.example.android.scheduler;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScheduleAdaptor extends RecyclerView.Adapter<ScheduleAdaptor.TaskViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private ArrayList<Task> mTasks;
    private Context mContext;

    public ScheduleAdaptor(ListItemClickListener listener, ArrayList<Task> taskList, Context context){
        mTasks = taskList;
        mOnClickListener = listener;
        mContext = context;
    }

    public interface ListItemClickListener{
        void ListItemClicked(int clickedItemIndex);
    }

    public void taskCompleted(int pos, RecyclerView.ViewHolder viewHolder){
        Task completed = mTasks.get(pos);
        completed.setLastCompleted(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()));
        mTasks.remove(pos);
        notifyItemRemoved(pos);
        mTasks.add(completed);
        notifyItemInserted(mTasks.size()-1);
        SchedulerPrefs.storeTaskList(mTasks, mContext);
        showUndoSnackbar(viewHolder);
    }

    public void taskPostponed(int pos){
        notifyItemRemoved(pos);
    }

    public void showUndoSnackbar(RecyclerView.ViewHolder viewHolder){
        Snackbar.make(viewHolder.itemView, "Snackbar!", Snackbar.LENGTH_LONG)
                .show();
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
        holder.bind(mTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void setmTasks(ArrayList<Task> tasks){
        mTasks = tasks;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTaskTitleText;
        ImageView mCompletedImage;
        ImageView mPostponedImage;
        TextView mCompletedDateText;
        TextView mAttemptedDateText;

        public TaskViewHolder(View taskView){
            super(taskView);
            mTaskTitleText = (TextView) taskView.findViewById(R.id.task_title_tv);
            mCompletedDateText = (TextView) taskView.findViewById(R.id.task_lastComplete_tv);
            mAttemptedDateText = (TextView) taskView.findViewById(R.id.task_lastAttempted_tv);

            taskView.setOnClickListener(this);
        }

        void bind (Task task){
            mTaskTitleText.setText(task.getTitle());
            mCompletedDateText.setText(String.valueOf(task.daysSinceLastCompleted()) + " days");
            mAttemptedDateText.setText(String.valueOf(task.daysSinceLastAttempt()) + " days");
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.ListItemClicked(clickedPosition);
        }
    }

}
