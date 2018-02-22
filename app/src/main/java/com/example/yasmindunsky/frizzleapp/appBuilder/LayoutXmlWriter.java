package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
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


            if(!viewsToWrite.values().isEmpty()) {
                // open tag: <GridLayout>
                xmlSerializer.startTag("", "GridLayout");
                xmlSerializer.attribute("", "android:id", String.valueOf("@+id/gridLayout"));
                xmlSerializer.attribute("", "android:layout_width", String.valueOf("match_parent"));
                xmlSerializer.attribute("", "android:layout_height", String.valueOf("match_parent"));

                for (UserCreatedView view : viewsToWrite.values()) {
                    view.createXmlString(xmlSerializer);
//                addElement(view);
                }
            }

            xmlSerializer.endDocument();

        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }

        return stringWriter.toString();
    }

    private void addElement(View view) {
        String name = "";
        String text = "";
        String fontFamily = "serif";
        String background = "";
        if (view.getClass().equals(TextView.class)) {
            TextView tv = (TextView)view;
            name = "TextView";
            text = (String) tv.getText();
            fontFamily = (String) tv.getTag(R.id.usersViewFontFamily);
            background = "@android:color/transparent";
        }
        else if (view.getClass().equals(Button.class)) {
            Button b = (Button)view;
            name = "Button";
            text = (String) b.getText();
            fontFamily = (String) b.getTag(R.id.usersViewFontFamily);
            background = "@drawable/user_button_background";
        }

        try {
            xmlSerializer.startTag("", name);
            xmlSerializer.attribute("", "android:id", "@+id/" + view.getTag(R.id.usersViewId).toString());
            xmlSerializer.attribute("", "android:layout_width", "wrap_content");
            xmlSerializer.attribute("", "android:layout_height", "wrap_content");
            xmlSerializer.attribute("", "android:margins", "10");
            xmlSerializer.attribute("", "android:text", text);
            xmlSerializer.attribute("", "android:fontFamily", fontFamily);
            xmlSerializer.attribute("", "android:background", background);
            xmlSerializer.attribute("", "android:backgroundTint", "@android:color/" + view.getTag(R.id.usersViewBgColor).toString());
            xmlSerializer.attribute("", "android:layout_column", view.getTag(R.id.usersViewCol).toString());
            xmlSerializer.attribute("", "android:layout_row", view.getTag(R.id.usersViewRow).toString());
            xmlSerializer.endTag("", name);
        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }
    }
}
