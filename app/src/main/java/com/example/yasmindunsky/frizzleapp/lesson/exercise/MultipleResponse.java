package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.example.yasmindunsky.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */
public class MultipleResponse extends Exercise {
    private ArrayList<String> userAnswer = new ArrayList<>();
    private static final int COLS_NUM = 2;

    public MultipleResponse(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    @Override
    public void createLayout(RelativeLayout layout, Context context, final Button checkButton) {
        // Create a LinearLayout that will hold the buttons and place it correctly.
        final LinearLayout linearLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(2000, 2000);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.exerciseQuestion);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(linearLayout);

        // Calculate num of rows needed and create TableRows accordingly.
        ArrayList<String> possibilities = this.getPossibilities();
        int possibilitiesNum = possibilities.size();
        int rowsNum = (possibilitiesNum / COLS_NUM) + 1;
        ArrayList<TableRow> rows = new ArrayList<>();
        for (int i = 0; i < rowsNum; i++) {
            final TableRow row = new TableRow(context);
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams();
            rowLayoutParams.width = LayoutParams.MATCH_PARENT;
            rowLayoutParams.height = LayoutParams.WRAP_CONTENT;
            rowLayoutParams.gravity = Gravity.CENTER;
            rowLayoutParams.setMarginStart((int)context.getResources().getDimension(R.dimen.start_margin));
            row.setLayoutParams(rowLayoutParams);
            rows.add(row);
            linearLayout.addView(row);
        }

        // Create possibilities ToggleButtons
        int buttonStyle = R.style.exerciseOptionButton;
        for (int i = 0; i < possibilitiesNum; i++) {
            final String possibility = possibilities.get(i);
            final ToggleButton button = new ToggleButton(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
            button.setText(possibility);
            TableRow.LayoutParams buttonlayoutParams = new TableRow.LayoutParams();
            buttonlayoutParams.height = LayoutParams.WRAP_CONTENT;
            buttonlayoutParams.width = LayoutParams.WRAP_CONTENT;
            buttonlayoutParams.setMargins(16,16,16,16);
            button.setLayoutParams(buttonlayoutParams);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userAnswer.contains(possibility)) {
                        // possibility was already selected - unselect it now
                        userAnswer.remove(possibility);
                    } else {
                        // possibility wasn't selected - select it now
                        userAnswer.add(possibility);
                    }

                    // disable the check button if none of the buttons is selected
                    if(userAnswer.isEmpty()){
                        checkButton.setEnabled(false);
                    } else {
                        checkButton.setEnabled(true);
                    }
                }
            });

            TableRow row = rows.get((int)i / COLS_NUM);
            row.addView(button);
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
