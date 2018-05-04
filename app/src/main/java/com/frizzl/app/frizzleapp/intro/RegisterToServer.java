package com.frizzl.app.frizzleapp.intro;

import android.os.AsyncTask;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.ConnectToServer;
import com.frizzl.app.frizzleapp.UserProfile;

import java.io.UnsupportedEncodingException;
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
        String birthDate = strings[2];
        String nickName = strings[3];

        try {
            String query = String.format("username=%s&password=%s&email=%s&birthDateStr=%s&nickName=%s",
                    URLEncoder.encode(email, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(password, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(email, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(birthDate, StandardCharsets.UTF_8.name()),
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