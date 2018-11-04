package com.frizzl.app.frizzleapp.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
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

        if (Support.isRTL()) {
            view.setRotationY(180);
        }

        Bundle bundle = getArguments();
        if (bundle != null) this.levelID = bundle.getInt("lesson");

        Button thanksButton = view.findViewById(R.id.thanksButton);
        thanksButton.setOnClickListener(v -> {
            PracticeActivity activity = (PracticeActivity) getActivity();
            if (activity != null) activity.goBack();
            UserProfile.user.finishedPractice(levelID);
        });
        return view;
    }
}