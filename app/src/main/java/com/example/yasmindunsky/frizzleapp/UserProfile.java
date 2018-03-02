package com.example.yasmindunsky.frizzleapp;

import com.example.yasmindunsky.frizzleapp.appBuilder.UserCreatedView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class UserProfile {

    private String username = "";
    private String nickName = "";

    private int currentLessonID = 1;
    private int topLessonID = 1;
    private int currentCourseID = 1;
    private int topCourseID = 1;

    private String xml = "";
    private String java = "";

    public Map<Integer, UserCreatedView> views = new HashMap<>();

    public Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    public void setViews(Map<Integer, UserCreatedView> views) {
        this.views = views;
    }


    public static UserProfile user = new UserProfile();

    private UserProfile() {
    }

    public int getCurrentCourseID() {
        return currentCourseID;
    }

    public void setCurrentCourseID(int currentCourseID) {
        this.currentCourseID = currentCourseID;
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



}
