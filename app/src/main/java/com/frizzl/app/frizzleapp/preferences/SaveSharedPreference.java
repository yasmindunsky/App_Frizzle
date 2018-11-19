package com.frizzl.app.frizzleapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.frizzl.app.frizzleapp.preferences.PreferencesUtility.*;

/**
 * Created by Noga on 02/07/2018.
 */

public class SaveSharedPreference {

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setFirstTime(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(FIRST_TIME_PREF, false);
        editor.apply();
    }

    public static boolean getFirstTime(Context context) {
        return getPreferences(context).getBoolean(FIRST_TIME_PREF, true);
    }
}