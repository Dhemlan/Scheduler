package com.example.android.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements ScheduleAdaptor.ListItemClickListener{

    public static final int LIST_ITEMS_COUNT = 100;
    private RecyclerView mTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskList = (RecyclerView) findViewById(R.id.rv_tasks);

        mTaskList.setLayoutManager(new LinearLayoutManager(this));
        mTaskList.setHasFixedSize(true);
        mTaskList.setAdapter(new ScheduleAdaptor(LIST_ITEMS_COUNT, this));
    }

    @Override
    public void ListItemClicked(int clickedItemIndex) {
        String message = "clicked item #" + clickedItemIndex;
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}