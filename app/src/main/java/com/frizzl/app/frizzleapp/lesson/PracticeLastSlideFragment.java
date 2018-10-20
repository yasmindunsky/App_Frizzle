package com.frizzl.app.frizzleapp.lesson;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;

/**
 * Created by Noga on 22/09/2018.
 */

public class PracticeLastSlideFragment extends android.support.v4.app.Fragment {

    private int levelID;

    public PracticeLastSlideFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice_end, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) this.levelID = bundle.getInt("lesson");

        Button thanksButton = view.findViewById(R.id.thanksButton);
        thanksButton.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            if (activity != null) activity.onBackPressed();
            UserProfile.user.finishedPractice(levelID);
        });
        return view;
    }
}