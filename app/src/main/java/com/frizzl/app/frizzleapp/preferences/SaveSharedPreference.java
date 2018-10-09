package com.frizzl.app.frizzleapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.frizzl.app.frizzleapp.preferences.PreferencesUtility.*;

/**
 * Created by Noga on 02/07/2018.
 */

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(USERNAME_PREF, username);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getPreferences(context).getString(USERNAME_PREF, "");
    }

    public static void setFirstTime(Context context, boolean firstTime) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(FIRST_TIME_PREF, firstTime);
        editor.apply();
    }

    public static boolean getFirstTime(Context context) {
        return getPreferences(context).getBoolean(FIRST_TIME_PREF, true);
    }
}