package com.frizzl.app.frizzleapp.lesson.exercise;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.lesson.LessonActivity;
import com.frizzl.app.frizzleapp.R;

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
        // Rotation for RTL swiping.
        if (Support.isRTL()) {
            fragmentView.setRotationY(180);
        }

        TextView fragmentQuestion = fragmentView.findViewById(R.id.exerciseQuestion);
        final ImageView fragmentImage = fragmentView.findViewById(R.id.exerciseImage);
        Bundle bundle = getArguments();

        int index = bundle.getInt("index");
        exercise = LessonActivity.getCurrentLesson().getExercises().get(index);

        // Disable swiping.
        if (index > 0){
            LessonActivity.setPagingEnabled(false);
        }

        fragmentQuestion.setText(exercise.getQuestion());
        if (!(exercise.getImageSource().equals("None"))) {
            String imageSrc = exercise.getImageSource();
            int imageDrawable = getResources().getIdentifier(imageSrc, "drawable", getActivity().getPackageName());
            fragmentImage.setImageResource(imageDrawable);
        }

        (fragmentView.findViewById(R.id.checkButton)).setEnabled(false);
        setCheckButtonOnClick(fragmentView);

        exercise.createLayout(((RelativeLayout) fragmentView.findViewById(R.id.exerciseElementsLayout)),
                fragmentView.getContext(), (Button)fragmentView.findViewById(R.id.checkButton));

        return fragmentView;
    }

    private void setCheckButtonOnClick(final View fragmentView) {
        fragmentView.findViewById(R.id.checkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.height = 85*3;
                layoutParams.width = 270*3;
                button.setLayoutParams(layoutParams);
                ((Button) view).setText("");
                if (exercise.isCorrect(fragmentView)) {
                    view.setBackgroundResource(R.drawable.correct);
                } else {
                    view.setBackgroundResource(R.drawable.incorrect);
                }
                LessonActivity.setPagingEnabled(true);
            }
        });
    }

}
