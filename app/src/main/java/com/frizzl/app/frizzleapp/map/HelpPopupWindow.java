package com.frizzl.app.frizzleapp.map;

import android.content.Context;
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

public class HelpPopupWindow extends PopupWindow {
    private final int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private final int height = GridLayout.LayoutParams.WRAP_CONTENT;
    private View popupView;

    public HelpPopupWindow(MapActivity activity){
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        popupView = inflater.inflate(R.layout.popup_help, null);
        setContentView(popupView);
        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        EditText emailEditText = popupView.findViewById(R.id.email);
        EditText feedbackEditText = popupView.findViewById(R.id.feedback_box);
        Button sendButton = popupView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener((View v) -> {
            String email = emailEditText.getText().toString();
            String feedback = feedbackEditText.getText().toString();

            AnalyticsUtils.logFeedbackEvent(email, feedback);

            Button doneButton = popupView.findViewById(R.id.doneButton);
            doneButton.setOnClickListener((view)->dismiss());

            switchFormVisibility(View.GONE);
        });
    }

    public void switchFormVisibility(int formVisibility) {
        int sentVisibility = formVisibility == View.GONE ? View.VISIBLE : View.GONE;

        popupView.findViewById(R.id.checkMark).setVisibility(sentVisibility);
        popupView.findViewById(R.id.sent).setVisibility(sentVisibility);
        popupView.findViewById(R.id.thankYou).setVisibility(sentVisibility);
        popupView.findViewById(R.id.doneButton).setVisibility(sentVisibility);

        popupView.findViewById(R.id.feedbackTitle).setVisibility(formVisibility);
        popupView.findViewById(R.id.feedback_box).setVisibility(formVisibility);
        popupView.findViewById(R.id.email).setVisibility(formVisibility);
        popupView.findViewById(R.id.sendButton).setVisibility(formVisibility);
    }

}
