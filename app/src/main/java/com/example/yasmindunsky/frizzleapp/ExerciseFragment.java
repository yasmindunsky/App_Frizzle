package com.example.yasmindunsky.frizzleapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {

    Exercise exercise;
    ArrayList<String> userAnswer = new ArrayList<>();
    boolean correct;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_exercise, container, false);
        TextView fragmentQuestion = fragmentView.findViewById(R.id.exerciseQuestion);
        final ImageView fragmentImage = fragmentView.findViewById(R.id.exerciseImage);
        Bundle bundle = getArguments();

        exercise = LessonActivity.currentLesson.getExercises().get(bundle.getInt("index"));

        String question = exercise.getQuestion();
        fragmentQuestion.setText(question);

        // present image if exists
        if (!exercise.getImageSource().equals("None")) {
            String imageSrc = exercise.getImageSource();
            int imageDrawable = getResources().getIdentifier(imageSrc, "drawable", getActivity().getPackageName());
            fragmentImage.setImageResource(imageDrawable);
        }

        // crate layout according to exercise type
        createLayout((LinearLayout) fragmentView.findViewById(R.id.exerciseElementsLayout), fragmentView.getContext());

        // set the OnClick method of the check-button
        ((Button) fragmentView.findViewById(R.id.checkButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(fragmentView);

                if (correct) {
                    view.setBackgroundColor(Color.GREEN);
                } else {
                    view.setBackgroundColor(Color.RED);
                }
            }
        });

        return fragmentView;
    }

    private void createLayout(LinearLayout view, Context context) {
        switch (exercise.getType()) {
            case "SingleResponse":

                // add RadioGroup to layout
                RadioGroup possibilitiesButtons = new RadioGroup(context);
                possibilitiesButtons.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                possibilitiesButtons.setId(R.id.radioGroup);
                view.addView(possibilitiesButtons);

                // create each possibility as a radio button and add to RadioGroup
                for (final String possibility : exercise.getPossibilities()) {
                    final RadioButton button = new RadioButton(context);
                    possibilitiesButtons.addView(button);
                    button.setText(possibility);
                }

                break;

            case "MultipleResponse":
                for (final String possibility : exercise.getPossibilities()) {
                    final Button button = new Button(context);
                    button.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
                    button.setText(possibility);

                    // set onClick so first click will select the answer and second will unsele
                    // second click will remove it from the list
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
                break;

            case "FreeText":
                EditText inputText = new EditText(context);
                inputText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                inputText.setId(R.id.userAnswerInput);
                view.addView(inputText);
                break;

            case "FillTheBlanks":
                break;
            case "WordBank":
                break;
            case "Matching":
                break;
        }
    }

    private void check(View fragmentView) {
        switch (exercise.getType()) {
            case "SingleResponse":
                RadioGroup radioGroup = fragmentView.findViewById(R.id.radioGroup);
                RadioButton checkedButton = fragmentView.findViewById(radioGroup.getCheckedRadioButtonId());
                String userSelectedAnswer = checkedButton.getText().toString();

                correct = userSelectedAnswer.equals(exercise.getAnswers().get(0));

                break;

            case "MultipleResponse":
                correct = true;

                // check that all of the user answers are right
                for (String answer : userAnswer) {
                    if (!exercise.getAnswers().contains(answer)) {
                        correct = false;
                    }
                }

                // check that all the right answers are chosen by the user
                for (String answer : exercise.getAnswers()) {
                    if (!userAnswer.contains(answer)) {
                        correct = false;
                    }
                }
                break;

            case "FreeText":
                // take the current answer from EditText and compare it to the right answer
                EditText userAnswerInput = fragmentView.findViewById(R.id.userAnswerInput);
                String currentAnswer = userAnswerInput.getText().toString();
                correct = exercise.getAnswers().contains(currentAnswer);
                break;

            case "FillTheBlanks":
                break;
            case "WordBank":
                break;
            case "Matching":
                break;
        }
    }
//
//    private void createLayout(LinearLayout view, Context context) {
//        switch (exercise.getType()) {
//            case "SingleResponse":
//                for (final String possibility : exercise.getPossibilities()) {
//                    final Button button = new Button(context);
//                    button.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
//                    button.setText(possibility);
//
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (userAnswer.isEmpty()) {
//                                // no other possibility is chosen - select this one
//                                userAnswer.add(possibility);
//                                button.setBackgroundColor(Color.MAGENTA);
//                            } else if (userAnswer.get(0).equals(possibility)) {
//                                // possibility was already selected - unselect it now
//                                userAnswer.remove(possibility);
//                                button.setBackgroundResource(android.R.drawable.btn_default);
//                            } else {
//                                // a different possibility was selected, switch between them
//                                userAnswer.remove(0);
//
//                                userAnswer.add(possibility);
//                                button.setBackgroundColor(Color.MAGENTA);
//                            }
//                        }
//                    });
//
//                    view.addView(button);
//                }
//                break;
//
//            case "MultipleResponse":
//                for (final String possibility : exercise.getPossibilities()) {
//                    final Button button = new Button(context);
//                    button.setLayoutParams(new LayoutParams(250, LayoutParams.WRAP_CONTENT));
//                    button.setText(possibility);
//
//                    // set onClick so first click will select the answer and second will unsele
//                    // second click will remove it from the list
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (userAnswer.contains(possibility)) {
//                                // possibility was already selected - unselect it now
//                                userAnswer.remove(possibility);
//                                button.setBackgroundResource(android.R.drawable.btn_default);
//                            } else {
//                                // possibility wasn't selected - select it now
//                                userAnswer.add(possibility);
//                                button.setBackgroundColor(Color.MAGENTA);
//                            }
//                        }
//                    });
//
//                    view.addView(button);
//                }
//                break;
//
//            case "FreeText":
//                EditText inputText = new EditText(context);
//                inputText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//                inputText.setId(R.id.userAnswerInput);
//                view.addView(inputText);
//                break;
//
//            case "FillTheBlanks":
//                break;
//            case "WordBank":
//                break;
//            case "Matching":
//                break;
//        }
//    }

}
