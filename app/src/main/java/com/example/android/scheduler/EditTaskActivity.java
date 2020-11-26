package com.example.android.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                editQuestion();
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

    public void editQuestion(){
        String taskName = mTaskNameEditText.getText().toString();
        if (taskName.equals("")){
            Toast.makeText(EditTaskActivity.this, R.string.task_name_not_blank_label, Toast.LENGTH_SHORT).show();
            return;
        }
        mCurTask.setTitle(taskName);
        mCurTask.setLastCompleted(SchedulerUtils.calcLastDate(mLastCompletedPicker.getValue()));
        mCurTask.setLastPostponed(SchedulerUtils.calcLastDate(mLastPostponedPicker.getValue()));

        SchedulerUtils.updateList(mTaskList, mCurTask);

        SchedulerPrefs.storeTaskList(mTaskList, getApplicationContext());
        Toast.makeText(EditTaskActivity.this, R.string.task_edited, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_task){
            editQuestion();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);
        return true;
    }
}