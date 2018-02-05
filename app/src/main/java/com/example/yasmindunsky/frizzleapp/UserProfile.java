package com.example.yasmindunsky.frizzleapp;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class UserProfile {

    private String username = "";
    private String nickName = "";

    private int currentLessonID = 0;
    private int topLessonID = 0;
    private int currentCourseID = 0;
    private int topCourseID = 0;

    private String xml = "";
    private String java = "";

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
