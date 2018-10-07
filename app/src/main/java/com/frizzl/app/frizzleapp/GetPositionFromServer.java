//package com.frizzl.app.frizzleapp;
//
//import android.os.AsyncTask;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
///**
// * Created by yasmin.dunsky on 03-Feb-18.
// */
//
//public class GetPositionFromServer extends AsyncTask<String, Void, String> {
//    public AsyncResponse delegate = null;
//
//    public GetPositionFromServer(AsyncResponse delegate) {
//        this.delegate = delegate;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        ConnectToServer connectToServer = new ConnectToServer();
//
//        String username = strings[0];
//
//        String body = null;
//        try {
//            body = String.format("username=%s",
//                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        return connectToServer.postToServer("/user/getPosition", body, "GET");
//    }
//
//
//    protected void onPostExecute(String result) {
//        try {
//            JSONObject reader = new JSONObject(result);
//
//            // save current and top lesson id
//            String topLessonId = String.valueOf(reader.get("topLessonId"));
//            String currentLessonId = String.valueOf(reader.get("currentLessonId"));
//            if (currentLessonId.equals("null")) {
//                UserProfile.user.setCurrentLessonID(1);
//                UserProfile.user.setTopLessonID(1);
//            } else {
//                UserProfile.user.setCurrentLessonID(Integer.parseInt(currentLessonId));
//                UserProfile.user.setTopLessonID(Integer.parseInt(topLessonId));
//            }
//
//            delegate.processFinish(result);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//}
