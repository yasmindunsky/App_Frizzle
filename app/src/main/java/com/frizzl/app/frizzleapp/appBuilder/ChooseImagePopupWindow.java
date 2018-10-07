package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.frizzl.app.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by Noga on 02/09/2018.
 */

public class ChooseImagePopupWindow extends PopupWindow {
    private int width = GridLayout.LayoutParams.WRAP_CONTENT;
    private int height = GridLayout.LayoutParams.WRAP_CONTENT;
    private RadioButton selectedButton;
    private View popupView;

    public ChooseImagePopupWindow(Context context, int index, UserCreatedImageView userCreatedImageView){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_choose_image, null);
        setContentView(popupView);

        setWidth(width);
        setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        android.support.v7.widget.AppCompatButton saveButton = popupView.findViewById(R.id.saveButton);
        ImageButton deleteButton = popupView.findViewById(R.id.delete);
        RadioButton radioButton1 = popupView.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = popupView.findViewById(R.id.radioButton2);
        RadioButton radioButton3 = popupView.findViewById(R.id.radioButton3);
//        RadioButton radioButton4 = popupView.findViewById(R.id.radioButton4);
        RadioButton radioButton5 = popupView.findViewById(R.id.radioButton5);
        RadioButton radioButton6 = popupView.findViewById(R.id.radioButton6);
        RadioButton radioButton7 = popupView.findViewById(R.id.radioButton7);
        selectedButton = radioButton1;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = selectedButton.getTag();
                if (tag != null) {
                    userCreatedImageView.setImage(selectedButton.getBackground(), tag.toString());
                    dismiss();
                }
            }
        });

        //DELETE
        deleteButton.setTag(index);
//        deleteButton.setOnClickListener(deleteView);
//

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
    }
}
