package com.frizzl.app.frizzleapp.practice;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class AppTasks implements Serializable{

    private final int appLevelID;
    private ArrayList<Task> tasks;

    public AppTasks(int appLevelID) {
        this.appLevelID = appLevelID;
    }

    public int getID() {
        return appLevelID;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getNumberOfTasks() {
        return tasks.size();
    }
}
