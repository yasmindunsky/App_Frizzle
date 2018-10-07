package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.frizzl.app.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by Noga on 02/09/2018.
 */

public class StartAppPopupWindow extends PopupWindow {
    private int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private int height = GridLayout.LayoutParams.WRAP_CONTENT;
    private RadioButton selectedButton;
    private AppBuilderActivity activityCalled;
    private View popupView;

    public StartAppPopupWindow(AppBuilderActivity activity){
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_start_app, null);
        setContentView(popupView);

        this.activityCalled = activity;

        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        Button startButton = popupView.findViewById(R.id.startButton);
        EditText editText = popupView.findViewById(R.id.app_name);
        RadioButton radioButton1 = popupView.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = popupView.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = popupView.findViewById(R.id.radioButton3);
//        RadioButton radioButton4 = popupView.findViewById(R.id.radioButton4);
        RadioButton radioButton5 = popupView.findViewById(R.id.radioButton5);
        RadioButton radioButton6 = popupView.findViewById(R.id.radioButton6);
        RadioButton radioButton7 = popupView.findViewById(R.id.radioButton7);

        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add(radioButton1);
        radioButtons.add(radioButton2);
        radioButtons.add(radioButton3);
//        radioButtons.add(radioButton4);
        radioButtons.add(radioButton5);
        radioButtons.add(radioButton6);
        radioButtons.add(radioButton7);

        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (RadioButton radioButton : radioButtons) {
                            radioButton.setChecked(false);
                        }
                        buttonView.setChecked(true);
                        selectedButton = (RadioButton) buttonView;
                    }
                }
            });
        }

//        radioButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.presentNextTutorialMessage();
//            }
//        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = String.valueOf(editText.getText());
                String iconDrawable = "";
                if (selectedButton != null) {
                    iconDrawable = (String) selectedButton.getTag();
                }
                activity.onStartButtonFromStartAppPopup(appName, iconDrawable);
                dismiss();
            }
        });
    }

}
