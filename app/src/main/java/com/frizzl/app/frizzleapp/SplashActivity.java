package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.frizzl.app.frizzleapp.map.MapActivity;
import com.frizzl.app.frizzleapp.onboarding.OnboardingActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Context applicationContext = getApplicationContext();

        // Check if it is the device's first time here
        boolean firstTime = SaveSharedPreference.getFirstTime(applicationContext);
//        firstTime = true; // TODO: change before release
        if(firstTime) {
            SaveSharedPreference.setFirstTime(applicationContext);
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

    @Override
    protected void onStop() {
        try {
            super.onStop();
        } catch (Exception e) {
            Log.w("", "onStop()", e);
            super.onStop();
        }
    }
}



