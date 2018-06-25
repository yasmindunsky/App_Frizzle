package com.frizzl.app.frizzleapp.appBuilder;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Noga on 30/01/2018.
 */

class LayoutXmlWriter {
    private XmlSerializer xmlSerializer;
    private StringWriter stringWriter;

    String writeXml(Map<Integer, UserCreatedView> viewsToWrite) {
        xmlSerializer = Xml.newSerializer();
        stringWriter = new StringWriter();
        try {
            xmlSerializer.setOutput(stringWriter);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);

            // open tag: <android.support.constraint.ConstraintLayout>
            xmlSerializer.startTag("", "android.support.constraint.ConstraintLayout");
            xmlSerializer.attribute("", "xmlns:android", "http://schemas.android.com/apk/res/android");
            xmlSerializer.attribute("", "xmlns:app", "http://schemas.android.com/apk/res-auto");
            xmlSerializer.attribute("", "xmlns:tools", "http://schemas.android.com/tools");
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("match_parent"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("match_parent"));
            xmlSerializer.attribute("", "tools:context", "co.frizzle.frizzleproject1.MainActivity");

            // open tag: <GridLayout>
            xmlSerializer.startTag("", "GridLayout");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/gridLayout"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("match_parent"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("match_parent"));

            if (viewsToWrite != null ) {
                for (UserCreatedView view : viewsToWrite.values()) {
                    view.createXmlString(xmlSerializer);
                }
            }

            xmlSerializer.endTag("", "GridLayout");

            xmlSerializer.endDocument();

        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }

        return stringWriter.toString();
    }
}
