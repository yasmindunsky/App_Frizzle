package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.yasmindunsky.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */

public class SingleResponse extends Exercise {
    public SingleResponse(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    @Override
    public void createLayout(RelativeLayout layout, Context context) {
        // add RadioGroup to layout
        RadioGroup possibilitiesButtons = new RadioGroup(context);
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.exerciseQuestion);
        possibilitiesButtons.setLayoutParams(layoutParams);
        possibilitiesButtons.setId(R.id.radioGroup);
        possibilitiesButtons.setOrientation(LinearLayout.HORIZONTAL);
        possibilitiesButtons.setGravity(Gravity.CENTER);
        layout.addView(possibilitiesButtons);

        // Create each possibility as a radio button and add to RadioGroup.
        // TODO: choose style based on number of possible answers.
        int buttonStyle = R.style.exerciseTwoOptionButton;
        for (final String possibility : this.getPossibilities()) {
            final AppCompatRadioButton button = new AppCompatRadioButton(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
            RadioGroup.LayoutParams buttonLayoutParams =
                    new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(20,20,20,16);
            button.setTextAppearance(buttonStyle);
            button.setLayoutParams(buttonLayoutParams);
            button.setText(possibility);
            possibilitiesButtons.addView(button);
        }
    }

    @Override
    public boolean isCorrect(View view) {
        // get the text on the checked radio button
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        AppCompatRadioButton checkedButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
        String userSelectedAnswer = checkedButton.getText().toString();

        // compare the text on the checked button to the answer
        return userSelectedAnswer.equals(this.getAnswers().get(0));    }
}
