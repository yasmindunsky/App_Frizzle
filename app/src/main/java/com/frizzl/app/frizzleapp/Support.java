package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Noga on 13/02/2018.
 */

public class Support {
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


}
