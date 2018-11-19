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

class InternetRequiredPopupWindow extends PopupWindow {
    private final int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private final int height = GridLayout.LayoutParams.WRAP_CONTENT;

    public InternetRequiredPopupWindow(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_internet_required, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        Button okButton = popupView.findViewById(R.id.got_it);
        okButton.setOnClickListener(v -> dismiss());
    }
}
