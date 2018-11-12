package com.frizzl.app.frizzleapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Noga on 12/11/2018.
 */
public class CodeCheckUtilsTest {
    @Test
    public void checkIfSpeakoutIsntEmpty_speakOutEmpty_true() throws Exception {
        String code = "speakOut(\"\");";
        boolean answer = CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
        assertEquals(true, answer);
    }

    @Test
    public void checkIfSpeakoutIsntEmpty_speakOutNotEmpty_false() throws Exception {
        String code = "speakOut(\"Hello\");";
        boolean answer = CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
        assertEquals(false, answer);
    }

}