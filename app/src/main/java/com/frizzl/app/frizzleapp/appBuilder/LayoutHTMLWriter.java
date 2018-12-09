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

class LayoutHTMLWriter {
    private XmlSerializer xmlSerializer;
    private StringWriter stringWriter;

    public String writeHTML(Map<Integer, UserCreatedView> viewsToWrite) {
        xmlSerializer = Xml.newSerializer();
        stringWriter = new StringWriter();
        try {
            xmlSerializer.setOutput(stringWriter);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);

            // open tag: <html>
            xmlSerializer.startTag("", "html");

            // open tag: <head>
            xmlSerializer.startTag("", "head");

            // open tag: <title>
            xmlSerializer.startTag("", "title");
            xmlSerializer.text("Confession Booth");
            xmlSerializer.endTag("", "title");

            // open tag: <style>
            xmlSerializer.startTag("", "style");
            xmlSerializer.text("@charset \"UTF-8\";\n" +
                    "\n" +
                    "\t\t\tbody {\n" +
                    "\t\t\t  /* Disable text seection and highlighting. */\n" +
                    "\t\t\t  -webkit-user-select: none; \n" +
                    "\t\t\t  -webkit-tap-highlight-color: transparent;\n" +
                    "\t\t\t  -webkit-touch-callout: none;\n" +
                    "\t\t\t\n" +
                    "\t\t\t  background: #edf0f1;\n" +
                    "\t\t\t  padding: 80px 0 0 0;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\tbody, input, button {\n" +
                    "\t\t\t  font-family: 'Roboto', sans-serif;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\tbutton {\n" +
                    "\t\t\t  width: 100%;\n" +
                    "\t\t\t  background-color: #cf4983;\n" +
                    "\t\t\t  border: none;\n" +
                    "\t\t\t  color: white;\n" +
                    "\t\t\t  padding: 20px;\n" +
                    "\t\t\t  text-align: center;\n" +
                    "\t\t\t  text-decoration: none;\n" +
                    "\t\t\t  display: inline-block;\n" +
                    "\t\t\t  font-size: 48px;\n" +
                    "\t\t\t  margin-top: 5%;\n" +
                    "\t\t\t  border-radius: 12px;\n" +
                    "\t\t\t  padding: 40px;\n" +
                    "\t\t\t\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\tdiv.title {\n" +
                    "\t\t\t  color: #cf4983;\n" +
                    "\t\t\t  font-size: 80px;\n" +
                    "\t\t\t  text-align: center;\n" +
                    "\t\t\t}\n" +
                    "\t\t\t\n" +
                    "\t\t\tdiv.explain {\n" +
                    "\t\t\t  font-size: 40px;\n" +
                    "\t\t\t  text-align: center;\n" +
                    "\t\t\t\n" +
                    "\t\t\t  padding-left:220px;\n" +
                    "\t\t\t  padding-right:220px;\n" +
                    "\t\t\t  padding-top:40px;\n" +
                    "\t\t\t  padding-bottom:40px;\n" +
                    "\t\t\t}" +
                    "img {\n" +
                    "\t\t\twidth:50%;\n" +
                    "\t\t\tmargin-left: 200px;\n" +
                    "\t\t\t}");
            xmlSerializer.endTag("", "style");

            xmlSerializer.endTag("", "head");

            // open tag: <body>
            xmlSerializer.startTag("", "body");

            // open tag: <div class="title">
            xmlSerializer.startTag("", "div");
            xmlSerializer.attribute("", "class", "title");
            xmlSerializer.text("Confession Booth");
            xmlSerializer.endTag("", "div");

            // open tag: <div style="width:80%; margin:auto;">
            xmlSerializer.startTag("", "div");
            xmlSerializer.attribute("", "style", "width:80%; margin:auto;");

            if (viewsToWrite != null ) {
                for (UserCreatedView view : viewsToWrite.values()) {
                    view.createHTMLString(xmlSerializer);
                }
            }

            xmlSerializer.endTag("", "div");
            xmlSerializer.endTag("", "body");
            xmlSerializer.endTag("", "html");

            xmlSerializer.endDocument();

        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }

        return stringWriter.toString();
    }


    }
