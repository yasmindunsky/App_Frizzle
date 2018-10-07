package com.frizzl.app.frizzleapp.lesson;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasmin.dunsky on 21-Dec-17.
 */
public class PracticeContentParser {

    private XmlResourceParser xmlResourceParser;
    Practice currentPractice;

    public PracticeContentParser() throws XmlPullParserException {
    }

    public Practice parsePractice(Context context, int practiceID) throws XmlPullParserException, IOException {
        currentPractice = new Practice(practiceID);
        String lessonXmlName = "practice_" + practiceID;
        xmlResourceParser = context.getResources().getXml(getResId(lessonXmlName, R.xml.class));
        ArrayList<PracticeSlide> slides = new ArrayList<>();

        try {
            int eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // slides pages parsing part
                if (eventType == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("slide")) {
                    PracticeSlide slide = parseSlides();
                    slides.add(slide);
                }
                eventType = xmlResourceParser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPractice.setSlides(slides);
        return currentPractice;
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

    private PracticeSlide parseSlides() throws XmlPullParserException, IOException {
        String infoText = getValue("info_text");
        String taskText = getValue("task_text");
        String reminderText = getValue("reminder_text");
        Code code = getCode("code");
        String design = getValue("mutable_design");
        String callToActionText = getValue("call_to_action");
        boolean hasDesign = design == null ? false : true;
        return new PracticeSlide(infoText, reminderText, taskText, code, callToActionText, hasDesign);
    }

    private Code getCode(String key)throws XmlPullParserException, IOException  {
            xmlResourceParser.next();

            while (!xmlResourceParser.getName().equals(key)) {
                xmlResourceParser.next();
            }

            boolean mutable = xmlResourceParser.getAttributeBooleanValue(0, false);
            boolean runnable = xmlResourceParser.getAttributeBooleanValue(1, false);
            xmlResourceParser.next();
            String codeString = xmlResourceParser.getText();
            return new Code(mutable, runnable, codeString);

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
