package com.frizzl.app.frizzleapp.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.appBuilder.AppBuilderActivity;
import com.frizzl.app.frizzleapp.lesson.AppContentParser;
import com.frizzl.app.frizzleapp.lesson.AppTasks;
import com.frizzl.app.frizzleapp.lesson.IntroActivity;
import com.frizzl.app.frizzleapp.lesson.PracticeActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;
    private ArrayList<MapButton> levelButtons = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView toolbarIcon;
    private ScrollView scrollView;
//    private AppMapButton tutorialAppButton;
    private IntroMapButton pollyIntroButton;
    private IntroMapButton friendshipIntroButton;
    private AppMapButton pollyAppButton;
    private AppMapButton friendshipTestAppButton;
    private PracticeMapButton firstPracticeButton;
    private PracticeMapButton speakOutPracticeButton;
    private PracticeMapButton onClickPracticeButton;
    private PracticeMapButton viewsPracticeButton;
    private PracticeMapButton variablesPracticeButton;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mapPresenter = new MapPresenter(this);

        constraintLayout = findViewById(R.id.constraintLayout);
        toolbar = findViewById(R.id.mapToolbar);
        toolbarIcon = findViewById(R.id.support_icon);
        scrollView = findViewById(R.id.map_scroll_view);
//        tutorialAppButton = findViewById(R.id.tutorial_app);
        pollyIntroButton = findViewById(R.id.polly_intro);
        friendshipIntroButton = findViewById(R.id.friendship_intro);
        pollyAppButton = findViewById(R.id.polly_app);
        friendshipTestAppButton = findViewById(R.id.friendship_app);
        firstPracticeButton = findViewById(R.id.first_practice);
        speakOutPracticeButton = findViewById(R.id.speakout_practice);
        onClickPracticeButton = findViewById(R.id.onclick_practice);
        viewsPracticeButton = findViewById(R.id.views_practice);
        variablesPracticeButton = findViewById(R.id.variables_practice);
        // Notice: Should be in order of appearance!
        levelButtons.addAll(Arrays.asList(
                pollyIntroButton,
                firstPracticeButton,
                speakOutPracticeButton,
                onClickPracticeButton,
                pollyAppButton,
                friendshipIntroButton,
                viewsPracticeButton,
                variablesPracticeButton,
                friendshipTestAppButton));

        PopupWindow helpPopupWindow = new HelpPopupWindow(this);
        toolbarIcon.setOnClickListener(v -> Support.presentPopup(helpPopupWindow, null, mainLayout, mainLayout,
                getApplicationContext()));

        View.OnClickListener onClickedApp = v -> {
            AppMapButton appMapButton = (AppMapButton) v;
            int levelID = appMapButton.getLevelID();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL_ID", levelID);
            mFirebaseAnalytics.logEvent("STARTED_APP", bundle);
            mapPresenter.onClickedApp(levelID);
        };
        View.OnClickListener onClickedPractice = v -> {
            PracticeMapButton practiceMapButton = (PracticeMapButton)v;
            int practiceLevelID = practiceMapButton.getPracticeID();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL_ID", practiceLevelID);
            mFirebaseAnalytics.logEvent("STARTED_PRACTICE", bundle);
            mapPresenter.onClickedPractice(practiceLevelID);
        };
        View.OnClickListener onClickedIntro = v -> {
            IntroMapButton introMapButton = (IntroMapButton)v;
            int levelID = introMapButton.getLevelID();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL_ID", levelID);
            mFirebaseAnalytics.logEvent("STARTED_INTRO", bundle);
            mapPresenter.onClickedIntro(levelID);
        };
//        tutorialAppButton.setOnClickListener(onClickedApp);
        pollyAppButton.setOnClickListener(onClickedApp);
        friendshipTestAppButton.setOnClickListener(onClickedApp);
        firstPracticeButton.setOnClickListener(onClickedPractice);
        speakOutPracticeButton.setOnClickListener(onClickedPractice);
        onClickPracticeButton.setOnClickListener(onClickedPractice);
        viewsPracticeButton.setOnClickListener(onClickedPractice);
        variablesPracticeButton.setOnClickListener(onClickedPractice);
        pollyIntroButton.setOnClickListener(onClickedIntro);

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
                mapButton.setEnabled(false);
            }
            else {
                mapButton.setEnabled(true);
                mapButton.setCompleted(true);
            }
        }
        levelButtons.get(topLevel).setEnabled(true);
    }

    public void goToApp(int levelID) {
        // parse app & update user profile
        AppContentParser appContentParser;
        try {
            appContentParser = new AppContentParser();
            AppTasks appTasks = appContentParser.parseAppXml(this, levelID);
            UserProfile.user.setCurrentAppTasks(appTasks);
            UserProfile.user.setCurrentUserAppLevelID(levelID);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        // go to app builder
        Intent appIntent = new Intent(this, AppBuilderActivity.class);
        appIntent.putExtra("appLevelID", levelID);
        startActivity(appIntent);
    }

    public void goToPractice(int practiceID) {
        // Present pre-practice pop-up
        final Context applicationContext = getApplicationContext();
        PopupWindow popupWindow = mapPresenter.getPrePracticePopup();
        Runnable startPractice = () -> {
            // go to practice
            Intent practiceIntent = new Intent(applicationContext, PracticeActivity.class);
            practiceIntent.putExtra("practiceID", practiceID);
            startActivity(practiceIntent);
        };
        Support.presentPopup(popupWindow, startPractice, constraintLayout, constraintLayout, applicationContext);


    }

    public void goToIntro(int levelID) {
        Intent introIntent = new Intent(getApplicationContext(), IntroActivity.class);
        introIntent.putExtra("levelID", levelID);
        startActivity(introIntent);
    }
}