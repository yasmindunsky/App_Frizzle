package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frizzl.app.frizzleapp.AnnotationUserCreatedViewType;
import com.frizzl.app.frizzleapp.Support;

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
    private static int numOfImageViews;
    private static int nextViewIndex;

    public static Map<Integer, UserCreatedView> initializeViews(Context context) {
        views = new HashMap<>();
        numOfButtons = 0;
        numOfTextViews = 0;
        numOfImageViews = 0;
        nextViewIndex = 0;

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

    private static void incrementImageViewsCount() {
        nextViewIndex++;
        numOfImageViews++;
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

    public static void addNewUserCreatedImageView(Context context){
        UserCreatedImageView userCreatedImageView = new UserCreatedImageView(context, nextViewIndex, numOfImageViews);
        views.put(nextViewIndex, userCreatedImageView);
        incrementImageViewsCount();
    }

    public static void deleteUserCreatedView(int viewToDeleteIndex){
        UserCreatedView viewToDelete = views.get(viewToDeleteIndex);

        if (viewToDelete.getClass().equals(UserCreatedTextView.class)) {
            numOfTextViews--;
        }
        else if (viewToDelete.getClass().equals(UserCreatedButton.class)) {
            numOfButtons--;
        }
        else if (viewToDelete.getClass().equals(UserCreatedImageView.class)) {
            numOfImageViews--;
        }

        View thisView = viewToDelete.getThisView();
        ((ViewGroup)thisView.getParent()).removeView(thisView);
        views.remove(viewToDeleteIndex);
    }

    public static int getNextViewIndex() {
        return nextViewIndex;
    }

    public static String getXml(String appIcon, String appName){
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeXml(views, appIcon, appName);
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
                String viewType = viewJson.getString("viewType");
                int index = Integer.parseInt(viewJson.getString("id"));
                Map<String,String> properties = new HashMap<>();
                for (Iterator<String> it = viewJson.keys(); it.hasNext(); ) {
                    String key = it.next();
                    properties.put(key, viewJson.get(key).toString());
                }
                UserCreatedView userCreatedView = null;
                switch (viewType) {
                    case AnnotationUserCreatedViewType.TEXT_VIEW:
                        userCreatedView = new UserCreatedTextView(context, properties, i);
                        numOfTextViews++;
                        break;
                    case AnnotationUserCreatedViewType.BUTTON:
                        userCreatedView = new UserCreatedButton(context, properties, i);
                        numOfButtons++;
                        break;
                    case AnnotationUserCreatedViewType.IMAGE_VIEW:
                        userCreatedView = new UserCreatedImageView(context, properties, i);
                        numOfImageViews++;
                        break;
                }

                // Set padding.
                String paddingString = properties.get("android:padding");
                int padding = Support.dpStringToPixel(paddingString, context);
                String paddingSideString = properties.get("android:paddingStart");
                int paddingSide = Support.dpStringToPixel(paddingSideString, context);

                userCreatedView.getThisView().setPadding(paddingSide,padding,paddingSide,padding);

                views.put(index, userCreatedView);
            }
            nextViewIndex = viewsJson.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return views;
    }

    public static JSONObject viewsToJson(Map<Integer, UserCreatedView> views) {
        JSONArray jsonArray = new JSONArray();
        JSONObject json;
        JSONObject finalObject = new JSONObject();

        if(views != null) {
            try {
                int i = 0;
                for (int key : views.keySet()){
                    json = new JSONObject();
                    json.put("id", i);
                    i++;
                    UserCreatedView userCreatedView = views.get(key);
                    json.put("viewType", userCreatedView.getViewType());
                    Map<String, String> properties = userCreatedView.getProperties();
                    for (Map.Entry<String, String> property : properties.entrySet()) {
                        json.put(property.getKey(), property.getValue());
                    }
                    jsonArray.put(json);
                }
                finalObject.put("views", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return finalObject;
    }

    public static Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    public static void setViews(Map<Integer, UserCreatedView> newViews) {
        views = newViews;
        numOfButtons = 0;
        numOfTextViews = 0;
        numOfImageViews = 0;
        nextViewIndex = 0;
    }

    public static int getNumOfButtons() {
        return numOfButtons;
    }

    public static int getNumOfTextViews() {
        return numOfTextViews;
    }

    public static int getNumOfImageViews() {
        return numOfImageViews;
    }

    public static void setNumOfButton(int numOfButton) {
    }

    public static void setNumOfTextViews(int numOfTextViews) {
        UserCreatedViewsModel.numOfTextViews = numOfTextViews;
    }

    public static void setNumOfImageViews(int numOfImageViews) {
        UserCreatedViewsModel.numOfImageViews = numOfImageViews;
    }

    public static String getManifest(String appIcon, String appName) {
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeManifest(appIcon, appName);
    }

    public static void setNextViewIndex(int nextViewIndex) {
        UserCreatedViewsModel.nextViewIndex = nextViewIndex;
    }
}
