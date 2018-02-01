package com.example.yasmindunsky.frizzleapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yasmin.dunsky on 31-Jan-18.
 */
public class ConnectToServer {

    public String postToServer(String path, String query) {
        final String SERVER_ADDRESS = "http://10.10.30.145:8000";

        HttpURLConnection client = null;
        try {

            URL url = new URL(SERVER_ADDRESS + path);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            client.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            client.setDoOutput(true);

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

            int responseCode = client.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                if ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                return sb.toString();

            } else {

                return Integer.toString(client.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return null;
    }
}
