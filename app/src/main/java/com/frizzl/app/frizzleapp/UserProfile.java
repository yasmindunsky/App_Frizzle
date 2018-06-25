package com.frizzl.app.frizzleapp;

import android.content.Context;

import com.frizzl.app.frizzleapp.appBuilder.UserCreatedTextView;
import com.frizzl.app.frizzleapp.appBuilder.UserCreatedView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class UserProfile {

    private String username = "";
    private String nickName = "";
    private String birthDate = "";
    private String currentAppType = "";

    private int currentLessonID = 1;
    private int topLessonID = 1;
    private int currentCourseID = 1;
    private int topCourseID = 1;

    private String xml = "";
    private String java = "";

    public Map<Integer,UserCreatedView> views = new HashMap<>();

    public static UserProfile user = new UserProfile();

    public void restartUserProfile(){
        username = "";
        nickName = "";
        birthDate = "";
        currentAppType = "";

        currentLessonID = 1;
        topLessonID = 1;
        currentCourseID = 1;
        topCourseID = 1;

        xml = "";
        java = "";
        views = new HashMap<>();
    }

    public Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    public void setViews(Map<Integer, UserCreatedView> views) {
        this.views = views;
    }

    private UserProfile() {
    }

    public int getCurrentCourseID() {
        return currentCourseID;
    }

    public void setCurrentCourseID(int currentCourseID) {
        if (currentCourseID > this.currentCourseID) {
            this.currentCourseID = currentCourseID;
            xml = "";
            java = "";
            views = new HashMap<>();
        }
    }

    public int getTopCourseID() {
        return topCourseID;
    }

    public void setTopCourseID(int topCourseID) {
        this.topCourseID = topCourseID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCurrentLessonID() {
        return currentLessonID;
    }

    public void setCurrentLessonID(int currentLessonID) {
        this.currentLessonID = currentLessonID;
        UserProfile.user.setCurrentCourseID(1);
        if (currentLessonID == 8) {
            UserProfile.user.setCurrentCourseID(2);
        }

        // update position in server
        new UpdatePositionInServer().execute();
    }

    public boolean isLessonOpen(int lessonID) {
        return (lessonID <= this.getTopLessonID());
    }

    public int getTopLessonID() {
        return topLessonID;
    }

    public void setTopLessonID(int topLessonID) {
        this.topLessonID = topLessonID;

    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getJava() {
        return java;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public String getCurrentAppType() {
        return currentAppType;
    }

    public void setCurrentAppType(String currentAppType) {
        this.currentAppType = currentAppType;
    }
}
