package com.frizzl.app.frizzleapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Noga on 13/12/2018.
 */
public class ErrorManagerTest {
    @Test
    public void getPracticeError() throws Exception {
    }

    @Test
    public void getBuildError() throws Exception {
        String originalCode = "";
        String result = ErrorManager.ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE.check(originalCode, "");
        assertEquals(null,
                result);
    }

}