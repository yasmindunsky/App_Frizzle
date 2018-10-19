package com.frizzl.app.frizzleapp;

import android.content.Context;

import com.frizzl.app.frizzleapp.lesson.AppTasks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class UserProfile implements Serializable {

    private AppTasks currentAppTasks;
    private int currentTaskNum;
    private UserApp tutorialUserApp;
    private UserApp pollyUserApp;
    private int currentUserAppLevelID;

    // level is of apps and practices combined
    private int currentLevel;
    private int topLevel;

    public static UserProfile user = new UserProfile();

    private UserProfile() {
        init();
    }

    public void init(){
        tutorialUserApp = null;
        pollyUserApp = null;
        topLevel = 4; // TODO: change before release
        currentLevel = 0;
    }

    public AppTasks getCurrentAppTasks() {
        return currentAppTasks;
    }

    public void setCurrentAppTasks(AppTasks currentAppTasks) {
        this.currentAppTasks = currentAppTasks;
        this.currentTaskNum = 0;
    }

    public UserApp getCurrentUserApp() {
        return (currentUserAppLevelID == 0) ? tutorialUserApp : pollyUserApp;
    }

    public void setCurrentUserAppLevelID(UserApp currentUserApp) {
        if (currentUserAppLevelID == 0){
            tutorialUserApp = currentUserApp;
        } else {
            pollyUserApp = currentUserApp;
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void storeSerializedObject(Context context) {
        String fileName = "FrizzlUserProfile";
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadSerializedObject(Context context){
        String fileName = "FrizzlUserProfile";
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (UserProfile) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
    }

    public void finishedPractice(int practiceID) {
        if (practiceID == topLevel) {
            topLevel++;
        }
        if (currentLevel < topLevel){
            currentLevel++;
            currentTaskNum = 0;
        }
    }

    public void finishedApp(int appLevelID) {
        if (appLevelID == topLevel) {
            topLevel++;
        }
        if (currentLevel < topLevel){
            currentLevel++;
        }
    }

    public int getCurrentTaskNum() {
        return currentTaskNum;
    }

    public void setCurrentTaskNum(int num){
        this.currentTaskNum = num;
    }

    public int getTopLevel() {
        return topLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setCurrentUserAppLevelID(int appLevelID) {
        currentUserAppLevelID = appLevelID;
    }

}
