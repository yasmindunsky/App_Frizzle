package com.example.yasmindunsky.frizzleapp;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class UpdatePositionInServer extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            ConnectToServer connectToServer = new ConnectToServer();

            String query = String.format("username=%s&currentLessonId=%s&topLessonId=%s&topCourseId=%s&currentCourseId=%s",
                    URLEncoder.encode(UserProfile.user.getUsername(), StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(String.valueOf(UserProfile.user.getCurrentLessonID()), StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(String.valueOf(UserProfile.user.getTopLessonID()), StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(String.valueOf(UserProfile.user.getTopCourseID()), StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(String.valueOf(UserProfile.user.getCurrentCourseID()), StandardCharsets.UTF_8.name()));

            return connectToServer.postToServer("/user/setPosition", query, "POST");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
