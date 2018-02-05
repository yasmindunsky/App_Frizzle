package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.os.AsyncTask;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.ConnectToServer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * Created by yasmin.dunsky on 01-Feb-18.
 */

public class SendFilesToServer extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;

    public SendFilesToServer(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        String xmlFile = strings[0];
        String javaFile = strings[1];

        String query = null;
        try {
            query = String.format("java=%s&xml=%s", URLEncoder.encode(javaFile, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(xmlFile, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return connectToServer.postToServer("/users/login/", query, "POST");
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
