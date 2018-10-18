package com.frizzl.app.frizzleapp.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.map.MapActivity;

public class OnboardingActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(v -> {
            setContentView(R.layout.activity_onboarding2);
            Button yayButton = findViewById(R.id.yayButton);
            yayButton.setOnClickListener(view -> {
            UserProfile.user.loadSerializedObject(getBaseContext());
            Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
            startActivity(mapIntent);
            });
        });

        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            viewPager.setRotationY(180);
//        }
    }
}



