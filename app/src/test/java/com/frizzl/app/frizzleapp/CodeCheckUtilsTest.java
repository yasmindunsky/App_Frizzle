package com.frizzl.app.frizzleapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Noga on 12/11/2018.
 */
public class CodeCheckUtilsTest {
    @Test
    public void checkIfFunctionInsideAFunction_contains_true() throws Exception {
        String code = "function funcName(element){function funcName2(element){}}";
        boolean answer = CodeCheckUtils.checkIfFunctionInsideAFunction(code);
        assertEquals(true, answer);

        code = "function funcName(element)function funcName2(element){}{}";
        answer = CodeCheckUtils.checkIfFunctionInsideAFunction(code);
        assertEquals(true, answer);

        code = "function funcName(function funcName2(element){}){}";
        answer = CodeCheckUtils.checkIfFunctionInsideAFunction(code);
        assertEquals(true, answer);

        code = "function funcName function funcName2(element){}(element){}";
        answer = CodeCheckUtils.checkIfFunctionInsideAFunction(code);
        assertEquals(true, answer);

        code = "functionfunction funcName2(element){} funcName (element){}";
        answer = CodeCheckUtils.checkIfFunctionInsideAFunction(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfContainsFunctionsWithSameName_contains_true() throws Exception {
        String code = "function funcName(element){speakOut(\"hello\");" +
                "function funcName(element){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfContainsFunctionsWithSameName(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfContainsFunctionsWithSameName_doesntContain_false() throws Exception {
        String code = "function funcName(element){speakOut(\"hello\");";
        boolean answer = CodeCheckUtils.checkIfContainsFunctionsWithSameName(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfContainsFunctionWithName_contains_true() throws Exception {
        String code = "function funcName(element){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfContainsFunctionWithName(code, "funcName");
        assertEquals(true, answer);
    }

    @Test
    public void checkIfContainsFunctionWithName_doesntContain_false() throws Exception {
        String code = "function otherName(element){speakOut(\"hello\");}";
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
        String code = "function otherName(element){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfFunctionSignatureIsValid(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfFunctionSignatureIsValid_missingSpace_false() throws Exception {
        String code = "functionotherName(element){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfFunctionSignatureIsValid(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isInside_true() throws Exception {
        String code = "function funcName(element){speakOut(\"hello\");}";
        boolean answer = CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isAfter_false() throws Exception {
        String code = "function funcName(element){}speakOut(\"hello\");";
        boolean answer = CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(code);
        assertEquals(false, answer);
    }

    @Test
    public void checkIfSpeakOutIsInsideCurlyBrackets_isBefore_false() throws Exception {
        String code = "function funcNamespeakOut(\"hello\");(element){}";
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