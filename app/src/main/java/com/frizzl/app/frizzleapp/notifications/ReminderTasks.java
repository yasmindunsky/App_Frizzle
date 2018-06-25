package com.frizzl.app.frizzleapp.notifications;

import android.content.Context;

/**
 * Created by Noga on 18/06/2018.
 */

public class ReminderTasks {
    public static final String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask (Context context, String action) {
        if (ACTION_CHARGING_REMINDER.equals(action)) {
            issueChargingReminder(context);
        }
    }

    private static void issueChargingReminder(Context context) {
        NotificationUtils.remindUser(context);
    }
}
