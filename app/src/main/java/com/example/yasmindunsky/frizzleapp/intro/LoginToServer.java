package com.example.yasmindunsky.frizzleapp.intro;

import android.os.AsyncTask;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.ConnectToServer;

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
            String query = String.format("username=%s&password=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(password, StandardCharsets.UTF_8.name()));
            return connectToServer.postToServer("/users/login/", query);

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

