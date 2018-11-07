package com.frizzl.app.frizzleapp.practice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.AnalyticsUtils;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    TextView fragmentText;
    int index;
    int appLevelId;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        view.setPadding(60,50,60,50);

        // Rotation for RTL swiping.
        if (Utils.isRTL()) {
            view.setRotationY(180);
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        fragmentText = view.findViewById(R.id.taskText);
        Bundle bundle = getArguments();
        assert bundle != null;
        index = bundle.getInt("index");
        appLevelId = bundle.getInt("lesson");
        TextView taskNum = view.findViewById(R.id.taskNum);
        int taskNumToDisplay = index + 1;
        taskNum.setText(String.format("%s %d:", getString(R.string.task), taskNumToDisplay));

        Task task = UserProfile.user.getCurrentAppTasks().getTasks().get(index);

        String taskText  = task.getText();
        fragmentText.setText(taskText);

//        com.airbnb.lottie.LottieAnimationView animationView = view.findViewById(R.id.checkMark);
//        animationView.animate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsUtils.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName()+ appLevelId + "_" + index);
    }
}
