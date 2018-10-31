package com.frizzl.app.frizzleapp.lesson;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    TextView fragmentText;
    private FirebaseAnalytics mFirebaseAnalytics;
    int index;
    int appLevelId;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        view.setPadding(60,50,60,50);

        // Rotation for RTL swiping.
        if (Support.isRTL()) {
            view.setRotationY(180);
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        fragmentText = view.findViewById(R.id.taskText);
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        appLevelId = bundle.getInt("lesson");
        TextView taskNum = view.findViewById(R.id.taskNum);
        int taskNumToDisplay = index + 1;
        taskNum.setText(getString(R.string.task) + " " + taskNumToDisplay + ":");

        Task task = UserProfile.user.getCurrentAppTasks().getTasks().get(index);

        String taskText  = task.getText();
        fragmentText.setText(taskText);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName()+ appLevelId + "_" + index);
    }
}
