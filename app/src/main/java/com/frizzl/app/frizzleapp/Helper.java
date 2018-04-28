package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Noga on 25/04/2018.
 */

public class Helper {
    public static View showPopup(Context context, int popupLayout, final View backgroundView){
        // inflate the popupLayout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(popupLayout, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        backgroundView.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(backgroundView, Gravity.CENTER, 0, 0);
            }
        });

        // Set closing button.
        ImageButton closeButton = popupView.findViewById(R.id.close);
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        // dim background.
        backgroundView.setForeground(context.getResources().getDrawable(R.drawable.shade));
        backgroundView.getForeground().setAlpha(150);

        return popupView;
    }
}
