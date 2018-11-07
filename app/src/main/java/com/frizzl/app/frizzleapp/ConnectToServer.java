package com.frizzl.app.frizzleapp;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by yasmin.dunsky on 31-Jan-18.
 */
public class ConnectToServer {

    public String postToServer(String path, String body, String method) {
        final String SERVER_ADDRESS = "http://server.frizzle.co:8080/frizzleserver";

        HttpURLConnection client = null;
        try {

            // open connection to server
            URL url = new URL(SERVER_ADDRESS + path);
            client = (HttpURLConnection) url.openConnection();

            // set the request properties
            client.setRequestMethod(method);
            client.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            client.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            client.setDoOutput(true);

            // build the request body as bites
            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            client.setRequestProperty("charset", "utf-8");
            client.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            client.setFixedLengthStreamingMode(postDataLength);

            Log.d("INSTALL", "in ConnectToServer, postToServer()");
            // send the data to the server
            try (OutputStream output = client.getOutputStream()) {
                output.write(body.getBytes(StandardCharsets.UTF_8));
                output.flush();
                output.close();
//                Log.d("INSTALL", "output: " + output);
            } catch (Exception e) {
                return "Connection to server failed";
            }

            if (path.contains("download")) {
                if (path.contains("Main")) {
                    convertInputStreamToFIle(client, "MainActivity.class");
                } else {
                    downloadApk(client);
                }
                return "file created";
            }

            return getTextResponse(client);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null)
                client.disconnect();
        }
        return null;
    }

    @NonNull
    private String getTextResponse(HttpURLConnection client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            StringBuilder sb = new StringBuilder("");
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    private void convertInputStreamToFIle(HttpURLConnection client, String fileName) {
        try {
            File newFile = new File("/data/user/0/com.example.yasmindunsky.frizzleapp/files/", fileName);
            FileOutputStream stream = new FileOutputStream(newFile, false);
            stream.write(client.getInputStream().available());
            stream.close();
        } catch (IOException e) {
        }
    }

    private void downloadApk(HttpURLConnection client) throws IOException {
        Log.d("INSTALL", "in ConnectToServer, downloadApk()");

        String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        File file = new File(PATH);
        file.mkdirs();

        File outputFile = new File(file, "frizzl_project.apk");
        outputFile.setReadable(true);
        outputFile.setWritable(true);

        if (outputFile.exists()) {
            outputFile.delete();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);
        InputStream inputStream;

        inputStream = client.getInputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }

        fos.close();
        inputStream.close();
    }


}