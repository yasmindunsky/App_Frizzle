package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.AnalyticsUtils;
import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 02/09/2018.
 */

class NotifyMePopupWindow extends PopupWindow {
    private final int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private final int height = GridLayout.LayoutParams.WRAP_CONTENT;

    public NotifyMePopupWindow(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_notify, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        EditText emailEditText = popupView.findViewById(R.id.email);

        Button sendButton = popupView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            AnalyticsUtils.logNotifyMe(email);
            dismiss();
        });
    }
}
