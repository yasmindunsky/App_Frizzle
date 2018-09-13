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
import com.frizzl.app.frizzleapp.intro.LoginActivity;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

import java.util.ArrayList;
import java.util.Arrays;

public class MapActivity extends AppCompatActivity {

    private int currentLevel;
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
                mapPresenter.onClickedApp(v);
            }
        };
        View.OnClickListener onClickedPractice = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPresenter.onClickedPractice(v);
            }
        };
        tutorialAppButton.setOnClickListener(onClickedApp);
        pollyAppButton.setOnClickListener(onClickedApp);
        friendshipTestAppButton.setOnClickListener(onClickedApp);
        speakOutPracticeButton.setOnClickListener(onClickedPractice);
        onClickPracticeButton.setOnClickListener(onClickedPractice);
        viewsPracticeButton.setOnClickListener(onClickedPractice);
        variablesPracticeButton.setOnClickListener(onClickedPractice);

        currentLevel = mapPresenter.getCurrentLevel();
        for (int i = 0; i < levelButtons.size(); i++) {
            MapButton mapButton = levelButtons.get(i);
            if (i >= currentLevel) {
                mapButton.setEnabled(false);
            }
            else {
                mapButton.setCompleted(true);
            }
        }
        levelButtons.get(currentLevel).setEnabled(true);

        // Set Toolbar sign-out button.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                UserProfile.user.restartUserProfile();

                Intent signOutIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(signOutIntent);
            }
        });

        // Set scroll position.
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

//    public void navigateToLesson(){
//        Intent lessonIntent = new Intent(this, LessonActivity.class);
//        startActivity(lessonIntent);
//    }
//
//    public void goToPlayground(View view) {
//        Intent appBuilderIntent = new Intent(this, AppBuilderActivity.class);
//        startActivity(appBuilderIntent);
//    }

    public void goToApp() {
        Intent appIntent = new Intent(this, AppBuilderActivity.class);
        startActivity(appIntent);
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
