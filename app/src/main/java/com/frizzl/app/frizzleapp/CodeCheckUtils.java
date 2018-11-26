package com.frizzl.app.frizzleapp;

import java.util.Objects;

/**
 * Created by Noga on 04/11/2018.
 */

public class CodeCheckUtils {
    public static boolean checkIfContainsFunctionWithName(String code, String functionName) {
        boolean correct = code.contains(functionName.trim());
        // Contains 'speakOut'
        correct &= code.contains("public void");
        return correct;
    }

    public static boolean checkIfContainsSpeakOutAndString(String code, String string, boolean shouldContain) {
        boolean correct;
        string = string.trim().toLowerCase();
        boolean containsString = code.toLowerCase().contains(string);
        correct = (shouldContain == containsString);
        // Contains 'speakOut'
        correct &= code.contains("speakOut");
        // Contains '("'
        code = code.replaceAll("\\s+", "");
        correct &= code.contains("(\"");
        // Contains '");'
        correct &= code.contains("\");");
        return correct;
    }

    public static boolean checkIfFunctionSignatureIsValid(String code) {
        int i = code.indexOf("public void ");
        if (i < 0) return false;
        code = code.substring(i + "public void ".length(), code.length());
        i = code.indexOf("(View view){");
        if (i < 0) return false;
        code = code.substring(i + "(View view)".length(), code.length());
        i = code.indexOf("}");
        return i >= 0;
    }

    public static boolean checkIfSpeakOutIsInsideCurlyBrackets(String code) {
        int i = code.indexOf("speakOut");
        if (i < 0) return false;
        String beforeSpeakOut = code.substring(0, i);
        String afterSpeakOut = code.substring(i + "speakOut".length(), code.length());
        return beforeSpeakOut.contains("{") && afterSpeakOut.contains("}");
    }

    public static boolean checkIfSpeakoutIsEmpty(String code) {
        String speakOutPrefix = "speakOut(\"";
        int i = code.indexOf(speakOutPrefix);
        if (i < 0) return true;
        int nextCharIndex = i + speakOutPrefix.length();
        String nextChar = code.substring(nextCharIndex, nextCharIndex+1);
        return Objects.equals(nextChar, "\"");
    }
}
