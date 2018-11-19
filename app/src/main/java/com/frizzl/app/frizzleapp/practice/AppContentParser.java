package com.frizzl.app.frizzleapp.practice;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 21-Dec-17.
 */
public class AppContentParser {

    private XmlResourceParser xmlResourceParser;

    public AppTasks parseAppXml(Context context, int currentAppLevelID)
            throws XmlPullParserException {
        String lessonXmlName = "app_" + Integer.toString(currentAppLevelID);
        xmlResourceParser = context.getResources().getXml(getResId(lessonXmlName, R.xml.class));
        AppTasks currentApp = new AppTasks(currentAppLevelID);
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG
                        && xmlResourceParser.getName().equals("task")) {
                    Task newTask = parseTask();
                    tasks.add(newTask);
                }
                eventType = xmlResourceParser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentApp.setTasks(tasks);
        return currentApp;
    }

    private static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Task parseTask() throws XmlPullParserException, IOException {
        String text = getValue("text");
        return new Task(text);
    }

    private String getValue(String key) throws XmlPullParserException, IOException {
        xmlResourceParser.next();

        while (!xmlResourceParser.getName().equals(key)) {
            xmlResourceParser.next();
        }

        xmlResourceParser.next();
        return xmlResourceParser.getText();
    }
}
