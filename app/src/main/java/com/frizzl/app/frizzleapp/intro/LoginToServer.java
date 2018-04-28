package com.frizzl.app.frizzleapp.intro;

import android.os.AsyncTask;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.ConnectToServer;
import com.frizzl.app.frizzleapp.GetPositionFromServer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class LoginToServer extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public LoginToServer(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        String username = strings[0];
        String password = strings[1];

        try {
            // try login
            String body = String.format("username=%s&password=%s&remember-me='false'",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(password, StandardCharsets.UTF_8.name()));
            String response = connectToServer.postToServer("/user/authenticate", body, "POST");

            // if login was successful, get user's data
            if (response.equals("Authentication succeeded")) {
                new GetPositionFromServer(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {

                    }
                }).execute(username);
            }

            return response;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}

