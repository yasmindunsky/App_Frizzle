package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 02/09/2018.
 */

class RequestPermissionPopupWindow extends PopupWindow {
    private final int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private final int height = GridLayout.LayoutParams.WRAP_CONTENT;

    public RequestPermissionPopupWindow(Context context,  Runnable requestPermission){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_request_permissions, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        Button okButton = popupView.findViewById(R.id.okButton);
        Button cancelButton = popupView.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(v -> {
            requestPermission.run();
            dismiss();
        });
        cancelButton.setOnClickListener(v -> dismiss());
    }
}
