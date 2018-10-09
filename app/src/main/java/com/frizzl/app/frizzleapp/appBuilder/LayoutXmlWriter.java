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

    public String writeXml(Map<Integer, UserCreatedView> viewsToWrite, String appIcon, String appName) {
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
            xmlSerializer.attribute("", "android:background", String.valueOf("@color/white"));
            xmlSerializer.attribute("", "tools:context", "com.frizzl.frizzlproject3.MainActivity");

            // open tag: <LinearLayout>
            xmlSerializer.startTag("", "LinearLayout");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/linearLayout"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("0dp"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("@dimen/app_bar_height"));
            xmlSerializer.attribute("", "android:background", String.valueOf("@color/frizzle_purple"));
            xmlSerializer.attribute("", "android:gravity", String.valueOf("center"));
            xmlSerializer.attribute("", "android:orientation", String.valueOf("horizontal"));
            xmlSerializer.attribute("", "android:paddingEnd", String.valueOf("20dp"));
            xmlSerializer.attribute("", "android:paddingStart", String.valueOf("20dp"));
            xmlSerializer.attribute("", "app:layout_constraintEnd_toEndOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintStart_toStartOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintTop_toTopOf", String.valueOf("parent"));

            // open and close tag: <ImageView>
            xmlSerializer.startTag("", "ImageView");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/app_icon"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("match_parent"));
            xmlSerializer.attribute("", "android:layout_weight", String.valueOf("1"));
            xmlSerializer.attribute("", "android:src", "@drawable/"+appIcon);
            xmlSerializer.endTag("", "ImageView");

            // open and close tag: <TextView>
            xmlSerializer.startTag("", "TextView");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/app_name_title"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_weight", String.valueOf("1"));
            xmlSerializer.attribute("", "android:gravity", String.valueOf("left"));
            xmlSerializer.attribute("", "android:text", appName);
            xmlSerializer.attribute("", "android:textAppearance", String.valueOf("@style/Text.Title.Light"));
            xmlSerializer.endTag("", "TextView");

            // close tag: <LinearLayout>
            xmlSerializer.endTag("", "LinearLayout");

            // open tag: <GridLayout>
            xmlSerializer.startTag("", "GridLayout");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/gridLayout"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("0dp"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("0dp"));
            xmlSerializer.attribute("", "app:layout_constraintEnd_toEndOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintStart_toStartOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintBottom_toBottomOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintTop_toBottomOf", String.valueOf("@id/linearLayout"));

            if (viewsToWrite != null ) {
                for (UserCreatedView view : viewsToWrite.values()) {
                    view.createXmlString(xmlSerializer);
                }
            }

            xmlSerializer.endTag("", "GridLayout");

            // open tag: <LinearLayout>
            xmlSerializer.startTag("", "LinearLayout");
            xmlSerializer.attribute("", "android:id", String.valueOf("@+id/backToFrizzlButton"));
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:gravity", String.valueOf("center"));
            xmlSerializer.attribute("", "app:layout_constraintEnd_toEndOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintStart_toStartOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "app:layout_constraintBottom_toBottomOf", String.valueOf("parent"));
            xmlSerializer.attribute("", "style", String.valueOf("@style/Button.BackToFrizzl"));

            // open and close tag: <ImageView>
            xmlSerializer.startTag("", "ImageView");
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("20dp"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("20dp"));
            xmlSerializer.attribute("", "android:layout_marginEnd", String.valueOf("8dp"));
            xmlSerializer.attribute("", "android:scaleType", String.valueOf("centerInside"));
            xmlSerializer.attribute("", "android:src", "@drawable/ic_back_to_frizzl_arrow");
            xmlSerializer.endTag("", "ImageView");

            // open and close tag: <TextView>
            xmlSerializer.startTag("", "TextView");
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:text", "Back To ");
            xmlSerializer.attribute("", "style", String.valueOf("@style/Text.BackToFrizzl"));
            xmlSerializer.endTag("", "TextView");

            // open and close tag: <ImageView>
            xmlSerializer.startTag("", "ImageView");
            xmlSerializer.attribute("", "android:layout_width", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:layout_height", String.valueOf("wrap_content"));
            xmlSerializer.attribute("", "android:src", "@drawable/ic_frizzle_logo_horizontal_small");
            xmlSerializer.endTag("", "ImageView");

            // close tag: <LinearLayout>
            xmlSerializer.endTag("", "LinearLayout");

            xmlSerializer.endDocument();

        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }

        return stringWriter.toString();
    }

    public String writeManifest(String appIcon, String appName) {
        xmlSerializer = Xml.newSerializer();
        stringWriter = new StringWriter();
        try {
            xmlSerializer.setOutput(stringWriter);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);

            // open tag: <manifest>
            xmlSerializer.startTag("", "manifest");
            xmlSerializer.attribute("", "xmlns:android", "http://schemas.android.com/apk/res/android");
            xmlSerializer.attribute("", "package", "com.frizzl.frizzlproject3");

            // open tag: <application>
            xmlSerializer.startTag("", "application");
            xmlSerializer.attribute("", "android:allowBackup", String.valueOf("true"));
            xmlSerializer.attribute("", "android:icon", String.valueOf("@drawable/"+appIcon));
            xmlSerializer.attribute("", "android:label", appName);

            // open tag: <activity>
            xmlSerializer.startTag("", "activity");
            xmlSerializer.attribute("", "android:name", String.valueOf(".MainActivity"));
            xmlSerializer.attribute("", "android:theme", String.valueOf("@style/AppTheme.NoActionBar"));

            // open tag: <intent-filter>
            xmlSerializer.startTag("", "intent-filter");
            xmlSerializer.attribute("", "android:name", String.valueOf(".MainActivity"));
            xmlSerializer.attribute("", "android:theme", String.valueOf("@style/AppTheme.NoActionBar"));

            // open and close tag: <action>
            xmlSerializer.startTag("", "action");
            xmlSerializer.attribute("", "android:name", String.valueOf("android.intent.action.MAIN"));
            xmlSerializer.endTag("", "action");

            // open and close tag: <category>
            xmlSerializer.startTag("", "category");
            xmlSerializer.attribute("", "android:name", String.valueOf("android.intent.category.LAUNCHER"));
            xmlSerializer.endTag("", "category");

            // close tag: <intent-filter>
            xmlSerializer.endTag("", "intent-filter");

            // close tag: <activity>
            xmlSerializer.endTag("", "activity");

            // close tag: <application>
            xmlSerializer.endTag("", "application");

            // close tag: <manifest>
            xmlSerializer.endTag("", "manifest");

            xmlSerializer.endDocument();

        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }

        return stringWriter.toString();
    }

    }
