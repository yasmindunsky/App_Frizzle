package com.frizzl.app.frizzleapp.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.appBuilder.AppBuilderActivity;
import com.frizzl.app.frizzleapp.lesson.AppContentParser;
import com.frizzl.app.frizzleapp.lesson.AppTasks;
import com.frizzl.app.frizzleapp.lesson.PracticeActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MapActivity extends AppCompatActivity {

    private MapPresenter mapPresenter;
    private ArrayList<MapButton> levelButtons = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private ScrollView scrollView;
    private AppMapButton tutorialAppButton;
    private AppMapButton pollyAppButton;
    private AppMapButton friendshipTestAppButton;
    private PracticeMapButton speakOutPracticeButton;
    private PracticeMapButton onClickPracticeButton;
    private PracticeMapButton viewsPracticeButton;
    private PracticeMapButton variablesPracticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        mapPresenter = new MapPresenter(this);

        toolbar = findViewById(R.id.mapToolbar);
        scrollView = findViewById(R.id.map_scroll_view);
        tutorialAppButton = findViewById(R.id.tutorial_app);
        pollyAppButton = findViewById(R.id.polly_app);
        friendshipTestAppButton = findViewById(R.id.friendship_app);
        speakOutPracticeButton = findViewById(R.id.speakout_practice);
        onClickPracticeButton = findViewById(R.id.onclick_practice);
        viewsPracticeButton = findViewById(R.id.views_practice);
        variablesPracticeButton = findViewById(R.id.variables_practice);
        levelButtons.addAll(Arrays.asList(tutorialAppButton,
                speakOutPracticeButton,
                onClickPracticeButton,
                pollyAppButton,
                viewsPracticeButton,
                variablesPracticeButton,
                friendshipTestAppButton));

        View.OnClickListener onClickedApp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppMapButton appMapButton = (AppMapButton) v;
                int appID = appMapButton.getAppID();
                mapPresenter.onClickedApp(appID);
            }
        };
        View.OnClickListener onClickedPractice = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeMapButton practiceMapButton = (PracticeMapButton)v;
                int practiceID = practiceMapButton.getPracticeID();
                mapPresenter.onClickedPractice(practiceID);
            }
        };
        tutorialAppButton.setOnClickListener(onClickedApp);
        pollyAppButton.setOnClickListener(onClickedApp);
        friendshipTestAppButton.setOnClickListener(onClickedApp);
        speakOutPracticeButton.setOnClickListener(onClickedPractice);
        onClickPracticeButton.setOnClickListener(onClickedPractice);
        viewsPracticeButton.setOnClickListener(onClickedPractice);
        variablesPracticeButton.setOnClickListener(onClickedPractice);

        // Set scroll position.
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
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

    public void goToApp(int appID) {
        // parse app & update user profile
        AppContentParser appContentParser = null;
        try {
            appContentParser = new AppContentParser();
            AppTasks appTasks = appContentParser.parseAppXml(this, appID);
            UserProfile.user.setCurrentAppTasks(appTasks);
            UserProfile.user.setCurrentUserAppID(appID);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // go to app builder
        Intent appIntent = new Intent(this, AppBuilderActivity.class);
        appIntent.putExtra("appID", appID);
        startActivity(appIntent);
    }

    public void goToPractice(int practiceID) {
        // go to app builder
        Intent practiceIntent = new Intent(this, PracticeActivity.class);
        practiceIntent.putExtra("practiceID", practiceID);
        startActivity(practiceIntent);
    }

//    private void updateCurrentPosition(int lessonNumber) {
//
//        UserProfile.user.setCurrentLessonID(lessonNumber);
//        UserProfile.user.setCurrentCourseID(1);
//        if (lessonNumber == 8) {
//            UserProfile.user.setCurrentCourseID(2);
//        }
//
//        // update position in server
//        new UpdatePositionInServer().execute();
//    }

}
