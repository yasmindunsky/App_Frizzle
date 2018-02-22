package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.Support;
import com.example.yasmindunsky.frizzleapp.SwipeAdapter;
import com.example.yasmindunsky.frizzleapp.UserProfile;
import com.example.yasmindunsky.frizzleapp.lesson.Lesson;
import com.example.yasmindunsky.frizzleapp.lesson.LessonContentParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class OnboardingActivity extends FragmentActivity {
    private static OnboardingSwipeAdapter swipeAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Create SwipeAdapter.
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new OnboardingSwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

        // Rotation for RTL swiping.
        if (Support.isRTL()) {
            viewPager.setRotationY(180);
        }

        // Connecting TabLayout with ViewPager to show swipe position in dots.
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.dotsTabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}



