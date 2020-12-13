package com.example.android.scheduler;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
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
        long prevCompletedDate = completed.getLastCompleted();
        completed.setLastCompleted(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()));
        mTasks.remove(pos);
        notifyItemRemoved(pos);
        mTasks.add(completed);
        completed.updateHistory(1);

        notifyItemInserted(mTasks.size()-1);
        SchedulerPrefs.storeTaskList(mTasks, mContext);
        showCompletedSnackbar(viewHolder, completed, pos, prevCompletedDate);
    }

    public void showCompletedSnackbar(RecyclerView.ViewHolder viewHolder, Task completed, int pos, long prevCompletedDate){
        Snackbar.make(viewHolder.itemView, R.string.task_completed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_label, new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        notifyItemRemoved(mTasks.size() - 1);
                        mTasks.remove(mTasks.size() - 1);
                        completed.updateHistoryUndo();

                        completed.setLastCompleted(prevCompletedDate);
                        mTasks.add(pos, completed);
                        notifyItemInserted(pos);
                        SchedulerPrefs.storeTaskList(mTasks, mContext);
                    }
                })
                .setDuration(5000)
                .show();
    }

    public void taskPostponed(int pos, RecyclerView.ViewHolder viewHolder){
        Task postponed = mTasks.get(pos);
        long prevPostponedDate = postponed.getLastPostponed();
        postponed.setLastPostponed(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()));
        postponed.updateHistory(0);

        notifyItemRemoved(pos);
        notifyItemInserted(SchedulerUtils.updateList(mTasks, postponed));
        SchedulerPrefs.storeTaskList(mTasks, mContext);
        showPostponedSnackbar(viewHolder, postponed, pos, prevPostponedDate);
    }

    public void showPostponedSnackbar(RecyclerView.ViewHolder viewHolder, Task postponed, int pos, long prevPostponedDate){
        Snackbar.make(viewHolder.itemView, R.string.task_postponed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_label, new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        postponed.setLastPostponed(prevPostponedDate);
                        notifyItemRemoved(mTasks.indexOf(postponed));
                        mTasks.remove(postponed);
                        mTasks.add(pos, postponed);
                        postponed.updateHistoryUndo();
                        notifyItemInserted(pos);
                        SchedulerPrefs.storeTaskList(mTasks, mContext);
                    }
                })
                .setDuration(5000)
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
        ArrayList<ImageView> mLast5Images = new ArrayList<ImageView>();
        TextView mCompletedDateText;
        TextView mAttemptedDateText;

        public TaskViewHolder(View taskView){
            super(taskView);
            mTaskTitleText = (TextView) taskView.findViewById(R.id.task_title_tv);
            mCompletedDateText = (TextView) taskView.findViewById(R.id.task_lastComplete_tv);
            mAttemptedDateText = (TextView) taskView.findViewById(R.id.task_lastAttempted_tv);
            mLast5Images.add((ImageView) taskView.findViewById(R.id.last5_iv1));
            mLast5Images.add((ImageView) taskView.findViewById(R.id.last5_iv2));
            mLast5Images.add((ImageView) taskView.findViewById(R.id.last5_iv3));
            mLast5Images.add((ImageView) taskView.findViewById(R.id.last5_iv4));
            mLast5Images.add((ImageView) taskView.findViewById(R.id.last5_iv5));

            taskView.setOnClickListener(this);
        }


        //TODO refactor computation out of bind?
        void bind (Task task){
            mTaskTitleText.setText(task.getTitle());
            int lastCompleted = task.daysSinceLastCompleted();
            Resources res = mContext.getResources();

            if (lastCompleted == 0){ mCompletedDateText.setText(R.string.today_label);}
            else{ mCompletedDateText.setText(res.getQuantityString(R.plurals.numberOfDays, lastCompleted, lastCompleted));}

            int lastAttempted = task.daysSinceLastAttempt();
            if (lastAttempted == 0){ mAttemptedDateText.setText(R.string.today_label);}
            else{ mAttemptedDateText.setText(res.getQuantityString(R.plurals.numberOfDays, lastAttempted, lastAttempted));}
            int i = 0;
            ArrayList<Integer> history = task.getHistoryLast10();
            for (ImageView image : mLast5Images){
                if (history == null || history.size() <= i){
                    break;
                }
                if (history.get(i) == 0){
                    image.setBackgroundResource(R.drawable.ic_clock);
                }
                else {
                    image.setBackgroundResource(R.drawable.ic_tick);
                }
                i++;
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.ListItemClicked(clickedPosition);
        }
    }

}
