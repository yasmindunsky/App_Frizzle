package com.frizzl.app.frizzleapp;

import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.ContentUtils;
import com.frizzl.app.frizzleapp.FrizzlApplication;
import com.frizzl.app.frizzleapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 04/11/2018.
 */

public class ErrorManager {
    private static boolean initialized = false;

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
    public static final ErrorCheck ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(currentCode)
                && CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "", true))
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
    // Checks if there are two functions with the same name.
    private static final ErrorCheck ERRORֹֹֹ_FUNCTIONS_WITH_SAME_NAME = (originalCode, currentCode) -> {
        if (CodeCheckUtils.checkIfContainsFunctionsWithSameName(currentCode))
        {
            return FrizzlApplication.resources.getString(R.string.error_functions_with_same_name);
        }
        return null;
    };

    // Returns null if error was not found, or Error to display.
    // Checks if there are two functions one inside the other.
    private static final ErrorCheck ERRORֹֹֹ_FUNCTION_INSIDE_FUNCTION = (originalCode, currentCode) -> {
        if (!CodeCheckUtils.checkIfFunctionInsideAFunction(currentCode))
        {
            return FrizzlApplication.resources.getString(R.string.error_function_inside_function);
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
    private static Map<String, String> taskHints;

    private static void init(){
        initialized = true;
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

        Map<String, ErrorCheck[]> buildTaskToChecks = new HashMap<>();
        buildTaskToChecks.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_2",
                new ErrorCheck[]{ERROR_FUNCTION_CHANGED_WHITE,
                        ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING,
                        ERRORֹֹֹ_FUNCTIONS_WITH_SAME_NAME,
                        ERRORֹֹֹ_FUNCTION_INSIDE_FUNCTION
                });
        buildTaskToChecks.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_3",
                new ErrorCheck[]{ERROR_FUNCTION_CHANGED_WHITE,
                        ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING,
                        ERRORֹֹֹ_FUNCTIONS_WITH_SAME_NAME,
                        ERRORֹֹֹ_FUNCTION_INSIDE_FUNCTION
                });
        buildTaskToChecks.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_4",
                new ErrorCheck[]{ERROR_FUNCTION_CHANGED_WHITE,
                        ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING,
                        ERRORֹֹֹ_FUNCTIONS_WITH_SAME_NAME,
                        ERRORֹֹֹ_FUNCTION_INSIDE_FUNCTION
                });
        buildTaskToChecks.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_5",
                new ErrorCheck[]{ERROR_FUNCTION_CHANGED_WHITE,
                        ERROR_FUNCTION_SPEAKOUT_IN_WRONG_PLACE,
                        ERROR_SPEAKOUT_SEMICOLON_MISSING,
                        ERRORֹֹֹ_FUNCTIONS_WITH_SAME_NAME,
                        ERRORֹֹֹ_FUNCTION_INSIDE_FUNCTION
                });

        taskHints = new HashMap<>();
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_0",
                FrizzlApplication.resources.getString(R.string.hint_confession_0));
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_1",
                FrizzlApplication.resources.getString(R.string.hint_confession_1));
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_2",
                FrizzlApplication.resources.getString(R.string.hint_confession_2));
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_3",
                FrizzlApplication.resources.getString(R.string.hint_confession_3));
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_4",
                FrizzlApplication.resources.getString(R.string.hint_confession_4));
        taskHints.put(ContentUtils.CONFESSIONS_APP_LEVEL_ID + "_5",
                FrizzlApplication.resources.getString(R.string.hint_confession_5));
    }

    public static String getPracticeError(int currentLevel, int currentSlide, String originalCode, String currentCode) {
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

    public static String getBuildError(int currentApp, int currentTask, String originalCode, String currentCode) {
        if (!initialized) init();

        String key = currentApp + "_" + currentTask;
//        if (buildTaskToChecks.containsKey(key)) {
//            ErrorCheck[] checksToPerform = buildTaskToChecks.get(key);
//            for (ErrorCheck errorCheck : checksToPerform) {
//                String checkResult = errorCheck.check(originalCode, currentCode);
//                if (checkResult != null) return checkResult;
//            }
//        }
        // Currently returning only hints.
        return taskHints.get(key);
    }
}
