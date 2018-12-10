package com.frizzl.app.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Noga on 02/09/2018.
 */

class ChooseImagePopupWindow extends PopupWindow {
    private RadioButton selectedButton;

    public ChooseImagePopupWindow(AppBuilderActivity appBuilderActivity, int index, UserCreatedImageView userCreatedImageView, int selectedImageID){
        LayoutInflater inflater = (LayoutInflater)
                appBuilderActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_choose_image, null);
        setContentView(popupView);

        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        setWidth(width);
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        selectedButton = popupView.findViewById(selectedImageID);
        selectedButton.setChecked(true);

        android.support.v7.widget.AppCompatButton saveButton = popupView.findViewById(R.id.saveButton);
        ImageButton deleteButton = popupView.findViewById(R.id.delete);
        RadioButton radioButton1 = popupView.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = popupView.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = popupView.findViewById(R.id.radioButton3);
        RadioButton radioButton4 = popupView.findViewById(R.id.radioButton4);
        RadioButton radioButton5 = popupView.findViewById(R.id.radioButton5);
        RadioButton radioButton6 = popupView.findViewById(R.id.radioButton6);
        RadioButton radioButton7 = popupView.findViewById(R.id.radioButton7);
        RadioButton radioButton8 = popupView.findViewById(R.id.radioButton8);

        radioButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBuilderActivity.openGallery(userCreatedImageView);
                dismiss();
            }
        });

        saveButton.setOnClickListener(v -> {
            Object tag = selectedButton.getTag();
            if (tag != null) {
                userCreatedImageView.setImage(selectedButton, tag.toString());
                dismiss();
            }
        });

        //DELETE
        deleteButton.setTag(index);

        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add(radioButton1);
        radioButtons.add(radioButton2);
        radioButtons.add(radioButton3);
        radioButtons.add(radioButton4);
        radioButtons.add(radioButton5);
        radioButtons.add(radioButton6);
        radioButtons.add(radioButton7);

        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (RadioButton rb : radioButtons) {
                        rb.setChecked(false);
                    }
                    buttonView.setChecked(true);
                    selectedButton = (RadioButton) buttonView;
                }
            });
        }
    }
}
