package com.example.yasmindunsky.frizzleapp;

import android.os.AsyncTask;
import java.io.IOException;
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
            client.setRequestProperty("USER-AGENT","Mozilla/5.0");
            client.setRequestProperty("ACCEPT-LANGUAGE","en-US,en;0.5");

            client.setDoOutput(true);

            String username = strings[0];
            String email = strings[1];
            String password = strings[2];

            String query = String.format("username=%s&email=%s&password=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(email, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(password, StandardCharsets.UTF_8.name()));

            byte[] postData = query.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            client.setRequestProperty("charset", "utf-8");
            client.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            client.setFixedLengthStreamingMode(postDataLength);

            try (OutputStream output = client.getOutputStream()) {
                output.write(query.getBytes(StandardCharsets.UTF_8));
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
//        TextView txt = (TextView) findViewById();
//        txt.setText("Executed"); // txt.setText(result);
        super.onPostExecute(result);
    }
}

// default ip - 127.0.0.1
// ip at Agur 192.168.1.8
// ip at PICO 10.10.30.145
// ip at Melchett 10.100.102.8
// ip at Noga 192.168.14.159
// ip at HUJI 172.29.105.213



