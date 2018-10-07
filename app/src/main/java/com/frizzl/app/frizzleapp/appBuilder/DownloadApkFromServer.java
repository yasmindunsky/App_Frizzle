package com.frizzl.app.frizzleapp.appBuilder;

import android.os.AsyncTask;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.ConnectToServer;
import com.frizzl.app.frizzleapp.UserProfile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by yasmin.dunsky on 14-Feb-18.
 */

public class DownloadApkFromServer extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public DownloadApkFromServer(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        String code = strings[0];
        String xml = strings[1];
        String manifest = strings[2];
        String username = "";
        String courseId = "3";
//        String username = UserProfile.user.getUsername();
//        String courseId = String.valueOf(UserProfile.user.getCurrentUserApp().getAppID());
//        String courseId = String.valueOf(UserProfile.user.getCurrentCourseID());

        String query = null;
        try {
            query = String.format("username=%s&courseId=%s&code=%s&xml=%s&xml2=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(courseId, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(code, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(xml, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(manifest, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return connectToServer.postToServer("/project/download", query, "GET");
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }


}
