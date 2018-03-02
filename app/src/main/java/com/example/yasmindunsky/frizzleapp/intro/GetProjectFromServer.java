package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Context;
import android.os.AsyncTask;

import com.example.yasmindunsky.frizzleapp.ConnectToServer;
import com.example.yasmindunsky.frizzleapp.UserProfile;
import com.example.yasmindunsky.frizzleapp.appBuilder.GraphicEditFragment;
import com.example.yasmindunsky.frizzleapp.appBuilder.UserCreatedButton;
import com.example.yasmindunsky.frizzleapp.appBuilder.UserCreatedTextView;
import com.example.yasmindunsky.frizzleapp.appBuilder.UserCreatedView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by yasmin.dunsky on 02-Mar-18.
 */

public class GetProjectFromServer extends AsyncTask<String, Void, String> {
    private Context mContext;
    private String attribute;

    public GetProjectFromServer(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        ConnectToServer connectToServer = new ConnectToServer();

        String username = strings[0];
        attribute = strings[1];

        String body = null;
        try {
            body = String.format("username=%s&attr=%s",
                    URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(attribute, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return connectToServer.postToServer("/project/getData", body, "GET");
    }


    protected void onPostExecute(String result) {
        try {
            JSONObject reader = new JSONObject(result);
            String attributeString = String.valueOf(reader.get(attribute));

            switch (attribute) {
                case "views":
                    // parse viewString to view element
                    GraphicEditFragment graphicEditFragment = new GraphicEditFragment();
                    Map<Integer, UserCreatedView> views = graphicEditFragment.jsonToViews(mContext, attributeString);

                    // save the view to the UserProfile object
                    UserProfile.user.setViews(views);
                    break;
                case "xml":
                    UserProfile.user.setXml(attributeString);
                    break;
                case "code":
                    UserProfile.user.setJava(attributeString);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveViewsToUser(String viewsString) {
        GraphicEditFragment graphicEditFragment = new GraphicEditFragment();
        Map<Integer, UserCreatedView> views = graphicEditFragment.jsonToViews(mContext, viewsString);
        UserProfile.user.setViews(views);

    }
}