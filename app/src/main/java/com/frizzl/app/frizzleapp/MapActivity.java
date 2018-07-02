package com.frizzl.app.frizzleapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.frizzl.app.frizzleapp.appBuilder.AppBuilderActivity;
import com.frizzl.app.frizzleapp.intro.LoginActivity;
import com.frizzl.app.frizzleapp.lesson.LessonActivity;
import com.frizzl.app.frizzleapp.notifications.NotificationUtils;
import com.frizzl.app.frizzleapp.notifications.ReminderUtils;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private int topCourseId;
    private int currentCourseId;
    private int topLessonId;
    private int currentLessonId;
    private MapPresenter mapPresenter;
    private ArrayList<Button> lessonButtons = new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: robotics
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        for (int i = 1; i <= 8; i++){
            int buttonIdentifier = getResources().getIdentifier(String.format("lesson%s", i), "id", getPackageName());
            Button lessonButton = findViewById(buttonIdentifier);
            lessonButton.setTag(i);
            lessonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapPresenter.onClickedLesson(v);
                }
            });
            lessonButtons.add(lessonButton);
        }
        toolbar = findViewById(R.id.mapToolbar);

        mapPresenter = new MapPresenter(this);

        currentCourseId = UserProfile.user.getCurrentCourseID();
        topCourseId = UserProfile.user.getTopCourseID();
        currentLessonId = UserProfile.user.getCurrentLessonID();
        topLessonId = UserProfile.user.getTopLessonID();

//        if (topLessonId < 9){
//            UserProfile.user.setCurrentLessonID(9);
//            currentLessonId = 9;
//            UserProfile.user.setTopLessonID(9);
//            topLessonId = 9;
//            UserProfile.user.setCurrentCourseID(2);
//            currentCourseId = 2;
//            UserProfile.user.setTopCourseID(2);
//            topCourseId = 2;
//        }

        // Set Toolbar sign-out button.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                UserProfile.user.restartUserProfile();

                Intent signOutIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(signOutIntent);
            }
        });

        // Wait for layout to finish loading before setting map design, since views position
        // is not fully set yet.
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setMapDesign();
            }
        });

        // Set notifications
//        ReminderUtils.initialize(this);
    }

    private void setMapDesign() {
        Bitmap bitmap = Bitmap.createBitmap(getWindowManager()
                .getDefaultDisplay().getWidth(), getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bitmap);

        String[] colors = getResources().getStringArray(R.array.frizzleColors);
        String[] betweenColors = getResources().getStringArray(R.array.frizzleBetweenColors);

        Button nextButton = findViewById(R.id.lesson1);
//TODO: robotics
        int numOfLessons = 8;
        for (int i = 2; i <= numOfLessons; i++) {
            Button currentButton = nextButton;

            String id = "lesson" + String.valueOf(i);
            int identifier = getResources().getIdentifier(id , "id", this.getPackageName());
            nextButton = findViewById(identifier);

            // Draw line to next button.
            //TODO: robotics
            drawLessonsLine(canvas, currentButton, nextButton, colors, i);
            // Draw current button.
            drawLessonCircle(canvas, currentButton, betweenColors, i);
        }

        drawLessonCircle(canvas, nextButton, betweenColors, numOfLessons+1);
    }

    private void drawLessonsLine(Canvas canvas, Button currentButton, Button nextButton, String[] colors, int i) {
        Paint paint = new Paint();

        int length = colors.length;
        if (i <= topLessonId) {
            int lineColor = getResources().getIdentifier(colors[(i-2)%length], "color", this.getPackageName());
            paint.setColor(getResources().getColor(lineColor));
        }
        else {
            paint.setColor(getResources().getColor(R.color.UnselectedLightGrey));
        }

        paint.setStrokeWidth(currentButton.getLayoutParams().width);

        Rect currentButtonRect = new Rect();
        currentButton.getGlobalVisibleRect(currentButtonRect);
        Rect nextButtonRect = new Rect();
        nextButton.getGlobalVisibleRect(nextButtonRect);

        canvas.drawLine(currentButtonRect.centerX(), currentButtonRect.centerY(),
                nextButtonRect.centerX(), nextButtonRect.centerY(), paint);
    }

    private void drawLessonCircle(Canvas canvas, Button button, String[] betweenColors, int i) {
            Paint paint = new Paint();
        int length = betweenColors.length;
        if (i <= topLessonId + 1) {
            paint.setColor(getResources().getColor(getResources().getIdentifier(betweenColors[(i-2)%length], "color", this.getPackageName())));        }
        else {
            paint.setColor(getResources().getColor(R.color.unselectedDarkGrey));
        }

        int[] location = new int[2];
        button.getLocationOnScreen(location);
        float circleX = location[0] + button.getWidth()/2;
        float circleY = location[1] + button.getHeight()/2;

//        float circleX = button.getX() + (button.getWidth()/2);
//        float circleY = button.getY() + (button.getHeight());
        int radius = button.getLayoutParams().width / 2;
        canvas.drawCircle(circleX,circleY, radius,paint);

        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        if (i == currentLessonId + 1) {
            canvas.drawCircle(circleX, circleY, radius, paint);
            canvas.drawCircle(circleX, circleY, radius+20, paint);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20 * getResources().getDisplayMetrics().density);
        paint.setTextAlign(Paint.Align.CENTER);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.calibri_regular);
            paint.setTypeface(typeface);
        }
        canvas.drawText(String.valueOf(i-1), circleX, circleY+20, paint);
    }

    public void navigateToLesson(){
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }

    public void goToPlayground(View view) {
        Intent appBuilderIntent = new Intent(this, AppBuilderActivity.class);
        startActivity(appBuilderIntent);
    }

    public void testNotification(View view){
        NotificationUtils.remindUser(this);
    }

//    public void goToLesson(View view) {
//        // Get the lesson id from the text on the clicked button.
//        int buttonID = view.getId();
//        Button button = view.findViewById(buttonID);
//        String lessonNumber = button.getText().toString();
//
//        // Not allowed to access this lesson yet.
//        if (Integer.parseInt(lessonNumber) > topLessonId) {
//            return;
//        }
//
//        UserProfile.user.setCurrentLessonID(Integer.parseInt(lessonNumber));
//
//        // Update current position of the user inside the lessons.
//        updateCurrentPosition(Integer.parseInt(lessonNumber));
//
//        // Start lesson activity.
//        Intent lessonIntent = new Intent(this, LessonActivity.class);
//        startActivity(lessonIntent);
//    }

//    private void updateCurrentPosition(int lessonNumber) {
//
//        UserProfile.user.setCurrentLessonID(lessonNumber);
//        UserProfile.user.setCurrentCourseID(1);
//        if (lessonNumber == 8) {
//            UserProfile.user.setCurrentCourseID(2);
//        }
//
//        // update position in server
//        new UpdatePositionInServer().execute();
//    }

}
