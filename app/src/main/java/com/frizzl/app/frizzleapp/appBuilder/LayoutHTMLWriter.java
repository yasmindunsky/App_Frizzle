package com.frizzl.app.frizzleapp.appBuilder;

import android.util.Log;
import android.util.Xml;

import com.frizzl.app.frizzleapp.ContentUtils;
import com.frizzl.app.frizzleapp.ViewUtils;

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

    public String writeHTML(Map<Integer, UserCreatedView> viewsToWrite,
                            String jsCode,
                            String appTitle,
                            String appIcon) {
        String iconURL = ViewUtils.iconNameToAddress(appIcon);
        jsCode = ContentUtils.codePrefix + jsCode;
        xmlSerializer = Xml.newSerializer();
        stringWriter = new StringWriter();
        try {
            xmlSerializer.setOutput(stringWriter);
            // start DOCUMENT
//            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag("", "!DOCTYPE html");

            // open tag: <html>
            xmlSerializer.startTag("", "html");

            // open tag: <head>
            xmlSerializer.startTag("", "head");

            // open tag: <title>
            xmlSerializer.startTag("", "title");
            xmlSerializer.text("Confession Booth");
            xmlSerializer.endTag("", "title");


            // open tag: <style>
            xmlSerializer.startTag("", "script");
            xmlSerializer.text(jsCode);
            xmlSerializer.endTag("", "script");

            // open tag: <style>
            xmlSerializer.startTag("", "style");
            xmlSerializer.text("@charset \"UTF-8\";" +
                    
                    "body {" +
                    "  /* Disable text seection and highlighting. */" +
                    "  -webkit-user-select: none; " +
                    "  -webkit-tap-highlight-color: transparent;" +
                    "  -webkit-touch-callout: none;" +
                    
                    "  background: #edf0f1;" +
                    "  padding: 0 0 0 0;" +
                    "}" +
                    
                    "body, input, button {" +
                    "  font-family: 'Roboto', sans-serif;" +
                    "}" +
                    
                    "button {\n" +
                    "  width: 100%;\n" +
                    "  background-color: #f84685;\n" +
                    "  border: none;\n" +
                    "  color: white;\n" +
                    "  padding: 20px;\n" +
                    "  text-align: center;\n" +
                    "  text-decoration: none;\n" +
                    "  display: inline-block;\n" +
                    "  font-size: 55px;\n" +
                    "  font-family: calibri;\n" +
                    "  margin-top: 5%;\n" +
                    "  border-radius: 12px;\n" +
                    "  padding: 40px;\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "div.title {\n" +
                    "\t\t\t\tfont-size: 60px;\n" +
                    "\t\t\t\tbackground-color: #1b2974;\n" +
                    "\t\t\t\tcolor: #FFFFFF;\n" +
                    "\t\t\t\tfont-family: hero;\n" +
                    "\t\t\t\ttext-transform: uppercase;\n" +
                    "\t\t\t\t\n" +
                    "\t\t\t\tclear: both;\n" +
                    "\t\t\t\ttext-align: center;\n" +
                    "\t\t\t\theight: 150px;\n" +
                    "\t\t\t\tline-height: 150px;\n" +
                    "\t\t\t\tmargin-bottom: 50px;\n" +
                    "\t\t\t\t}\n" +
                    "\n" +
                    "\t\t\t\n" +
                    "\t\t\t.icon{\n" +
                    "\t\t\t\tvertical-align: middle;\n" +
                    "\t\t\t\tdisplay: inline-block;\n" +
                    "\t\t\t\theight: 150px;\n" +
                    "\t\t\t}" +
                    
                    "div.explain {" +
                    "  font-size: 40px;" +
                    "  text-align: center;" +
                    
                    "  padding-left:220px;" +
                    "  padding-right:220px;" +
                    "  padding-top:40px;" +
                    "  padding-bottom:40px;" +
                    "}" +
                    "img {" +
                    "width:50%;" +
                    "margin-left: 200px;" +
                    "}");
            xmlSerializer.endTag("", "style");

            xmlSerializer.endTag("", "head");

            // open tag: <body>
            xmlSerializer.startTag("", "body");

            // open tag: <div class="title">
            xmlSerializer.startTag("", "div");
            xmlSerializer.attribute("", "class", "title");
            // open tag: <div class="span">
            xmlSerializer.startTag("", "span");
            // open tag: <div class="img">
            xmlSerializer.startTag("", "img");
            xmlSerializer.attribute("", "class", "icon");
            xmlSerializer.attribute("", "src", iconURL);
            xmlSerializer.endTag("", "img");
            xmlSerializer.endTag("", "span");
            xmlSerializer.text(appTitle);
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
