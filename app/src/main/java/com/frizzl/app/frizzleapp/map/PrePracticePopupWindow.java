package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;

/**
 * Created by Noga on 02/09/2018.
 */

public class PrePracticePopupWindow extends PopupWindow {
    private final int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private final int height = GridLayout.LayoutParams.WRAP_CONTENT;
    private View popupView;
    private AppCompatTextView textView;

    public PrePracticePopupWindow(MapActivity activity, String explanationText, Runnable startPractice){
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        popupView = inflater.inflate(R.layout.popup_pre_practice, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        textView = popupView.findViewById(R.id.practiceExplanationText);
        textView.setText(explanationText);
        Button okButton = popupView.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            startPractice.run();
            dismiss();
        });
    }

}
