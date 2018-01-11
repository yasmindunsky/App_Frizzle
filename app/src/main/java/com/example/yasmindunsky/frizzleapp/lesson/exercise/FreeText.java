package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.yasmindunsky.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */

public class FreeText extends Exercise {
    public FreeText(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    public void createLayout(RelativeLayout layout, Context context, final Button checkButton){
        int style = R.style.editText;
        final EditText inputText = new EditText(new ContextThemeWrapper(context, style), null, style);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.exerciseQuestion);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        inputText.setLayoutParams(layoutParams);
        inputText.setHint("Type in your answer..");
        inputText.setId(R.id.userAnswerInput);
        layout.setGravity(Gravity.CENTER);
        layout.addView(inputText);


        inputText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(inputText.getText().toString().equals("")){
                    checkButton.setEnabled(false);
                } else {
                    checkButton.setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public boolean isCorrect(View view) {
        EditText freeTextAnswer = view.findViewById(R.id.userAnswerInput);
        String userFreeTextAnswer = freeTextAnswer.getText().toString();
        return this.getAnswers().contains(userFreeTextAnswer);
    }


}
