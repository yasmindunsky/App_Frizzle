package com.frizzl.app.frizzleapp.notifications;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Noga on 18/06/2018.
 */

public class ReminderUtils {
    private static final int REMINDER_INTERVAL_HOURS = 1;
//    private static final int REMINDER_INTERVAL_SECONDS = (int)(TimeUnit.HOURS.toSeconds(REMINDER_INTERVAL_HOURS));
    private static final int REMINDER_INTERVAL_SECONDS = 45;
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS/3;

    private static final String REMINDER_JOB_TAG = "reminder_tag";

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;
        sInitialized = true;
        scheduleChargingReminder(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                startReminders(context);
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
    }

    public static void startReminders(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, NotificationIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
