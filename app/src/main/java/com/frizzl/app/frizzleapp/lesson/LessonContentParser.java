package com.frizzl.app.frizzleapp.lesson;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.frizzl.app.frizzleapp.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.frizzl.app.frizzleapp.lesson.exercise.Exercise;

/**
 * Created by yasmin.dunsky on 21-Dec-17.
 */
public class LessonContentParser {

    private XmlResourceParser xmlResourceParser;
    Lesson currentLesson = LessonActivity.getCurrentLesson();

    public LessonContentParser(Context context) throws XmlPullParserException {
        String lessonXmlName = "lesson_" + Integer.toString(currentLesson.getID());
        xmlResourceParser = context.getResources().getXml(getResId(lessonXmlName, R.xml.class));
    }

    public void parseLesson() throws XmlPullParserException, IOException {

        ArrayList<Slide> slides = new ArrayList<>();
        ArrayList<Exercise> exercises = new ArrayList<>();
        Task task = null;

        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // slides pages parsing part
                if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("slide")) {
                    Slide newPage = parseSlides();
                    slides.add(newPage);
                }

                else if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("exercise")) {
                    Exercise newPage = parseExercise();
                    exercises.add(newPage);
                }

                else if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("task")) {
                    task = parseTask();
                }

                eventType = xmlResourceParser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> imageSources = new ArrayList<>();
        imageSources.add("good_job");
        Slide lastSlidePage = new Slide("", imageSources);
        slides.add(lastSlidePage);

        currentLesson.setSlides(slides);
        currentLesson.setExercises(exercises);
        currentLesson.setTask(task);
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

    private Slide parseSlides() throws XmlPullParserException, IOException {
        String text = getValue("text");
        List<String> imageSources = getValues("image");
        return new Slide(text, imageSources);
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

        return Exercise.createInstance(type, question, image, content, possibilities, answers);
    }


    private Task parseTask() throws IOException, XmlPullParserException {
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

    private List<String> getValues(String key) throws IOException, XmlPullParserException {
        xmlResourceParser.next();

        List<String> values = new ArrayList<>();
        while (!xmlResourceParser.getName().equals(key)) {
            xmlResourceParser.next();
        }
        while (xmlResourceParser.getEventType() != XmlPullParser.END_TAG &&
                xmlResourceParser.getName().equals(key)) {
            xmlResourceParser.next();
            values.add(xmlResourceParser.getText());
            xmlResourceParser.next();
            xmlResourceParser.next();
        }

        return values;
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
