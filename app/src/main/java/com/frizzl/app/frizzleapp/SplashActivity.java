package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;

import com.crashlytics.android.Crashlytics;
import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

public class SplashActivity extends Activity{
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(getApplicationContext()).setAnalyticsCollectionEnabled(false);
        }


        // Check if already logged in
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            String username = SaveSharedPreference.getUserName(getApplicationContext());
            UserProfile.user.setUsername(username);
            UserProfile.user.updateProfileFromServerAndGoToMap(getApplicationContext());
        }
        else {
            Intent onboardingIntent = new Intent(getBaseContext(), OnboardingActivity.class);
            startActivity(onboardingIntent);
        }
    }
}



