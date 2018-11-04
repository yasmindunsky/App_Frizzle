package com.frizzl.app.frizzleapp.practice;

import android.content.res.Resources;

import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.FrizzlApplication;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Noga on 04/11/2018.
 */

public class PracticeErrorManager {
    private static boolean initialized = false;

    interface ErrorCheck {
        String check(String originalCode, String currentCode);
    }

    // Returns null if error1 was not found, or Error to display.
    private static ErrorCheck errorCheck0 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            return "0";
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if the currentCode is identical to the originalCode
    private static ErrorCheck errorCheck1 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (originalCode.equals(currentCode)) {
                return FrizzlApplication.resources.getString(R.string.error_read_instructions);
            }
            return null;
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if the user deleted the speakOut command and left only the text inside
    private static ErrorCheck errorCheck2 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (!CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "", true))
            {
                return FrizzlApplication.resources.getString(R.string.error_add_speakout);
            }
            return null;
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if a semicolon is missing
    private static ErrorCheck errorCheck3 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (!currentCode.contains(";"))
            {
                return FrizzlApplication.resources.getString(R.string.error_add_speakout);
            }
            return null;
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if an unnecessary speakOut is there
    private static ErrorCheck errorCheck4 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (CodeCheckUtils.checkIfContainsSpeakOutAndString(currentCode, "", true))
            {
                return FrizzlApplication.resources.getString(R.string.error_delete_speakout_add_function);
            }
            return null;
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if the user changed the white parts of the function
    private static ErrorCheck errorCheck5 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (!CodeCheckUtils.checkIfFunctionSignatureIsValid(currentCode))
            {
                return FrizzlApplication.resources.getString(R.string.error_dont_change_function);
            }
            return null;
        }
    };

    // Returns null if error1 was not found, or Error to display.
    // Checks if the speakOut command is not in the right place
    private static ErrorCheck errorCheck6 = new ErrorCheck() {
        @Override
        public String check(String originalCode, String currentCode)
        {
            if (!CodeCheckUtils.checkIfSpeakOutIsInsideCurlyBrackets(currentCode))
            {
                return FrizzlApplication.resources.getString(R.string.error_speakout_brackets);
            }
            return null;
        }
    };

    private static List<ErrorCheck> errorChecksList;
    private static Map<String, int[]> levelAndSlideToChecks;

    public static void init(){
        errorChecksList = new ArrayList<ErrorCheck>();
        errorChecksList.add(errorCheck0);
        errorChecksList.add(errorCheck1);
        errorChecksList.add(errorCheck2);
        errorChecksList.add(errorCheck3);
        errorChecksList.add(errorCheck4);
        errorChecksList.add(errorCheck5);
        errorChecksList.add(errorCheck6);

        levelAndSlideToChecks = new HashMap<>();
        levelAndSlideToChecks.put(Support.SPEAKOUT_PRACTICE_LEVEL_ID + "_1", new int[]{1, 2, 3});
        levelAndSlideToChecks.put(Support.SPEAKOUT_PRACTICE_LEVEL_ID + "_2", new int[]{1, 2, 3});
        levelAndSlideToChecks.put(Support.ONCLICK_PRACTICE_LEVEL_ID + "_5", new int[]{1, 4, 5});
        levelAndSlideToChecks.put(Support.ONCLICK_PRACTICE_LEVEL_ID + "_7", new int[]{1, 4, 5});
        levelAndSlideToChecks.put(Support.ONCLICK_PRACTICE_LEVEL_ID + "_8", new int[]{1, 5, 2, 6});
        levelAndSlideToChecks.put(Support.ONCLICK_PRACTICE_LEVEL_ID + "_9", new int[]{});
    }

    public static String check(int currentLevel, int currentSlide, String originalCode, String currentCode) {
        if (!initialized) init();

        String key = currentLevel + "_" + currentSlide;
        if (!levelAndSlideToChecks.containsKey(key)) return null;
        int[] checksToPerform = levelAndSlideToChecks.get(key);
        for (int checkIndex : checksToPerform) {
            if (checkIndex < errorChecksList.size()) {
                ErrorCheck errorCheck = errorChecksList.get(checkIndex);
                String checkResult = errorCheck.check(originalCode, currentCode);
                if (checkResult != null) return checkResult;
            }
        }
        return null;
    }
}
