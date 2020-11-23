package com.example.android.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    private ArrayList<Task> mTaskList;
    private Button mAddTaskButton;
    private EditText mTaskNameEditText;
    private NumberPicker mLastCompletedPicker;
    private NumberPicker mLastPostponedPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mAddTaskButton = (Button) findViewById(R.id.add_task_bt);
        mTaskNameEditText = (EditText) findViewById(R.id.add_task_name_et);
        mLastCompletedPicker = (NumberPicker) findViewById(R.id.add_task_last_completed_np);
        mLastPostponedPicker = (NumberPicker) findViewById(R.id.add_task_last_postponed_np);

        mTaskList = SchedulerPrefs.retrieveTaskList(getApplicationContext());
        mLastCompletedPicker.setMaxValue(MainActivity.LAST_ATTEMPT_MAX_VALUE);
        mLastPostponedPicker.setMaxValue(MainActivity.LAST_ATTEMPT_MAX_VALUE);

        mAddTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String taskName = mTaskNameEditText.getText().toString();
                if (taskName.equals("")){
                    Toast.makeText(AddTaskActivity.this, "Task name cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                mTaskList.add(new Task(taskName, SchedulerUtils.calcLastDate(mLastCompletedPicker.getValue()),
                        SchedulerUtils.calcLastDate(mLastPostponedPicker.getValue())));
                SchedulerPrefs.storeTaskList(mTaskList, getApplicationContext());
                Toast.makeText(AddTaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



}