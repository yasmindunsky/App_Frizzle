package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check if already logged in
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            String username = SaveSharedPreference.getUserName(getApplicationContext());
            UserProfile.user.setUsername(username);
            UserProfile.user.updateProfileFromServerAndGoToMap(getApplicationContext());
        }
        else {
            Intent onboardingIntent = new Intent(getApplicationContext(), OnboardingActivity.class);
            getApplicationContext().startActivity(onboardingIntent);
        }
    }
}



