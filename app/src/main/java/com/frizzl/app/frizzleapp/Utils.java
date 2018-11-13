package com.frizzl.app.frizzleapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Noga on 13/02/2018.
 */

public class Utils {
    public final static int FIRST_PRACTICE_LEVEL_ID = 1;
    public final static int SPEAKOUT_PRACTICE_LEVEL_ID = 2;
    public final static int ONCLICK_PRACTICE_LEVEL_ID = 3;
    public final static int POLLY_APP_LEVEL_ID = 4;

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

    public static String hexFromColorInt(int colorInt){
        return String.format("#%06X", (0xFFFFFF & colorInt));
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
        if(((Activity) context).isFinishing()) return;

        if (viewToDim != null) dimView(viewToDim, context);
        popupWindow.setOnDismissListener(() -> {
            if (viewToDim != null) undimView(viewToDim, context);
            if (runOnDismiss != null) {
                runOnDismiss.run();
            }
        });
        // In order to show popUp after activity has been created
        if (viewToPopOn != null && !((Activity)context).isFinishing()) {
            popupWindow.showAtLocation(viewToPopOn, Gravity.CENTER, 0, 0);
        }
    }

    public static int dpStringToPixel(Context context, String dp) {
        dp = dp.replaceAll("[^\\d.]", "");
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  Integer.parseInt(dp), context.getResources().getDisplayMetrics());
    }

    private static void dimView(View view, Context context) {
        view.setForeground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.shade, null));
        view.getForeground().setAlpha(220);
    }

    private static void undimView(View view, Context context) {
        view.setForeground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.shade, null));
        view.getForeground().setAlpha(0);
    }

    public static SpannableStringBuilder markWithColorBetweenTwoSymbols(Spannable s, String symbol, int color, boolean removeSymbol, boolean makeBold, Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(s);
        int symbolLength = symbol.length();
        int startPosition = 0;
        int firstSymbol = ssb.toString().indexOf(symbol, startPosition);
        int secondSymbol;
        while (firstSymbol >= 0) {
            secondSymbol = (ssb.toString()).indexOf(symbol, firstSymbol + 1);
            if (secondSymbol > 0) {
                ssb.setSpan(new ForegroundColorSpan(color), firstSymbol, secondSymbol + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (makeBold) {
                    Typeface font = Utils.getRegularFontByLanguage(context);
                    ssb.setSpan(new CustomTypefaceSpan("", font), firstSymbol, secondSymbol + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (removeSymbol) {
                    ssb.replace(firstSymbol, firstSymbol+symbolLength, "");
                    ssb.replace(secondSymbol-symbolLength, secondSymbol, ""); //Indices
                    // should have secondSymbol, symbolLength+symbolLength but since first symbol
                    // was already deleted they changed.
                }
                startPosition = secondSymbol + 1;
            } else {
                startPosition = firstSymbol + 1;
            }
            firstSymbol = ssb.toString().indexOf(symbol, startPosition);
        }
        return ssb;
    }

    public static boolean volumeIsLow(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumePercentage = 100 * currentVolume/maxVolume;
        return currentVolumePercentage < 20;
    }

    public static void presentVolumeToast(Context context) {
        Toast.makeText(context, R.string.turn_volume_up, Toast.LENGTH_LONG).show();
    }

    public static Typeface getRegularFontByLanguage(Context context) {
        Typeface font = null;
        String language = Locale.getDefault().getLanguage();

        if (language.equals("en"))
            font = ResourcesCompat.getFont(context, R.font.calibri_regular);
        else if (language.equals("iw"))
            font = ResourcesCompat.getFont(context, R.font.rubik_regular);
        return font;
    }
}
