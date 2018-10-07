package com.frizzl.app.frizzleapp.intro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class OnboardingActivity extends FragmentActivity{
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Button button = findViewById(R.id.yayButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile.user.loadSerializedObject(getBaseContext());
                Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

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

        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            viewPager.setRotationY(180);
//        }
    }
}



