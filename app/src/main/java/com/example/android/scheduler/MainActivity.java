package com.example.android.scheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements ScheduleAdaptor.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int LAST_ATTEMPT_MAX_VALUE = 14;
    public static final int LIST_ITEMS_COUNT = 100;
    private RecyclerView taskRecyclerView;
    private ArrayList<Task> mTaskList;
    private ScheduleAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecyclerView = (RecyclerView) findViewById(R.id.rv_tasks);

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setHasFixedSize(true);

        mTaskList = SchedulerPrefs.retrieveTaskList(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        mAdaptor = new ScheduleAdaptor(this, mTaskList, this);
        taskRecyclerView.setAdapter(mAdaptor);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(mAdaptor));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTaskList = SchedulerPrefs.retrieveTaskList(this);
        mAdaptor.setmTasks(mTaskList);
        mAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void ListItemClicked(int clickedItemIndex) {
        //String message = "clicked item #" + clickedItemIndex;
        Intent intent = new Intent (this, EditTaskActivity.class);
        intent.putExtra("curTaskPosition", clickedItemIndex);
        startActivity(intent);
    }


}