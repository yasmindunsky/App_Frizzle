package com.frizzl.app.frizzleapp.practice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;
import com.frizzl.app.frizzleapp.SwipeDirection;
import com.frizzl.app.frizzleapp.UserProfile;

import org.xmlpull.v1.XmlPullParserException;

public class PracticeActivity extends FragmentActivity {
    private Practice currentPractice;
    private RoundCornerProgressBar progressBar;
    private PracticeViewPager viewPager;

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
        PracticeSwipeAdapter swipeAdapter = new PracticeSwipeAdapter(getSupportFragmentManager(), currentPractice);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(position);
                SwipeDirection swipeDirection;
                if (position < UserProfile.user.getTopSlideInLevel()) {
                    swipeDirection = SwipeDirection.all;
                }
                else {
                    swipeDirection = SwipeDirection.left; // This is the setting also for RTL
                }
                viewPager.setAllowedSwipeDirection(swipeDirection);
                hideKeyboardIfNeeded();

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

    private void hideKeyboardIfNeeded() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        int count = viewPager.getCurrentItem();
        if (count == 0) {
            goBack();
        } else {
            viewPager.setCurrentItem(count - 1);
            progressBar.setProgress(count - 1);
        }
    }

    public void goBack() {
        super.onBackPressed();
        if (currentPractice.getID() == UserProfile.user.getTopLevel()) {
            UserProfile.user.setCurrentSlideInLevel(viewPager.getCurrentItem());
        }
    }

    public ViewGroup getMainLayout() {
        return findViewById(R.id.constraintLayout);
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



