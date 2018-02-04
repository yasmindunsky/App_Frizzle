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
        // case of success
        if (result.equals(Integer.toString(HttpURLConnection.HTTP_OK))) {
            delegate.processFinish("Registration Succeeded");

            // case that email is already exists in the DB
        } else if (result.equals(Integer.toString(HttpURLConnection.HTTP_CONFLICT))) {
            delegate.processFinish("email already exists");

            // case of any other error
        } else {
            delegate.processFinish("Something went wrong. Status: " + result);
        }
    }
}

// default ip - 127.0.0.1
// ip at Agur 192.168.1.12
// ip at PICO 10.10.30.145
// ip at Melchett 10.100.102.8
// ip at Noga 192.168.14.159
// ip at HUJI 172.29.105.213