package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Noga on 13/02/2018.
 */

public class AnalyticsUtils {
    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void init(FirebaseAnalytics instance, Context applicationContext) {
        mFirebaseAnalytics = instance;

        // Don't sent analytics in debug
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG);
//        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true); // TODO: change before release

        String android_id = Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        List<String> testDevicesIDs = Arrays.asList("f7124e727cd2c363", "e1ba81bb230e0daf");
        mFirebaseAnalytics.setUserProperty("test_device", String.valueOf(testDevicesIDs.contains(android_id)));
    }

    public static void logRunAppEvent(){
        mFirebaseAnalytics.logEvent("RUN_APP", null);
    }

    public static void logCompletedTaskEvent(int currentLevel, int currentTaskNum){
        Bundle bundle = new Bundle();
        bundle.putString("TASK_ID", currentLevel + "_" + currentTaskNum);
        mFirebaseAnalytics.logEvent("COMPLETED_TASK", bundle);
    }

    public static void logFeedbackEvent(String email, String feedback) {
        Bundle bundle = new Bundle();
        bundle.putString("FEEDBACK_EMAIL", email);
        bundle.putString("FEEDBACK_CONTENT", feedback);
        mFirebaseAnalytics.logEvent("FEEDBACK", bundle);
    }

    public static void setCurrentScreen(FragmentActivity activity, String simpleName, String s) {
        mFirebaseAnalytics.setCurrentScreen(activity, simpleName, s);
    }

    public static void logStartedAppEvent(int levelID) {
        Bundle bundle = new Bundle();
        bundle.putInt("LEVEL_ID", levelID);
        mFirebaseAnalytics.logEvent("STARTED_APP", bundle);
    }

    public static void logStartedPracticeEvent(int practiceLevelID) {
        Bundle bundle = new Bundle();
        bundle.putInt("LEVEL_ID", practiceLevelID);
        mFirebaseAnalytics.logEvent("STARTED_PRACTICE", bundle);
    }

    public static void logStartedIntroEvent(int levelID) {
        Bundle bundle = new Bundle();
        bundle.putInt("LEVEL_ID", levelID);
        mFirebaseAnalytics.logEvent("STARTED_INTRO", bundle);
    }
}
