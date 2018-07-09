package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frizzl.app.frizzleapp.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Noga on 09/07/2018.
 */

public class UserCreatedViewsModel {
    private static Map<Integer, UserCreatedView> views;
    private static int numOfButtons;
    private static int numOfTextViews;
    private static int nextViewIndex;

    public static Map<Integer, UserCreatedView> initializeViews(Context context) {
        views = new HashMap<>();
        numOfButtons = 0;
        numOfTextViews = 0;
        nextViewIndex = 0;

        // Add hello world view
        addNewUserCreatedButton(context);
        return views;
        }

    private static void incrementButtonsCount() {
        nextViewIndex++;
        numOfButtons++;
    }

    private static void incrementTextViewsCount() {
        nextViewIndex++;
        numOfTextViews++;
    }

    public static void addNewUserCreatedTextView(Context context){
        UserCreatedTextView userCreatedTextView = new UserCreatedTextView(context, nextViewIndex, numOfTextViews);
        views.put(nextViewIndex, userCreatedTextView);
        incrementTextViewsCount();
    }

    public static void addNewUserCreatedButton(Context context){
        UserCreatedButton userCreatedButton = new UserCreatedButton(context, nextViewIndex, numOfButtons);
        views.put(nextViewIndex, userCreatedButton);
        incrementButtonsCount();
    }

    public static void deleteUserCreatedVIew(int viewToDeleteIndex){
        UserCreatedView viewToDelete = views.get(viewToDeleteIndex);

        if (viewToDelete.getClass().equals(UserCreatedTextView.class)) {
            numOfTextViews--;
        }
        else if (viewToDelete.getClass().equals(UserCreatedButton.class)) {
            numOfButtons--;
        }

        View thisView = viewToDelete.getThisView();
        ((ViewGroup)thisView.getParent()).removeView(thisView);
        views.remove(viewToDeleteIndex);
    }

    public static int getNextViewIndex() {
        return nextViewIndex;
    }

    public static String getXml(){
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeXml(views);
    }

    public static Map<Integer, UserCreatedView> jsonToViews(Context context, String viewsJsonString) {
        views = new HashMap<>();
        numOfButtons = 0;
        numOfTextViews = 0;
        nextViewIndex = 0;

        try {
            JSONArray viewsJson = new JSONArray(viewsJsonString);
            for(int i = 0 ; i < viewsJson.length(); i++){
                JSONObject viewJson = viewsJson.getJSONObject(i);
                UserCreatedView.ViewType viewType = getViewType(viewJson.getString("viewType"));
                int index = Integer.parseInt(viewJson.getString("id"));
                Map<String,String> properties = new HashMap<>();
                for (Iterator<String> it = viewJson.keys(); it.hasNext(); ) {
                    String key = it.next();
                    properties.put(key.toString(), viewJson.get(key).toString());
                }
                UserCreatedView userCreatedView = null;
                switch (viewType) {
                    case TextView:
                        userCreatedView = new UserCreatedTextView(context, properties, i);
                        numOfTextViews++;
                        break;
                    case Button:
                        userCreatedView = new UserCreatedButton(context, properties, i);
                        numOfButtons++;
                        break;
                }
                views.put(index, userCreatedView);
            }
            nextViewIndex = viewsJson.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return views;
    }

    public static JSONObject viewsToJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject finalObject = new JSONObject();

        views = UserProfile.user.getViews();

        if(views != null) {
            try {
                int i = 0;
                for (int key : views.keySet()){
                    json = new JSONObject();
                    json.put("id", i);
                    i++;
                    UserCreatedView userCreatedView = views.get(key);
                    json.put("viewType", userCreatedView.getViewType().toString());
                    Map<String, String> properties = userCreatedView.getProperties();
                    for (Map.Entry<String, String> property : properties.entrySet()) {
                        json.put(property.getKey(), property.getValue());
                    }
                    jsonArray.put(json);
                }
//                for (int i = 0; i < views.size(); i++) {
//                    json = new JSONObject();
//                    json.put("id", i);
//                    UserCreatedView userCreatedView = views.get(i);
//                    json.put("viewType", userCreatedView.getViewType().toString());
//                    Map<String, String> properties = userCreatedView.getProperties();
//                    for (Map.Entry<String, String> property : properties.entrySet()) {
//                        json.put(property.getKey(), property.getValue());
//                    }
//                    jsonArray.put(json);
//                }

                finalObject.put("views", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return finalObject;
    }

    public static UserCreatedView.ViewType getViewType(String viewType) {
        switch (viewType) {
            case "Button":
                return UserCreatedView.ViewType.Button;
            case "TextView":
                return UserCreatedView.ViewType.TextView;
        }
        return null;
    }

    public static Map<Integer, UserCreatedView> getViews() {
        return views;
    }
}
