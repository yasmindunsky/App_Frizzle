package com.frizzl.app.frizzleapp.lesson;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PracticeActivity extends FragmentActivity {
    private static PracticeSwipeAdapter swipeAdapter;
    private static Practice currentPractice;
    private static CustomViewPager viewPager;
    private boolean isAroundFragmentVisible;

    static public Practice getCurrentPractice() {
        return currentPractice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        // parse practice & update user profile
        int practiceID = getIntent().getIntExtra("practiceID", 1);
        PracticeContentParser practiceContentParser = null;
        try {
            practiceContentParser = new PracticeContentParser();
            currentPractice = practiceContentParser.parsePractice(this, practiceID);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        // Set home button.
        ImageButton mapButton = findViewById(R.id.home);
        mapButton.setOnClickListener(v -> onBackPressed());

        RoundCornerProgressBar progressBar = findViewById(R.id.progressBar);
        int numOfSlides = currentPractice.getNumOfSlides();
        progressBar.setMax(numOfSlides);
        progressBar.setProgress(1f);

        isAroundFragmentVisible = true;

        // Create SwipeAdapter.
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new PracticeSwipeAdapter(getSupportFragmentManager(), currentPractice);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setPagingEnabled(false);
        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            viewPager.setRotationY(180);
//        }
    }
}



