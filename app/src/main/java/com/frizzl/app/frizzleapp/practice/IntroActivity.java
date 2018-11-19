package com.frizzl.app.frizzleapp.practice;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;
import com.frizzl.app.frizzleapp.SwipeDirection;
import com.frizzl.app.frizzleapp.UserProfile;

import org.xmlpull.v1.XmlPullParserException;

public class IntroActivity extends FragmentActivity {
    private  Practice currentPractice;
    private PracticeViewPager viewPager;
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
            currentPractice = practiceContentParser.parsePractice(this,
                    levelID,
                    "intro");
        } catch (XmlPullParserException e) {
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
        IntroSwipeAdapter swipeAdapter =
                new IntroSwipeAdapter(getSupportFragmentManager(), currentPractice);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(position);
                SwipeDirection swipeDirection;
                if (position < UserProfile.user.getTopSlideInLevel()) {
                    swipeDirection = SwipeDirection.all;
                }
                else {
                    swipeDirection = SwipeDirection.left; // This is the setting also for RTL.
                }
                viewPager.setAllowedSwipeDirection(swipeDirection);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        if (currentPractice.getID() == UserProfile.user.getTopLevel()) {
            int currentSlide = UserProfile.user.getCurrentSlideInLevel();
            if (currentSlide != 0) {
                viewPager.setCurrentItem(currentSlide);
                progressBar.setProgress(currentSlide);
            }
        }

        // Rotation for RTL swiping.
        if (ViewUtils.isRTL()) {
            viewPager.setRotationY(180);
        }
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



