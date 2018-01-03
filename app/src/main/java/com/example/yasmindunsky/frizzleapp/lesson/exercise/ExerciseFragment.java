package com.example.yasmindunsky.frizzleapp.lesson.exercise;

import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;
import com.example.yasmindunsky.frizzleapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {

    Exercise exercise;

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

        fragmentQuestion.setText(exercise.getQuestion());
        if (!exercise.getImageSource().equals("None")) {
            String imageSrc = exercise.getImageSource();
            int imageDrawable = getResources().getIdentifier(imageSrc, "drawable", getActivity().getPackageName());
            fragmentImage.setImageResource(imageDrawable);
        }

        exercise.createLayout(((LinearLayout) fragmentView.findViewById(R.id.exerciseElementsLayout)), fragmentView.getContext());

        setCheckButtonOnClick(fragmentView);

        return fragmentView;
    }

    private void setCheckButtonOnClick(final View fragmentView) {
        fragmentView.findViewById(R.id.checkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exercise.isCorrect(fragmentView)) {
                    view.setBackgroundColor(Color.GREEN);
                } else {
                    view.setBackgroundColor(Color.RED);
                }
            }
        });
    }
}
