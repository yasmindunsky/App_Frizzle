package com.frizzl.app.frizzleapp.intro;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.Calendar;

public class OnboardingActivity extends FragmentActivity{
    private static OnboardingSwipeAdapter swipeAdapter;
    private CustomViewPager viewPager;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        // Handle dynamic links
        FirebaseAnalytics.getInstance(getApplicationContext());
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String utm_campaign = deepLink.getQueryParameter("utm_campaign");
                            String utm_source = deepLink.getQueryParameter("utm_source");
                            // Send user property to firebase
                            mFirebaseAnalytics.setUserProperty("utm_campaign", utm_campaign);
                            mFirebaseAnalytics.setUserProperty("utm_source", utm_source);
                        }


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "getDynamicLink:onFailure", e);
                    }
                });

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



