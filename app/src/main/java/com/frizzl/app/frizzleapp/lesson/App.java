package com.frizzl.app.frizzleapp.lesson;

import com.frizzl.app.frizzleapp.lesson.exercise.Exercise;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class App {

    private int appID;
    private ArrayList<Task> tasks;
    private String xml;
    private String java;
    private String appName;
    private String icon;

    public App(int appID) {
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
