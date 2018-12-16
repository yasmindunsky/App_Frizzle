package com.frizzl.app.frizzleapp.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.frizzl.app.frizzleapp.AnalyticsUtils;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;
import com.frizzl.app.frizzleapp.appBuilder.AppBuilderActivity;
import com.frizzl.app.frizzleapp.practice.IntroActivity;
import com.frizzl.app.frizzleapp.practice.PracticeActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;
    private final ArrayList<MapButton> levelButtons = new ArrayList<>();
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private PracticeMapButton friendshipIntroButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout )
                this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        mapPresenter = new MapPresenter(this);

        constraintLayout = findViewById(R.id.constraintLayout);
        ImageView toolbarIcon = findViewById(R.id.support_icon);
        scrollView = findViewById(R.id.map_scroll_view);

        PracticeMapButton confessionsIntroButton = findViewById(R.id.confession_intro);
        friendshipIntroButton = findViewById(R.id.friendship_intro);
        AppMapButton confessionsAppButton = findViewById(R.id.confession_app);
        AppMapButton friendshipTestAppButton = findViewById(R.id.friendship_app);
        PracticeMapButton firstPracticeButton = findViewById(R.id.first_practice);
        PracticeMapButton speakOutPracticeButton = findViewById(R.id.speakout_practice);
        PracticeMapButton onClickPracticeButton = findViewById(R.id.onclick_practice);
        PracticeMapButton viewsPracticeButton = findViewById(R.id.views_practice);
        PracticeMapButton variablesPracticeButton = findViewById(R.id.variables_practice);

        // Notice: Should be in order of appearance!
        levelButtons.addAll(Arrays.asList(
                confessionsIntroButton,
                firstPracticeButton,
                speakOutPracticeButton,
                onClickPracticeButton,
                confessionsAppButton,
                friendshipIntroButton,
                viewsPracticeButton,
                variablesPracticeButton,
                friendshipTestAppButton));

        HelpPopupWindow helpPopupWindow = new HelpPopupWindow(this);

        toolbarIcon.setOnClickListener(v -> {
            ViewUtils.presentPopup(helpPopupWindow, null, mainLayout, mainLayout,
                    this);
            helpPopupWindow.switchFormVisibility(View.VISIBLE);

        });

        View.OnClickListener onClickedApp = v -> {
            AppMapButton button = (AppMapButton) v;
//            if (button.status == Status.disabled)
//                button.setCurrent();
//            else if (button.status == Status.current)
//                button.setCompleted();
//            else if (button.status == Status.completed)
//                button.setDisabled();
            int levelID = button.getLevelID();
            AnalyticsUtils.logStartedAppEvent(levelID);
            mapPresenter.onClickedApp(levelID);
        };

        View.OnClickListener onClickedPractice = v -> {
            PracticeMapButton button = (PracticeMapButton) v;
//            if (button.status == Status.disabled)
//                button.setCurrent();
//            else if (button.status == Status.current)
//                button.setCompleted();
//            else if (button.status == Status.completed)
//                button.setDisabled();
            int practiceLevelID = button.getPracticeID();
            AnalyticsUtils.logStartedPracticeEvent(practiceLevelID);
            mapPresenter.onClickedPractice(practiceLevelID);
        };
        View.OnClickListener onClickedIntro = v -> {
            PracticeMapButton introMapButton = (PracticeMapButton)v;
            int levelID = introMapButton.getPracticeID();
            AnalyticsUtils.logStartedIntroEvent(levelID);
            mapPresenter.onClickedIntro(levelID);
        };

        View.OnClickListener openNotifyMe = v -> {
            PopupWindow notifyMePopupWindow =
                    new NotifyMePopupWindow(this);
            ViewUtils.presentPopup(notifyMePopupWindow, null, constraintLayout, constraintLayout, this);
        };
        friendshipIntroButton.setOnClickListener(openNotifyMe);
        firstPracticeButton.setOnClickListener(onClickedPractice);
        speakOutPracticeButton.setOnClickListener(onClickedPractice);
        onClickPracticeButton.setOnClickListener(onClickedPractice);
        viewsPracticeButton.setOnClickListener(onClickedPractice);
        variablesPracticeButton.setOnClickListener(onClickedPractice);
        
        confessionsAppButton.setOnClickListeners(onClickedApp);
        confessionsIntroButton.setOnClickListener(onClickedIntro);

        // Set scroll position.
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onResume() {
        super.onResume();
        int topLevel = mapPresenter.getTopLevel();
        for (int i = 0; i < levelButtons.size(); i++) {
            MapButton mapButton = levelButtons.get(i);
            if (i >= topLevel) {
                mapButton.setDisabled();
            }
            else {
                mapButton.setCompleted();
            }
        }
        levelButtons.get(topLevel).setCurrent();
        // Focus on new course
        if (topLevel == 5){
            constraintLayout.post(new Runnable() {
                @Override
                public void run() {
                    friendshipIntroButton.getParent().requestChildFocus(friendshipIntroButton,friendshipIntroButton);
//                    scrollView.scrollTo(0, friendshipIntroButton.getBottom());
                }
            });
        }
    }

    public void goToApp(int levelID) {
        mapPresenter.parseAppAndUpdateUserProfile(levelID);

        // Go to app builder.
        Intent appIntent = new Intent(this, AppBuilderActivity.class);
        appIntent.putExtra("appLevelID", levelID);
        startActivity(appIntent);
    }

    public void goToPractice(int practiceID) {
        Runnable startPractice = () -> {
            // Go to practice.
            Intent practiceIntent = new Intent(getApplicationContext(), PracticeActivity.class);
            practiceIntent.putExtra("practiceID", practiceID);
            startActivity(practiceIntent);
        };

        // Present pre-practice pop-up.
        PopupWindow popupWindow = mapPresenter.getPrePracticePopup(startPractice);

        if(isFinishing()) startPractice.run();
        ViewUtils.presentPopup(popupWindow, null, constraintLayout, constraintLayout, this);
    }

    public void goToIntro(int levelID) {
        Intent introIntent = new Intent(getApplicationContext(), IntroActivity.class);
        introIntent.putExtra("levelID", levelID);
        startActivity(introIntent);
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
        } catch (Exception e) {
            Log.w("", "onStop()", e);
            super.onStop();
        }
    }
}