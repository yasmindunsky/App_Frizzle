package com.frizzl.app.frizzleapp.lesson;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.Design;
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
class PracticeContentParser {

    private XmlResourceParser xmlResourceParser;
    private Practice currentPractice;

    PracticeContentParser() throws XmlPullParserException {
    }

    Practice parsePractice(Context context, int practiceID, String lessonXmlName) throws XmlPullParserException, IOException {
        currentPractice = new Practice(practiceID);
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
        Code code = getCode();
        Design design = getDesign();
        String callToActionText = getValue("call_to_action");
        return new PracticeSlide(infoText, reminderText, taskText, code, callToActionText, design);
    }

    private Code getCode()throws XmlPullParserException, IOException  {
        String key = "code";
            xmlResourceParser.next();

            while (!xmlResourceParser.getName().equals(key)) {
                xmlResourceParser.next();
            }

            boolean shows = xmlResourceParser.getAttributeBooleanValue(2, false);
            if (!shows) {
                xmlResourceParser.next();
                return null;
            }
            boolean mutable = xmlResourceParser.getAttributeBooleanValue(0, false);
            boolean runnable = xmlResourceParser.getAttributeBooleanValue(1, false);
            boolean waitForCTA = xmlResourceParser.getAttributeBooleanValue(3, false);
            xmlResourceParser.next();
            String codeString = xmlResourceParser.getText();
            return new Code(mutable, runnable, waitForCTA, codeString);

    }

    private Design getDesign()throws XmlPullParserException, IOException  {
        String key = "design";
        xmlResourceParser.next();

        while (!xmlResourceParser.getName().equals(key)) {
            xmlResourceParser.next();
        }

        boolean shows = xmlResourceParser.getAttributeBooleanValue(1, false);
        if (!shows) {
            xmlResourceParser.next();
            return null;
        }
        boolean runnable = xmlResourceParser.getAttributeBooleanValue(0, false);
        boolean withOnClickSet = xmlResourceParser.getAttributeBooleanValue(2, false);
        xmlResourceParser.next();
        return new Design(runnable, withOnClickSet);
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
