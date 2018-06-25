package com.frizzl.app.frizzleapp.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Noga on 19/06/2018.
 */

public class NotificationIntentService extends IntentService {

    public NotificationIntentService(){
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ReminderTasks.executeTask(this, "charging-reminder");
    }
}
