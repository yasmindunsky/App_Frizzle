package com.frizzl.app.frizzleapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Noga on 12/11/2018.
 */
public class CodeCheckUtilsTest {
    @Test
    public void checkIfContainsFunctionWithName_contains_true() throws Exception {
        String code = "public void funcName(View view){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfContainsFunctionWithName(code, "funcName");
        assertEquals(true, answer);
    }

    @Test
    public void checkIfContainsFunctionWithName_doesntContain_false() throws Exception {
        String code = "public void otherName(View view){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfContainsFunctionWithName(code, "funcName");
        assertEquals(false, answer);
    }

    @Test
    public void checkIfContainsSpeakOutAndString_containsAndShould_true() throws Exception {
        String code = "speakOut(\"hello\");";
        boolean answer = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "hello", true);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfContainsSpeakOutAndString_doesntContainAndShould_false() throws Exception {
        String code = "speakOut(\"goodbye\");";
        boolean answer = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "hello", true);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfContainsSpeakOutAndString_containsAndShouldnt_false() throws Exception {
        String code = "speakOut(\"hello\");";
        boolean answer = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "hello", false);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfContainsSpeakOutAndString_doesntContainAndShouldnt_true() throws Exception {
        String code = "speakOut(\"goodbye\");";
        boolean answer = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "hello", false);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfFunctionSignatureIsValid_valid_true() throws Exception {
        String code = "public void otherName(View view){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfFunctionSignatureIsValid(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfFunctionSignatureIsValid_missingSpace_false() throws Exception {
        String code = "public voidotherName(View view){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfFunctionSignatureIsValid(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isInside_true() throws Exception {
        String code = "public void funcName(View view){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isAfter_false() throws Exception {
        String code = "public void funcName(View view){}speakOut(\"hello\");";
        boolean answer = CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isBefore_false() throws Exception {
        String code = "public void funcNamespeakOut(\"hello\");(View view){}";
        boolean answer = CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakoutIsEmpty_speakOutEmpty_true() throws Exception {
        String code = "speakOut(\"\");";
        boolean answer = CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfSpeakoutIsEmpty_speakOutNotEmpty_false() throws Exception {
        String code = "speakOut(\"Hello\");";
        boolean answer = CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakoutIsEmpty_noCode_true() throws Exception {
        String code = "";
        boolean answer = CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
        assertEquals(true, answer);
    }

}