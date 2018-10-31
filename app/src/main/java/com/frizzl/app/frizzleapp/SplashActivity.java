package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArraySet;
import android.widget.Toast;

import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

        String android_id = Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        List<String> testDevicesIDs = Arrays.asList("f7124e727cd2c363", "e1ba81bb230e0daf");
        mFirebaseAnalytics.setUserProperty("test_device", String.valueOf(testDevicesIDs.contains(android_id)));


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



