package com.frizzl.app.frizzleapp;

import android.content.Context;

import com.frizzl.app.frizzleapp.practice.AppTasks;

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
    private UserApp confessionUserApp;
    private UserApp friendshipUserApp;

    // Level is of apps and practices combined.
    private int currentLevel;
    private int topLevel;
    // Slide in practice and task in app.
    private int currentSlideInLevel;
    private int topSlideInLevel;

    public static UserProfile user = new UserProfile();
    private static String userID;

    private UserProfile() {
        init();
    }

    public static void setUserID(String userID) {
        UserProfile.userID = userID;
    }

    public static String getUserID() {
        return userID;
    }

    private void init(){
        confessionUserApp = null;
        friendshipUserApp = null;
        topLevel = 8; // TODO: change before release
        currentLevel = 0;
        currentSlideInLevel = 0;
        topSlideInLevel = 0;
    }

    public AppTasks getCurrentAppTasks() {
        return currentAppTasks;
    }

    public void setCurrentAppTasks(AppTasks currentAppTasks) {
        this.currentAppTasks = currentAppTasks;
    }

    public UserApp getCurrentUserApp() {
        if (ContentUtils.CONFESSIONS_APP_LEVEL_ID == currentLevel) {
            return confessionUserApp;
        }
        else if (ContentUtils.FRIENDSHIP_APP_LEVEL_ID == currentLevel) {
            return friendshipUserApp;
        }
        else {
            return null;
        }
    }

    public void setCurrentUserApp(UserApp currentUserApp) {
        if (ContentUtils.CONFESSIONS_APP_LEVEL_ID == currentLevel) {
            confessionUserApp = currentUserApp;
        }
        else if (ContentUtils.FRIENDSHIP_APP_LEVEL_ID == currentLevel) {
            friendshipUserApp = currentUserApp;
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

    public void finishedPractice(int levelID) {
        if (levelID == topLevel) {
            topLevel++;
        }
        if (currentLevel < topLevel){
            currentLevel++;
            currentSlideInLevel = 0;
            topSlideInLevel = 0;
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

    public int getTopLevel() {
        return topLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentSlideInLevel() {
        return currentSlideInLevel;
    }

    public void setCurrentSlideInLevel(int currentSlideInLevel) {
        this.currentSlideInLevel = currentSlideInLevel;
    }

    public int getTopSlideInLevel() {
        return topSlideInLevel;
    }

    public void setTopSlideInLevel(int topSlideInLevel) {
        this.topSlideInLevel = topSlideInLevel;
    }

    public void finishedCurrentSlideInLevel() {
        if (currentSlideInLevel == topSlideInLevel) {
            topSlideInLevel++;
        }
        if (currentSlideInLevel < topSlideInLevel){
            currentSlideInLevel++;
        }
    }
}
