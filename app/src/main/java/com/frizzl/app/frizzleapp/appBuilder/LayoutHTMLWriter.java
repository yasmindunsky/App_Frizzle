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
            
            // open tag: <meta>
            xmlSerializer.startTag("", "meta");
            xmlSerializer.attribute("", "property", "og:image");
            xmlSerializer.attribute("", "content", iconURL);
            xmlSerializer.endTag("", "meta");

//            // Viewport
//            xmlSerializer.startTag("", "meta");
//            xmlSerializer.attribute("", "name", "viewport");
//            xmlSerializer.attribute("", "content", "width=device-width, initial-scale=1.0");
//            xmlSerializer.endTag("", "meta");

            // Google fonts
            xmlSerializer.startTag("", "link");
            xmlSerializer.attribute("", "href", "https://fonts.googleapis.com/css?family=Open+Sans:700|Rubik");
            xmlSerializer.attribute("", "rel", "stylesheet");
            xmlSerializer.endTag("", "link");

            // Manifest
            xmlSerializer.startTag("", "link");
            xmlSerializer.attribute("", "rel", "manifest");
            xmlSerializer.attribute("", "href", "data:application/manifest+json,{" +
                    "\"short_name\": \"Confession Booth\"," +
                    "\"name\": \"Confession Booth\"," +
                    "\"icons\": [" +
            "{" +
                    "\"src\":" + iconURL +
                    "\"type\": \"image/png\"," +
                    "\"sizes\": \"192x192\"" +
            "},"+
            "{" +
                "\"src\":" + iconURL +
                    "\"type\": \"image/png\","+
                    "\"sizes\": \"512x512\"" +
            "}]," +
            "\"background_color\": \"#1b2974\"," +
             "\"display\": \"standalone\"," +
             "\"theme_color\": \"#1b2974\" +" +
        "}");
            xmlSerializer.endTag("", "link");

            // open tag: <style>
            xmlSerializer.startTag("", "script");
            xmlSerializer.text(jsCode);
            xmlSerializer.endTag("", "script");

            // open tag: <style>
            xmlSerializer.startTag("", "style");
            xmlSerializer.text("@charset \"UTF-8\";" +
                    
                    "body {" +
                    "  /* Disable text selection and highlighting. */" +
                    "  -webkit-user-select: none; " +
                    "  -webkit-tap-highlight-color: transparent;" +
                    "  -webkit-touch-callout: none;" +
                    
                    "  background: #edf0f1;" +
                    "  padding: 0 0 0 0;" +
                    "}" +
                    
                    "body, input, button {" +
                    "  font-family: 'Open Sans', sans-serif;" +
                    "}" +
                    
                    "button {" +
                    "  width: 100%;" +
                    "  border: none;" +
                    "  padding: 20px;" +
                    "  text-align: center;" +
                    "  text-decoration: none;" +
                    "  display: inline-block;" +
                    "  font-size: 55px;" +
                    "  margin-top: 3%;" +
                    "  margin-bottom: 3%;" +
                    "  border-radius: 12px;" +
                    "  padding: 40px;" +
                    "" +
                    "}" +
                    "" +
                    "div.title {" +
                    "font-size: 60px;" +
                    "background-color: #1b2974;" +
                    "color: #FFFFFF;" +
                    "font-family: 'Rubik', sans-serif;" +
                    "text-transform: uppercase;" +
                    "" +
                    "clear: both;" +
                    "text-align: center;" +
                    "height: 150px;" +
                    "line-height: 150px;" +
                    "margin-bottom: 50px;" +
                    "}" +
                    "" +
                    "" +
                    ".icon{" +
                    "vertical-align: middle;" +
                    "display: inline-block;" +
                    "height: 150px;" +
                    "}" +
                    
                    "div.explain {" +
                    "  font-size: 40px;" +
                    "  text-align: center;" +
                    
                    "  padding-left:220px;" +
                    "  padding-right:220px;" +
                    "  padding-top:40px;" +
                    "  padding-bottom:40px;" +
                    "}" +
                    ".userImg {" +
                    "width: 50vw;" +
                    "height: 50vw;" +
                    "  margin-top: 3%;" +
                    "  margin-bottom: 3%;" +
                    "display: block;" +
                    "  margin-left: auto;" +
                    "  margin-right: auto;" +
                    "  object-fit: cover;" +
                    "}" +
                    "p {" +
                    "font-size: 60px;" +
                    " }" +
                    "");
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
