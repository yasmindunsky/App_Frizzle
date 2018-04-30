package com.frizzl.app.frizzleapp.intro;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;

public class OnboardingActivity extends FragmentActivity{
    private static OnboardingSwipeAdapter swipeAdapter;
    private CustomViewPager viewPager;

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
        final TabLayout tabLayout = findViewById(R.id.dotsTabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}



