package com.example.yasmindunsky.frizzleapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class lessonFragment extends Fragment {
    TextView fragmentText;

    public lessonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        fragmentText = view.findViewById(R.id.fragmentText);

        Bundle bundle = getArguments();

        if (LessonActivity.currentLesson.isInTheory()) {
            String message = LessonActivity.currentLesson.getLessonTheory().get(bundle.getInt("index")).getLessonText();
            fragmentText.setText(message);
        } else {
            String message = LessonActivity.currentLesson.getLessonExercise().get(bundle.getInt("index")).getText();
            fragmentText.setText(message);

            // finished exercise, show good job page
            if (LessonActivity.currentLesson.getLessonExercisePageNumber() == (bundle.getInt("index") + 1)) {
                Button button = view.findViewById(R.id.next);
                button.setVisibility(View.VISIBLE);
            }
        }

        //for debugging: show current position in swipe
        TextView showPosition = view.findViewById(R.id.position);
        showPosition.setText("Page " + bundle.getInt("index") + "from " + LessonActivity.swipeAdapter.getCount());

        return view;
    }

}
