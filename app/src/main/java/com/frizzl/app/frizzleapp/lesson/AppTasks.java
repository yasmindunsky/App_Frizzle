package com.frizzl.app.frizzleapp.lesson;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class AppTasks {

    private int appID;
    private ArrayList<Task> tasks;

    public AppTasks(int appID) {
        this.appID = appID;
    }

    public int getID() {
        return appID;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTasksNum() {
        return tasks.size();
    }
}
