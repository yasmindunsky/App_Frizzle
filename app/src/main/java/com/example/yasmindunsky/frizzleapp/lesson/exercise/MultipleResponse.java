package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */
public class MultipleResponse extends Exercise {
    private ArrayList<String> userAnswer = new ArrayList<>();

    public MultipleResponse(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    @Override
    public void createLayout(LinearLayout view, Context context) {
        for (final String possibility : this.getPossibilities()) {
            final Button button = new Button(context);
            button.setLayoutParams(new ViewGroup.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText(possibility);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userAnswer.contains(possibility)) {
                        // possibility was already selected - unselect it now
                        userAnswer.remove(possibility);
                        button.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        // possibility wasn't selected - select it now
                        userAnswer.add(possibility);
                        button.setBackgroundColor(Color.MAGENTA);
                    }
                }
            });

            view.addView(button);
        }
    }

    @Override
    public boolean isCorrect(View view) {
        // check that all of the user answers are right
        for (String answer : userAnswer) {
            if (!this.getAnswers().contains(answer)) {
                return false;
            }
        }

        // check that all the right answers are chosen by the user
        for (String answer : this.getAnswers()) {
            if (!userAnswer.contains(answer)) {
                return false;
            }
        }
        return true;
    }
}
