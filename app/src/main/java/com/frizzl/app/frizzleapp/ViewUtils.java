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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Noga on 13/02/2018.
 */

public class ViewUtils {
    public static final String CONNECTION_FAILED = "15657";
    public static final int GET_FROM_GALLERY = 23284;
    private static final Map<String, String> iconNameToAddress = new HashMap<String, String>() {{
        put("ic_crown","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_crown.svg?alt=media&token=ae6ff211-4d1c-42c2-a8ab-dde384a5fe78");
        put("ic_diamond","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_diamond.svg?alt=media&token=ea8e72fa-5c1b-4c77-8b0b-527eb8efcffb");
        put("ic_heart_bubble","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_heart_bubble.svg?alt=media&token=07ae0aad-03cc-48fa-be1d-b90b0b0e73b4");
        put("ic_heart_envelope","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_heart_envelope.svg?alt=media&token=54f054bc-0cfb-4b21-a829-4bb124ad0d76");
        put("ic_loud_speaker","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_loud_speaker.svg?alt=media&token=34620f3f-aa7c-4e83-841b-20d32df5460f");
        put("ic_like","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourIcons%2Fic_like.svg?alt=media&token=676faacd-24c5-4da1-9343-66eea7d1674c");
    }};
    private static final Map<String, String> imgNameToAddress = new HashMap<String, String>() {{
        put("img_1","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_1.jpg?alt=media&token=01f3f99d-353a-4113-844c-7f43cd888985");
        put("img_2","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_2.jpg?alt=media&token=da52b575-3f57-42d8-ad88-259a91b95327");
        put("img_3","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_3.jpg?alt=media&token=7a2957ea-9cd5-4838-a5e4-9bf24c8d97bf");
        put("img_4","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_4.jpg?alt=media&token=24e5fb89-1c6e-4615-9d7f-8fc846843caa");
        put("img_5","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_5.jpg?alt=media&token=26b28482-76b9-495f-a553-a46c9f55a739");
        put("img_6","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_6.jpg?alt=media&token=3faaa1de-a726-4fb1-a9de-e03baeadebd1");
        put("img_7","https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/ourImages%2Fimg_7.jpg?alt=media&token=58730e08-5977-4be7-b5ef-2a82b0c9f40b");
    }};

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    private static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static int dpStringToPixel(String dp, Context context) {
        dp = dp.replaceAll("[^\\d.]", "");
        double doubleDp = Double.parseDouble(dp);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  (int)doubleDp, context.getResources().getDisplayMetrics());
    }

    public static String hexFromColorInt(int colorInt){
        return String.format("#%06X", (0xFFFFFF & colorInt));
    }

    public static String hexFromColorResource(int colorResource, Context context){
        return "#" + Integer.toHexString(context.getResources().getColor(colorResource) & 0x00ffffff);
    }

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



    private static void dimView(View view, Context context) {
        view.setForeground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.shade, null));
        view.getForeground().setAlpha(220);
    }

    private static void undimView(View view, Context context) {
        view.setForeground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.shade, null));
        view.getForeground().setAlpha(0);
    }

    public static SpannableStringBuilder markWithColorBetweenTwoSymbols(Spannable s, String symbol, int color, boolean makeBold, Context context) {
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
                    Typeface font = ViewUtils.getRegularFontByLanguage(context);
                    if (font != null)
                        ssb.setSpan(new CustomTypefaceSpan(font), firstSymbol, secondSymbol + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                ssb.replace(firstSymbol, firstSymbol+symbolLength, "");
                ssb.replace(secondSymbol-symbolLength, secondSymbol, ""); //Indices
                // should have secondSymbol, symbolLength+symbolLength but since first symbol
                // was already deleted they changed.
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
        return currentVolumePercentage < 30;
    }

    public static void presentVolumeToast(Context context) {
        Toast.makeText(context, R.string.turn_volume_up, Toast.LENGTH_LONG).show();
    }

    private static Typeface getRegularFontByLanguage(Context context) {
        Typeface font = null;
        String language = Locale.getDefault().getLanguage();

        if (language.equals("en"))
            font = ResourcesCompat.getFont(context, R.font.calibri_regular);
        else if (language.equals("iw"))
            font = ResourcesCompat.getFont(context, R.font.rubik_regular);
        return font;
    }

    public static String iconNameToAddress(String appIcon) {
        return iconNameToAddress.get(appIcon);
    }

    public static String imgNameToAddress(String imgName) {
        return imgNameToAddress.get(imgName);
    }
}
