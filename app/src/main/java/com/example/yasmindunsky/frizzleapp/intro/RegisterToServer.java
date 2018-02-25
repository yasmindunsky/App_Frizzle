package com.example.yasmindunsky.frizzleapp.intro;

import android.os.AsyncTask;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.ConnectToServer;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class RegisterToServer extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public RegisterToServer(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();
        String password = strings[0];
        String email = strings[1];
        String nickName = strings[2];

        try {
            String query = String.format("username=%s&password=%s&email=%s&nickName=%s",
                    URLEncoder.encode(email, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(password, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(email, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(nickName, StandardCharsets.UTF_8.name()));
            return connectToServer.postToServer("/user/register", query, "POST");
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