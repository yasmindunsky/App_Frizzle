package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.os.AsyncTask;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.ConnectToServer;
import com.example.yasmindunsky.frizzleapp.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * Created by yasmin.dunsky on 01-Feb-18.
 */

public class BuildApkInServer extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public BuildApkInServer(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        GraphicEditFragment graphicEditFragment = new GraphicEditFragment();

        String courseId = String.valueOf(UserProfile.user.getCurrentCourseID());
        String username = UserProfile.user.getUsername();

        String start = strings[0];
        String end = strings[1];
        String code = start + UserProfile.user.getJava() + end;

        String xml = UserProfile.user.getXml();

        JSONObject viewsToJson = ((GraphicEditFragment) graphicEditFragment).viewsToJson();
        String views = viewsToJson.toString();

        String query = null;
        try {
            query = String.format("username=%s&courseId=%s&code=%s&xml=%s&views=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(courseId, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(code, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(xml, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(views, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return connectToServer.postToServer("/project/save", query, "POST");
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
