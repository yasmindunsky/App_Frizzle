package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SplashActivity extends Activity{
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Obtain the FirebaseAnalytics instance.
        Context applicationContext = getApplicationContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext);
        // Don't sent analytics in debug
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG);
//        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true); // TODO: change before release

        // Check if it is the device's first time here
        boolean firstTime = SaveSharedPreference.getFirstTime(applicationContext);
//        firstTime = true; // TODO: change before release
        if(firstTime) {
            SaveSharedPreference.setFirstTime(applicationContext, false);
            Intent onboardingIntent = new Intent(getBaseContext(), OnboardingActivity.class);
            startActivity(onboardingIntent);
        }
        else {
            // get userProfile from internal storage
            UserProfile.user.loadSerializedObject(applicationContext);
            Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
            startActivity(mapIntent);
        }
    }
}



