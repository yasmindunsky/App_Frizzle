package com.frizzl.app.frizzleapp;

/**
 * Created by Noga on 14/11/2018.
 */

public class ContentUtils {
    public final static int FIRST_PRACTICE_LEVEL_ID = 1;
    public final static int SPEAKOUT_PRACTICE_LEVEL_ID = 2;
    public final static int ONCLICK_PRACTICE_LEVEL_ID = 3;
    public final static int CONFESSIONS_APP_LEVEL_ID = 4;
    public final static int FRIENDSHIP_APP_LEVEL_ID = 8;

    public final static String functionIdentification = "function";
    public final static String functionParams = "()";
    public final static String speakOutIdentification = "speakOut";

    public final static String codePrefix = "if ('serviceWorker' in navigator) {\n" +
            "    navigator.serviceWorker.register('service-worker.js', {\n" +
            "        scope: '.' // <--- THIS BIT IS REQUIRED\n" +
            "    }).then(function(registration) {\n" +
            "        // Registration was successful\n" +
            "        console.log('ServiceWorker registration successful with scope: ', registration.scope);\n" +
            "    }, function(err) {\n" +
            "        // registration failed :(\n" +
            "        console.log('ServiceWorker registration failed: ', err);\n" +
            "    });\n" +
            "}" +
            "function speakOut (value) {\n" +
            "    var msg = new SpeechSynthesisUtterance(value);\n" +
            "\twindow.speechSynthesis.speak(msg)\n" +
            "}";
}
