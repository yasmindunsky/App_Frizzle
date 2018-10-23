package com.frizzl.app.frizzleapp.lesson;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class IntroActivity extends FragmentActivity {
    private  IntroSwipeAdapter swipeAdapter;
    private  Practice currentPractice;
    private  CustomViewPager viewPager;
    private RoundCornerProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        // parse practice & update user profile
        int levelID = getIntent().getIntExtra("levelID", 1);
        PracticeContentParser practiceContentParser;
        try {
            practiceContentParser = new PracticeContentParser();
            currentPractice = practiceContentParser.parsePractice(this, levelID, "intro");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        // Set home button.
        ImageButton mapButton = findViewById(R.id.home);
        mapButton.setOnClickListener(v -> onBackPressed());

        progressBar = findViewById(R.id.progressBar);
        int numOfSlides = currentPractice.getNumOfSlides();
        progressBar.setMax(numOfSlides);
        progressBar.setProgress(0f);

        // Create SwipeAdapter.
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new IntroSwipeAdapter(getSupportFragmentManager(), currentPractice);
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
}



