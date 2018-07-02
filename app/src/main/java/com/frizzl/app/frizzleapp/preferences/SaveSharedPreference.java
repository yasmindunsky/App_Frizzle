package com.frizzl.app.frizzleapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.frizzl.app.frizzleapp.UserProfile;

import static com.frizzl.app.frizzleapp.preferences.PreferencesUtility.*;

/**
 * Created by Noga on 02/07/2018.
 */

public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
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
}