package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */

public class WordBank extends FillTheBlanks {
    private int WORD_BANK_ID_INIT_NUMBER = 2000;
    private ArrayList<String> userAnswer = new ArrayList<>();

    public WordBank(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    @Override
    public void createLayout(final RelativeLayout fragmentView, Context context) {
        super.createLayout(fragmentView, context);

        for(int i = 0; i < this.getPossibilities().size(); i++){
            final String possibility  = this.getPossibilities().get(i);
            final Button button = new Button(context);
            button.setLayoutParams(new ViewGroup.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText(possibility);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userAnswer.contains(possibility)) {
                        // possibility was already selected - un-select it now
                        userAnswer.remove(possibility);
                        button.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        // possibility wasn't selected - select it now
                        userAnswer.add(possibility);
                        EditText answer = fragmentView.findViewById(BLANK_ID_INIT_NUMBER);
                        answer.setText(possibility);
                        button.setBackgroundColor(Color.MAGENTA);
                    }
                }
            });

            fragmentView.addView(button);
        }
    }

    @Override
    public boolean isCorrect(View view) {
        return false;
    }
}
