package com.example.yasmindunsky.frizzleapp;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class CreateNewUser extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection client = null;
        try {
            URL url = new URL("http://10.10.30.145:8000/users/register/");

            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");

            String charset = StandardCharsets.UTF_8.name();
            client.setRequestProperty("Accept-Charset", charset);
            client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            client.setDoOutput(true);
            client.setChunkedStreamingMode(0);

//            OutputStream out = new BufferedOutputStream(client.getOutputStream());

            String username = "username";
            String password = "password";
            String query = String.format("username=%s&password=%s",URLEncoder.encode(username, charset), URLEncoder.encode(password, charset));


            try (OutputStream output = client.getOutputStream()) {
                output.write(query.getBytes(charset));

                output.flush();
                output.close();
            }

        } catch (MalformedURLException e) {
            //Handles an incorrectly entered URL
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            //Handles URL access timeout.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return null;
    }
}
