package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.intro.LoginActivity;
import com.frizzl.app.frizzleapp.intro.OnboardingActivity;
import com.google.firebase.analytics.FirebaseAnalytics;


public class SecondCourseActivity extends AppCompatActivity {

    private int topLessonId;
    private int currentLessonId;
    private ConstraintLayout mainLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map_course_2, null);
        setContentView(mainLayout);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        currentLessonId = 1;
        topLessonId = 1;

        // Set Toolbar sign-out button.
        android.support.v7.widget.Toolbar toolbar =
                findViewById(R.id.mapToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to sign-out screen
                UserProfile.user.restartUserProfile();
                Intent signOutIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(signOutIntent);
            }
        });

        openUnlockPopup();
    }

    private void openUnlockPopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_unlock, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        mainLayout.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                openBetaWithoutUnlockPopup();
            }
        });

        // Set closing button.
        TextView closeButton = popupView.findViewById(R.id.skip);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                openBetaWithoutUnlockPopup();
            }
        });

        // dim
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        frameLayout.setForeground(getResources().getDrawable(R.drawable.shade));
        frameLayout.getForeground().setAlpha(150);

        LinearLayout linearLayout = popupView.findViewById(R.id.unlock);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                openBetaAfterUnlockPopup();
            }
        });
    }

    private void openBetaAfterUnlockPopup() {
        // inflate the popupLayout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate( R.layout.popup_beta_after_unlock, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        mainLayout.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
            }
        });

        // Set closing button.
        ImageButton closeButton = popupView.findViewById(R.id.close);
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        final EditText email = popupView.findViewById(R.id.email);
        AppCompatButton notifyButton = popupView.findViewById(R.id.notify);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to firebase
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", String.valueOf(email.getText()));
                mFirebaseAnalytics.logEvent("NOTIFY", bundle);
                popupWindow.dismiss();
            }
        });

        // dim background.
        mainLayout.setForeground(getApplicationContext().getResources().getDrawable(R.drawable.shade));
        mainLayout.getForeground().setAlpha(150);

        // User chose to unlock!
        Bundle bundle = new Bundle();
        bundle.putString("UNLOCKED", String.valueOf(true));
        mFirebaseAnalytics.logEvent("UNLOCK", bundle);
    }

    private void openBetaWithoutUnlockPopup() {
        // inflate the popupLayout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_beta_without_unlock, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        mainLayout.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
            }
        });

        // Set closing button.
        ImageButton closeButton = popupView.findViewById(R.id.close);
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        // dim background.
        mainLayout.setForeground(getApplicationContext().getResources().getDrawable(R.drawable.shade));
        mainLayout.getForeground().setAlpha(150);

        // User chose not to unlock
        Bundle bundle = new Bundle();
        bundle.putString("UNLOCKED", String.valueOf(false));
        mFirebaseAnalytics.logEvent("UNLOCK", bundle);

        Button doneButton = popupView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
                float rating = ratingBar.getRating();

                EditText feedbackEditText = popupView.findViewById(R.id.feedback);
                String feedback = String.valueOf(feedbackEditText.getText());

                // Send feedback somewhere
                Bundle bundle = new Bundle();
                bundle.putString("Feedback", feedback);
                mFirebaseAnalytics.logEvent("Feedback", bundle);

                // Send to firebase
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Rating");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(rating));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Intent startOverIntent = new Intent(getBaseContext(), OnboardingActivity.class);
                startActivity(startOverIntent);
            }
        });




    }
}
