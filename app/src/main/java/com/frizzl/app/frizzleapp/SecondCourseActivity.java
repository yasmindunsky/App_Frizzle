package com.frizzl.app.frizzleapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.intro.LoginActivity;


public class SecondCourseActivity extends AppCompatActivity {

    private int topLessonId;
    private int currentLessonId;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map_course_2, null);
        setContentView(mainLayout);

        currentLessonId = 1;
        topLessonId = 1;

        // Set Toolbar sign-out button.
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.mapToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to sign-out screen
                UserProfile.user.initUser();
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

//        // Set closing button.
//        ImageButton closeButton = popupView.findViewById(R.id.close);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                openBetaWithoutUnlockPopup();
//            }
//        });

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
        Helper.showPopup(getApplicationContext(), R.layout.popup_beta_after_unlock, mainLayout);
    }

    private void openBetaWithoutUnlockPopup() {
        Helper.showPopup(getApplicationContext(), R.layout.popup_beta_without_unlock, mainLayout);
    }
}
