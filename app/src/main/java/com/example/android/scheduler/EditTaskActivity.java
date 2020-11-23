package com.example.android.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class EditTaskActivity extends AppCompatActivity {
    private ArrayList<Task> mTaskList;
    private Button mEditTaskButton;
    private Button mDeleteTaskButton;
    private EditText mTaskNameEditText;
    private NumberPicker mLastCompletedPicker;
    private NumberPicker mLastPostponedPicker;
    private Task mCurTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        mEditTaskButton = (Button) findViewById(R.id.edit_task_bt);
        mDeleteTaskButton = (Button) findViewById(R.id.edit_task_delete_bt);
        mTaskNameEditText = (EditText) findViewById(R.id.add_task_name_et);
        mLastCompletedPicker = (NumberPicker) findViewById(R.id.add_task_last_completed_np);
        mLastPostponedPicker = (NumberPicker) findViewById(R.id.add_task_last_postponed_np);

        mTaskList = SchedulerPrefs.retrieveTaskList(getApplicationContext());
        mLastCompletedPicker.setMaxValue(MainActivity.LAST_ATTEMPT_MAX_VALUE);
        mLastPostponedPicker.setMaxValue(MainActivity.LAST_ATTEMPT_MAX_VALUE);

        mCurTask = mTaskList.get(getIntent().getIntExtra("curTaskPosition", 0));
        mTaskNameEditText.setText(mCurTask.getTitle());
        mLastCompletedPicker.setValue((int) mCurTask.daysSinceLastCompleted());
        mLastPostponedPicker.setValue((int) mCurTask.daysSinceLastPostponed());

        mEditTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String taskName = mTaskNameEditText.getText().toString();
                if (taskName.equals("")){
                    Toast.makeText(EditTaskActivity.this, "Task name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurTask.setTitle(taskName);
                mCurTask.setLastCompleted(SchedulerUtils.calcLastDate(mLastCompletedPicker.getValue()));
                mCurTask.setLastPostponed(SchedulerUtils.calcLastDate(mLastPostponedPicker.getValue()));

                SchedulerPrefs.storeTaskList(mTaskList, getApplicationContext());
                Toast.makeText(EditTaskActivity.this, "Task edited", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mDeleteTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(v, R.string.confirm_delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.delete_label, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mTaskList.remove(mCurTask);
                                SchedulerPrefs.storeTaskList(mTaskList, getApplicationContext());
                                finish();
                            }
                        });
                snackbar.show();
            }
        });
}
}