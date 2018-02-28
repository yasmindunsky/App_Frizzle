package com.example.yasmindunsky.frizzleapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by yasmin.dunsky on 03-Feb-18.
 */

public class GetPositionFromServer extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        String username = strings[0];
        String body = null;
        try {
            body = String.format("username=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return connectToServer.postToServer("/user/getPosition", body, "GET");
    }


    protected void onPostExecute(String result) {
        try {
            JSONObject reader = new JSONObject(result);
            String topLessonId = String.valueOf(reader.get("topLessonId"));
            String currentLessonId = String.valueOf(reader.get("currentLessonId"));
            String viewsJSON = String.valueOf(reader.get("views"));

            if (currentLessonId.equals("null")) {
                UserProfile.user.setCurrentLessonID(1);
                UserProfile.user.setTopLessonID(1);
            } else {
                UserProfile.user.setCurrentLessonID(Integer.parseInt(currentLessonId));
                UserProfile.user.setTopLessonID(Integer.parseInt(topLessonId));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fromJSONtoViews(String jsonViews){

    }
}
