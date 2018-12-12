package com.frizzl.app.frizzleapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Noga on 04/11/2018.
 */

public class CodeCheckUtils {
    private static final String functionIdentification = ContentUtils.functionIdentification;
    private static final String functionParams = ContentUtils.functionParams;
    private static final String speakOutIdentification = ContentUtils.speakOutIdentification;

    public static boolean checkIfContainsFunctionWithName(String code, String functionName) {
        boolean correct = code.contains(functionName.trim());
        // Contains 'speakOut'
        correct &= code.contains(ContentUtils.functionIdentification);
        return correct;
    }

    public static boolean checkIfContainsSpeakOutAndString(String code, String string, boolean shouldContain) {
        boolean correct;
        string = string.trim().toLowerCase();
        boolean containsString = code.toLowerCase().contains(string);
        correct = (shouldContain == containsString);
        // Contains 'speakOut'
        correct &= code.contains(speakOutIdentification);
        // Contains '("'
        code = code.replaceAll("\\s+", "");
        correct &= code.contains("(\"");
        // Contains '");'
        correct &= code.contains("\");");
        // Contains " twice
        int numOfParentheses = code.length() - code.replace("\"", "").length();
        correct &= (numOfParentheses == 2);
        return correct;
    }

    public static boolean checkIfFunctionSignatureIsValid(String code) {
        int i = code.indexOf(functionIdentification + " ");
        if (i < 0) return false;
        code = code.substring(i + (functionIdentification + " ").length(), code.length());
        i = code.indexOf(functionParams + "{");
        if (i < 0) return false;
        code = code.substring(i + functionParams.length(), code.length());
        i = code.indexOf("}");
        return i >= 0;
    }

    public static boolean checkIfSpeakOutIsInsideCurlyBrackets(String code) {
        int i = code.indexOf(speakOutIdentification);
        if (i < 0) return false;
        String beforeSpeakOut = code.substring(0, i);
        String afterSpeakOut = code.substring(i + speakOutIdentification.length(), code.length());
        return beforeSpeakOut.contains("{") && afterSpeakOut.contains("}");
    }

    public static boolean checkIfSpeakoutIsEmpty(String code) {
        String speakOutPrefix = speakOutIdentification + "(\"";
        int i = code.indexOf(speakOutPrefix);
        if (i < 0) return true;
        int nextCharIndex = i + speakOutPrefix.length();
        String nextChar = code.substring(nextCharIndex, nextCharIndex+1);
        return Objects.equals(nextChar, "\"");
    }

    public static boolean checkIfContainsFunctionsWithSameName(String code) {
        List<String> functionsList = extractDefinedFunctions(code);
        Set<String> functionsSet = new HashSet<>(functionsList);
        return functionsSet.size() < functionsList.size();
    }

    public static List<String> extractDefinedFunctions(String code) {
        List<String> functions = new ArrayList<>();
        int index = code.indexOf(functionIdentification);
        while (index >= 0) {
            String substring = code.substring(index, code.length());
            int functionNameEnd = substring.indexOf("(");
            if (functionNameEnd > 0) {
                String function = code.substring(
                        index + functionIdentification.length() + 1,
                        index + functionNameEnd);
                functions.add(function.trim());
            }
            index = code.indexOf(functionIdentification, index + 1);
        }
        return functions;
    }

    public static boolean checkIfFunctionInsideAFunction(String code) {
        int firstFunctionStart = code.indexOf(functionIdentification);
        int firstFunctionEnd = code.indexOf("}");
        String insideFunction = code.substring(firstFunctionStart + (functionIdentification).length(), firstFunctionEnd);
        return insideFunction.contains(functionIdentification);
    }
}
