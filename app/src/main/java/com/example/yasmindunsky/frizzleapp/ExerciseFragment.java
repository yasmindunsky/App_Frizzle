package com.example.yasmindunsky.frizzleapp;
import android.support.v4.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {


    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        TextView fragmentQuestion = view.findViewById(R.id.question);
        ImageView fragmentImage = view.findViewById(R.id.exerciseImage);
        FrameLayout elementsLayout = view.findViewById(R.id.exerciseElementsLayout);
        Button fragmentButton = view.findViewById(R.id.checkButton);
        Bundle bundle = getArguments();

        Exercise exercise = LessonActivity.currentLesson.getExercises().get(bundle.getInt("index"));
        String question = exercise.getQuestion();
        fragmentQuestion.setText(question);

//        if()
//        String imageSrc = slide.getImageSource();
//        int imageDrawable = getResources().getIdentifier(imageSrc , "drawable", getActivity().getPackageName());
//        fragmentImage.setImageResource(imageDrawable);

        return view;
    }

}
