//package com.frizzl.app.frizzleapp.notifications;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
//
//import com.frizzl.app.frizzleapp.R;
//import com.frizzl.app.frizzleapp.UserProfile;
//import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
//
//import org.jetbrains.annotations.TestOnly;
//
//import java.util.ArrayList;
//import java.util.Random;
//
///**
// * Created by Noga on 18/06/2018.
// */
//
//public class NotificationUtils {
//    private static final int PENDING_INTENT_ID = 3417;
//    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";
//    private static final int NOTIFICATION_ID = 1138;
//
//    public static void remindUser(Context context){
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel mChannel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            mChannel = new NotificationChannel(
//                    NOTIFICATION_CHANNEL_ID,
//                    context.getString(R.string.main_notification_channel_name),
//                    NotificationManager.IMPORTANCE_HIGH);
//        notificationManager.createNotificationChannel(mChannel);
//        }
//        String notificationText = generateNotificationText();
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                .setSmallIcon(R.drawable.ic_book_lessons_icon)
//                .setContentTitle(notificationText)
//                .setContentIntent(contentIntent(context))
//                .setAutoCancel(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
//        }
//        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//    }
//
//    private static PendingIntent contentIntent(Context context){
//        // An intent that will open the app
//        Intent startActivityIntent = new Intent(context, OnboardingActivity.class);
//        return PendingIntent.getActivity(
//                context,
//                PENDING_INTENT_ID,
//                startActivityIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//
//    private static Bitmap largeIcon (Context context) {
//        Resources res = context.getResources();
//        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_check_mark_icon);
//        return largeIcon;
//    }
//
//    public static String generateNotificationText(){
//        String nickname = UserProfile.user.getNickName();
//        // TODO: add currentAppType to nitifications
////        String currentAppType = UserProfile.user.getCurrentAppType();
//        int lessonID = UserProfile.user.getTopLessonID();
//
//        ArrayList<String> notifications = new ArrayList<>();
//        notifications.add(String.format("in just %s more lessons your app will be ready to share!", String.valueOf(8 - lessonID)));
//        String beautifulFraction = createBeautifulFraction(lessonID, 8);
//        notifications.add(String.format("you've already completed %s of your app, come back to finish it.", beautifulFraction));
//        notifications.add("who will be the first you'll share your app with?");
//
//        Random rand = new Random();
//        int randInt = rand.nextInt(notifications.size());
//        String chosenNotification = notifications.get(randInt);
//        randInt = rand.nextInt(2);
//        if (((randInt % 2) == 0) && (!nickname.isEmpty())) {
//            chosenNotification = nickname + ", " + chosenNotification;
//        }
//        // Capitalize first letter
//        chosenNotification = chosenNotification.substring(0, 1).toUpperCase() + chosenNotification.substring(1);
//
//        return chosenNotification;
//    }
//
//    public static String createBeautifulFraction(int n1, int n2) {
//        int temp1 = n1;
//        int temp2 = n2;
//
//        while (n1 != n2){
//            if(n1 > n2)
//                n1 = n1 - n2;
//            else
//                n2 = n2 - n1;
//        }
//
//        int n3 = temp1 / n1 ;
//        int n4 = temp2 / n1 ;
//
//        return(n3 + "/" + n4);
//    }
//}
