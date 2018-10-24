package com.frizzl.app.frizzleapp.lesson;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PracticeActivity extends FragmentActivity {
    private  PracticeSwipeAdapter swipeAdapter;
    private  Practice currentPractice;
    private RoundCornerProgressBar progressBar;
    private  CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        // parse practice & update user profile
        int practiceID = getIntent().getIntExtra("practiceID", 1);
        PracticeContentParser practiceContentParser;
        try {
            practiceContentParser = new PracticeContentParser();
            String lessonXmlName = "practice_" + practiceID;
            currentPractice = practiceContentParser.parsePractice(this, practiceID, lessonXmlName);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        // Set home button.
        ImageButton mapButton = findViewById(R.id.home);
        mapButton.setOnClickListener(v -> goBack());

        progressBar = findViewById(R.id.progressBar);
        int numOfSlides = currentPractice.getNumOfSlides();
        progressBar.setMax(numOfSlides);
        progressBar.setProgress(0f);

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

    @Override
    public void onBackPressed() {
        int count = viewPager.getCurrentItem();
        if (count == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(count - 1);
            progressBar.setProgress(count - 1);
        }

    }

    public void goBack() {
        super.onBackPressed();
    }
}



