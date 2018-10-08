package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Noga on 13/02/2018.
 */

public class Support {
    public final static int SPEAKOUT_PRACTICE_LEVEL_ID = 0;
    public final static int ONCLICK_PRACTICE_LEVEL_ID = 1;
    public final static int POLLY_APP_LEVEL_ID = 2;

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static int dpStringToPixel(String dp, Context context) {
        dp = dp.replaceAll("[^\\d.]", "");
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  Integer.parseInt(dp), context.getResources().getDisplayMetrics());
    }

    public static ArrayList<String> colorsHexList = new ArrayList<String>() {{
        add("#39a085");
        add("#f07a00");
        add("#b93660");
        add("#535264");
        add("#f8b119");
    }};

    public static void presentPopup(PopupWindow popupWindow, Runnable runOnDismiss, View viewToPopOn,
                             View viewToDim, Context context){
        dimView(viewToDim, context);
        popupWindow.setOnDismissListener(() -> {
            undimView(viewToDim, context);
            if (runOnDismiss != null) {
                runOnDismiss.run();
            }
        });
        // In order to show popUp after activity has been created
        viewToPopOn.post(() -> popupWindow.showAtLocation(viewToPopOn, Gravity.CENTER, 0, 0));
    }

    public static void dimView(View view, Context context) {
        view.setForeground(context.getResources().getDrawable(R.drawable.shade));
        view.getForeground().setAlpha(220);
    }

    public static void undimView(View view, Context context) {
        view.setForeground(context.getResources().getDrawable(R.drawable.shade));
        view.getForeground().setAlpha(0);
    }
}
