package com.frizzl.app.frizzleapp.lesson;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

//import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    TextView fragmentText;
    private FirebaseAnalytics mFirebaseAnalytics;
    int index;
    int appId;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            view.setRotationY(180);
//        }

        fragmentText = view.findViewById(R.id.textView2);
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        appId = bundle.getInt("lesson");

        Task task = UserProfile.user.getCurrentApp().getTasks().get(index);

        String taskText  = task.getText();
        fragmentText.setText(taskText);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName()+ appId +"_"+index);
    }

    public void showTextAndIllustration(String text){
        fragmentText.setText(text);
    }
}
