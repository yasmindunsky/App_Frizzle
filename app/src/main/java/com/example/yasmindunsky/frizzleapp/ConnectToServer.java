package com.example.yasmindunsky.frizzleapp;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
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

    public String postToServer(String path, String body, String method) {
        final String SERVER_ADDRESS = "http://192.168.1.12:8080";

        HttpURLConnection client = null;
        try {

            return "No connection";

//            // open connection to server
//            URL url = new URL(SERVER_ADDRESS + path);
//            client = (HttpURLConnection) url.openConnection();
//
//            // set the request properties
//            client.setRequestMethod(method);
//            client.setRequestProperty("USER-AGENT", "Mozilla/5.0");
//            client.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
//            client.setDoOutput(true);
//
//            // build the request body as bites
//            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
//            int postDataLength = postData.length;
//
//            client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            client.setRequestProperty("charset", "utf-8");
//            client.setRequestProperty("Content-Length", Integer.toString(postDataLength));
//            client.setFixedLengthStreamingMode(postDataLength);
//
//            // send the data to the server
//            try (OutputStream output = client.getOutputStream()) {
//                output.write(body.getBytes(StandardCharsets.UTF_8));
//                output.flush();
//                output.close();
//            } catch (Exception e) {
//                return "Connection to server failed";
//            }
//
//            // in case of GET method, parse response from server
//            if(method.equals("GET")){
//                return getServerResponseText(client);
//            }
//
//            // in case of POST method, return the response code
//            int responseCode = client.getResponseCode();
//            return Integer.toString(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return null;
    }

    @NonNull
    private String getServerResponseText(HttpURLConnection client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        StringBuffer sb = new StringBuffer("");
        String line = "";

        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();

        return sb.toString();
    }
}
