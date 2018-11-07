package com.frizzl.app.frizzleapp;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Locale;
import java.util.UUID;

import static io.fabric.sdk.android.Fabric.isDebuggable;

/**
 * Created by Noga on 16/07/2018.
 */

public class FrizzlApplication extends Application {
    public static Resources resources;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        resources = getResources();
        AnalyticsUtils.init(FirebaseAnalytics.getInstance(this), getApplicationContext());

//        if(BuildConfig.DEBUG) { // TODO: change before sending out APK
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//        }

        // Normal app init code...

        // Set language key for Crashlytics
        if(!isDebuggable()) {
            String language;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                language = LocaleList.getDefault().get(0).getLanguage();
            } else {
                language = Locale.getDefault().getLanguage();
            }
            Crashlytics.setString("language", language);
            Crashlytics.setUserIdentifier(UUID.randomUUID().toString());
        }

////        // Set to English and LTR
//        Resources resources = this.getResources();
//        Configuration conf = resources.getConfiguration();
////        conf.locale = new Locale(Locale.ENGLISH.getLanguage());
////        resources.updateConfiguration(conf, resources.getDisplayMetrics());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            conf.setLayoutDirection(conf.locale);
//        }

//        Configuration config = getBaseContext().getResources().getConfiguration();
//        String lang = Locale.ENGLISH.getLanguage();
//        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
//        {
//            locale = new Locale(lang);
//            Locale.setDefault(locale);
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        }

        Task<InstanceIdResult> instanceId = FirebaseInstanceId.getInstance().getInstanceId();
    }

}
