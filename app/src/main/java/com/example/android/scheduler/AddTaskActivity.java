package com.example.android.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
                addQuestion();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.menu_add_task){
                addQuestion();
            }
        return super.onOptionsItemSelected(item);
    }

    public void addQuestion(){
        String taskName = mTaskNameEditText.getText().toString();
        if (taskName.equals("")){
            Toast.makeText(AddTaskActivity.this, R.string.task_name_not_blank_label, Toast.LENGTH_SHORT).show();
            return;
        }
        long lastCompleted = SchedulerUtils.calcLastDate(mLastCompletedPicker.getValue());
        if (lastCompleted == 0){
            mTaskList.add(new Task(taskName, lastCompleted, 0));
        }
        else {
            SchedulerUtils.updateList(mTaskList, new Task(taskName, lastCompleted,
                    SchedulerUtils.calcLastDate(mLastPostponedPicker.getValue())));
        }
        SchedulerPrefs.storeTaskList(mTaskList, getApplicationContext());
        Toast.makeText(AddTaskActivity.this, R.string.task_added, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }
}