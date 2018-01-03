package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */

public class FillTheBlanks extends Exercise {
    protected int BLANK_ID_INIT_NUMBER = 2000;

    public FillTheBlanks(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    public void createLayout(LinearLayout view, Context context) {
        for (int i = 0; i < this.getContent().size(); i++) {
            String sentence = this.getContent().get(i);

            // add each sentence from the Content array
            TextView text = new TextView(context);
            text.setText(sentence);
            view.addView(text);

            // add an EditText for the user's answer after each sentence except after the last one
            if (i < this.getContent().size() - 1) {
                EditText blank = new EditText(context);
                blank.setId(BLANK_ID_INIT_NUMBER + i);
                view.addView(blank);
            }
        }
    }

    public boolean isCorrect(View fragmentView){

        for (int i = 0; i < this.getContent().size() - 1; i++) {
            EditText blankAnswer = fragmentView.findViewById(BLANK_ID_INIT_NUMBER + i);
            String userBlankAnswer = blankAnswer.getText().toString();

            if (!userBlankAnswer.equals(this.getAnswers().get(i))) {
                return false;
            }
        }

        return true;
    }
}
