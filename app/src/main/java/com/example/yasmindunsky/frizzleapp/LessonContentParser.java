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
        String lessonXmlName = "lesson_" + Integer.toString(LessonActivity.currentLesson.getID());
        xmlResourceParser = context.getResources().getXml(getResId(lessonXmlName, R.xml.class));
    }

    public void parseLesson() throws XmlPullParserException, IOException {

        ArrayList<Slide> slides = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();

        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // slides pages parsing part
                if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("slide")) {
                    Slide newPage = parseSlides();
                    slides.add(newPage);
                }

                // TODO parse exercise and tasks
                else if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("exercise")) {
                    Exercise newPage = parseExercise();
                    exercises.add(newPage);
                }

                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Slide lastSlidePage = new Slide("GOOD JOB!", "fireworks");
        slides.add(lastSlidePage);
        LessonActivity.currentLesson.setSlides(slides);

        LessonActivity.currentLesson.setExercises(exercises);
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

    private Slide parseSlides() throws XmlPullParserException, IOException {
        String text = getValue("text");
        String image = getValue("image");

        xmlResourceParser.next();

        return new Slide(text, image);
    }

    private Exercise parseExercise() throws XmlPullParserException, IOException {
        String type = getValue("type");
        String question = getValue("question");
        String image = getValue("image");

        ArrayList<String> content = new ArrayList<>();
        addChildren(content, "content", "object");

        ArrayList<String> possibilities = new ArrayList<>();
        addChildren(possibilities, "possibilities", "possibility");

        ArrayList<String> answers = new ArrayList<>();
        addChildren(answers, "answers", "answer");

        return new Exercise(type, question, image, content, possibilities, answers);
    }

    private String getValue(String key) throws XmlPullParserException, IOException {
        xmlResourceParser.next();

        while (!xmlResourceParser.getName().equals(key)) {
            xmlResourceParser.next();
        }

        xmlResourceParser.next();
        return xmlResourceParser.getText();
    }


    private void addChildren(ArrayList<String> possibilities, String root, String child) throws XmlPullParserException, IOException {
        getValue(root);

        while (!(xmlResourceParser.getEventType() == XmlPullParser.END_TAG && xmlResourceParser.getName().equals(root))) {

            while (!xmlResourceParser.getName().equals(child)) {
                xmlResourceParser.next();
            }
            xmlResourceParser.next();
            possibilities.add(xmlResourceParser.getText());

            while (!(xmlResourceParser.getEventType() == XmlPullParser.END_TAG && xmlResourceParser.getName().equals(child))) {
                xmlResourceParser.next();
            }

            xmlResourceParser.next();
        }
    }
}
