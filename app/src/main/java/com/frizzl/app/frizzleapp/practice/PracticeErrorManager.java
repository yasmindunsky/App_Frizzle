package com.frizzl.app.frizzleapp.practice;

import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.ContentUtils;
import com.frizzl.app.frizzleapp.FrizzlApplication;
import com.frizzl.app.frizzleapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Noga on 04/11/2018.
 */

class PracticeErrorManager {

    interface ErrorCheck {
        String check(String originalCode, String currentCode);
    }

    // Returns null if error1 was not found, or Error to display.
    private static final ErrorCheck errorCheck0 = (originalCode, currentCode) -> "0";

    // Returns null if error was not found, or Error to display.
    // Checks if the currentCode is identical to the originalCode
    private static final ErrorCheck ERROR_GENERAL = (originalCode, currentCode) -> {
        if (originalCode.equals(currentCode)) {
            return FrizzlApplication.resources.getString(R.string.error_read_instructions);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if the user deleted the speakOut command and left only the text inside
    private static final ErrorCheck ERROR_SPEAKOUT_MISSING = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "", true))
        {
            return FrizzlApplication.resources.getString(R.string.error_add_speakout);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if a semicolon is missing
    private static final ErrorCheck ERROR_SPEAKOUT_SEMICOLON_MISSING = (originalCode, currentCode) -> {
        if (!currentCode.contains(";"))
        {
            return FrizzlApplication.resources.getString(R.string.error_add_speakout);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if an unnecessary speakOut is there
    private static final ErrorCheck ERROR_FUNCTION_UNNECESSARY_SPEAKOUT = (originalCode, currentCode) -> {
        if (CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "", true))
        {
            return FrizzlApplication.resources.getString(R.string.error_delete_speakout_add_function);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if the user changed the white parts of the function
    private static final ErrorCheck ERROR_FUNCTION_CHANGED_WHITE = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfFunctionSignatureIsValid(currentCode))
        {
            return FrizzlApplication.resources.getString(R.string.error_dont_change_function);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if the speakOut command is not in the right place
    private static final ErrorCheck ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(currentCode))
        {
            return FrizzlApplication.resources.getString(R.string.error_speakout_brackets);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if the speakOut command had a text other than 'this is so cool'
    private static final ErrorCheck ERROR_SPEAKOUT_NOT_THIS_IS_SO_COOL = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "this is so cool", true))
        {
            return FrizzlApplication.resources.getString(R.string.error_speakout_not_this_is_so_cool);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if the speakOut command had a text other than 'this is so cool'
    private static final ErrorCheck ERROR_FUNCTION_NOT_MYFUNCTION = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfContainsFunctionWithName(currentCode, "myFunction"))
        {
            return FrizzlApplication.resources.getString(R.string.error_function_not_myfunction);
        }
        return null;
    };

    private static Map<String, ErrorCheck[]> levelAndSlideToChecks;

    private static void init(){
        levelAndSlideToChecks = new HashMap<>();
        levelAndSlideToChecks.put(ContentUtils.SPEAKOUT_PRACTICE_LEVEL_ID + "_1",
                new ErrorCheck[]{ERROR_GENERAL,
                        ERROR_SPEAKOUT_MISSING,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING});
        levelAndSlideToChecks.put(ContentUtils.SPEAKOUT_PRACTICE_LEVEL_ID + "_2",
                new ErrorCheck[]{ERROR_GENERAL,
                        ERROR_SPEAKOUT_MISSING,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING,
                        ERROR_SPEAKOUT_NOT_THIS_IS_SO_COOL});
        levelAndSlideToChecks.put(ContentUtils.ONCLICK_PRACTICE_LEVEL_ID + "_5",
                new ErrorCheck[]{ERROR_GENERAL,
                        ERROR_FUNCTION_UNNECESSARY_SPEAKOUT,
                        ERROR_FUNCTION_CHANGED_WHITE});
        levelAndSlideToChecks.put(ContentUtils.ONCLICK_PRACTICE_LEVEL_ID + "_7",
                new ErrorCheck[]{ERROR_GENERAL,
                        ERROR_FUNCTION_UNNECESSARY_SPEAKOUT,
                        ERROR_FUNCTION_CHANGED_WHITE,
                        ERROR_FUNCTION_NOT_MYFUNCTION});
        levelAndSlideToChecks.put(ContentUtils.ONCLICK_PRACTICE_LEVEL_ID + "_8",
                new ErrorCheck[]{ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE,
                        ERROR_GENERAL,
                        ERROR_SPEAKOUT_MISSING,
                        ERROR_FUNCTION_CHANGED_WHITE});
        levelAndSlideToChecks.put(ContentUtils.ONCLICK_PRACTICE_LEVEL_ID + "_9",
                new ErrorCheck[]{});
    }

    public static String getError(int currentLevel, int currentSlide, String originalCode, String currentCode) {
        boolean initialized = false;
        if (!initialized) init();

        String key = currentLevel + "_" + currentSlide;
        if (!levelAndSlideToChecks.containsKey(key)) return null;
        ErrorCheck[] checksToPerform = levelAndSlideToChecks.get(key);
        for (ErrorCheck errorCheck : checksToPerform) {
            String checkResult = errorCheck.check(originalCode, currentCode);
            if (checkResult != null) return checkResult;
        }
        return FrizzlApplication.resources.getString(R.string.error_read_instructions);
    }
}
