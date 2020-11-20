package com.example.android.scheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SchedulerPrefs {

    public static void storeTaskList(ArrayList<Task> taskList, Context context){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        prefsEditor.putString("taskList", json);
        prefsEditor.commit();
    }

    public static ArrayList<Task> retrieveTaskList(Context context){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("taskList", "");
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> taskList = gson.fromJson(json, type);
        return taskList;
    }

}
