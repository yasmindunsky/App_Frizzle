package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

    public void createLayout(RelativeLayout view, Context context, final Button checkButton) {
        for (int i = 0; i < this.getContent().size(); i++) {
            String sentence = this.getContent().get(i);

            // add each sentence from the Content array
            TextView text = new TextView(context);
            text.setText(sentence);
            view.addView(text);

            // create an array to indicate which blanks are filled with text
            final ArrayList<Boolean> blankIsFilled = new ArrayList<>();

            // add an EditText for the user's answer after each sentence except after the last one
            if (i < this.getContent().size() - 1) {
                final EditText blank = new EditText(context);
                blank.setId(BLANK_ID_INIT_NUMBER + i);

                //TODO add disable and enable check button
//                blankIsFilled.add(false);
//                final int finalI = i;
//                blank.addTextChangedListener(new TextWatcher() {
//                    public void afterTextChanged(Editable s) {
//                        if(blank.getText().toString().equals("")){
//                            blankIsFilled.set(finalI, false);
//                        } else {
//                            blankIsFilled.set(finalI, true);
//                        }
//
//                        if(!blankIsFilled.contains(false)) {
//                            checkButton.setEnabled(true);
//                        }
//                    }
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
//                });

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
