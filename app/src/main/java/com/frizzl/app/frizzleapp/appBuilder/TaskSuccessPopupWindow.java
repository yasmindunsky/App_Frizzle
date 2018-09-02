package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Noga on 02/09/2018.
 */

public class TaskSuccessPopupWindow extends PopupWindow {
    int width = GridLayout.LayoutParams.WRAP_CONTENT;
    int height = GridLayout.LayoutParams.WRAP_CONTENT;

    public TaskSuccessPopupWindow(Context conext){
        LayoutInflater inflater = (LayoutInflater)
                conext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_task_success, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        Button continueButton = popupView.findViewById(R.id.continueButton);
        TextView notReady = popupView.findViewById(R.id.notReady);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        notReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
