package com.example.yasmindunsky.frizzleapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link goToAppBuilderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link goToAppBuilderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class goToAppBuilderFragment extends Fragment {

    public goToAppBuilderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        // Rotation for RTL swiping.
        view.setRotationY(180);

        // Update the top position of the user if needed
        updateTopPosition();

        view.findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appBuilderActivity = new Intent(getActivity(), AppBuilderActivity.class);
                startActivity(appBuilderActivity);
            }
        });

        return view;
    }

    private void updateTopPosition() {
        int lessonNumber = UserProfile.user.getCurrentLessonID();
        if (lessonNumber + 1 > UserProfile.user.getTopLessonID()) {
            UserProfile.user.setTopLessonID(lessonNumber + 1);
            UserProfile.user.setTopCourseID(1);

            // update position in server
            new UpdatePositionInServer().execute();
        }
    }
}
