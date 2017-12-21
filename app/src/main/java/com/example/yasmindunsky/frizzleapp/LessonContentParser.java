package com.example.yasmindunsky.frizzleapp;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 21-Dec-17.
 */

public class LessonContentParser {

    private XmlResourceParser xmlResourceParser;

    public LessonContentParser(Context context) {
        String lessonXmlName = "lesson_" + Integer.toString(LessonActivity.currentLesson.getLessonID());
        xmlResourceParser = context.getResources().getXml(getResId(lessonXmlName, R.xml.class));
    }

    public void parseLesson() throws XmlPullParserException, IOException {

        ArrayList<Theory> theoreticalPages = new ArrayList<>();
        ArrayList<Exercise> exercisePages = new ArrayList<>();

        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // theory pages parsing part
                if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("theory")) {
                    Theory newPage = parseTheoreticalPages();
                    theoreticalPages.add(newPage);
                }

                // TODO parse exercise and tasks
                else if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("exercise")){
                    Exercise newPage = parseExercisePages();
                    exercisePages.add(newPage);
                }

                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Theory lastTheoryPage = new Theory("GOOD JOB!", "fireworks");
        theoreticalPages.add(lastTheoryPage);
        LessonActivity.currentLesson.setLessonTheory(theoreticalPages);

        Exercise lastExercisePage = new Exercise("GOOD JOB!");
        exercisePages.add(lastExercisePage);
        LessonActivity.currentLesson.setLessonExercise(exercisePages);
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Theory parseTheoreticalPages() throws XmlPullParserException, IOException {
        String text = null;
        String imgSrc = null;

        xmlResourceParser.next();
        String name = xmlResourceParser.getName();

        while(!name.equals("text")){
            xmlResourceParser.next();
            name = xmlResourceParser.getName();
        }

        xmlResourceParser.next();
        text = xmlResourceParser.getText();

        xmlResourceParser.next();
        name = xmlResourceParser.getName();

        while(!name.equals("image")){
            xmlResourceParser.next();
            name = xmlResourceParser.getName();
        }

        xmlResourceParser.next();
        imgSrc = xmlResourceParser.getText();

        xmlResourceParser.next();

        return new Theory(text, imgSrc);
    }


    private Exercise parseExercisePages() throws XmlPullParserException, IOException {
        String text = null;
        String imgSrc = null;

        xmlResourceParser.next();
        String name = xmlResourceParser.getName();

        while(!name.equals("text")){
            xmlResourceParser.next();
            name = xmlResourceParser.getName();
        }

        xmlResourceParser.next();
        text = xmlResourceParser.getText();

        xmlResourceParser.next();
        name = xmlResourceParser.getName();

        while(!name.equals("image")){
            xmlResourceParser.next();
            name = xmlResourceParser.getName();
        }

        xmlResourceParser.next();
        imgSrc = xmlResourceParser.getText();

        xmlResourceParser.next();

        return new Exercise(text);
    }
}
