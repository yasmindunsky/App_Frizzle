package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

import java.util.Locale;

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Set to English and LTR
        Configuration conf = this.getResources().getConfiguration();
        Locale locale = Locale.ENGLISH;
        conf.locale = locale;
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(conf.locale);
        }
        this.getResources().updateConfiguration(conf, this.getResources().getDisplayMetrics());

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



