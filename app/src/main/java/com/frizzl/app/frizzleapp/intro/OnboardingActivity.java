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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Button button = findViewById(R.id.yayButton);
        button.setOnClickListener(v -> {
            UserProfile.user.loadSerializedObject(getBaseContext());
            Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
            startActivity(mapIntent);
        });

        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            viewPager.setRotationY(180);
//        }
    }
}



