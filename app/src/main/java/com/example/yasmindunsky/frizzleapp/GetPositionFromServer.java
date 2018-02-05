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

        connectToServer.postToServer("/user/getPosition", body, "GET");
        return null;
    }


    protected void onPostExecute(String result) {
//        JSONArray jArray = null;
//        try {
//            jArray = new JSONArray(result);
//
//
//            for (int i = 0; i < jArray.length(); i++) {
//                JSONObject jObject = jArray.getJSONObject(i);
//                String name = jObject.getString("name");
//                String tab1_text = jObject.getString("tab1_text");
//                int active = jObject.getInt("active");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
