package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        // Wait for layout to finish loading before setting map design, since views position
        // is not fully set yet.
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setMapDesign();
            }
        });
    }

    private void setMapDesign() {
        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bitmap);
        Paint paint = new Paint();

        String[] colors = getResources().getStringArray(R.array.frizzleColors);
        String[] betweenColors = getResources().getStringArray(R.array.frizzleBetweenColors);

        Button nextButton = findViewById(R.id.lesson1);

        for (int i = 2; i <= 7; i++) {
            Button currentButton = nextButton;

            String id = "lesson" + String.valueOf(i);
            int identifier = getResources().getIdentifier(id , "id", this.getPackageName());
            nextButton = findViewById(identifier);

            // TODO Set color.
            setLessonCircleColor(currentButton, betweenColors, i);

            // Draw line to next button.
            paint = new Paint();
            int lineColor = getResources().getIdentifier(colors[i-2], "color", this.getPackageName());
            paint.setColor(getResources().getColor(lineColor));
            paint.setStrokeWidth(currentButton.getLayoutParams().width);


            float startX =
                    (currentButton.getLeft() + (currentButton.getWidth()/2));
//                    (currentButton.getLeft() + currentButton.getRight())/2;
            float startY =
                     (currentButton.getTop() + (currentButton.getHeight()/2));
//                     ((currentButton.getBottom() + currentButton.getTop())/2) + 15;
            float endX = (nextButton.getLeft() + nextButton.getRight())/2;
            float endY = ((nextButton.getBottom() + nextButton.getTop())/2) - 10;

            Rect currentButtonRect = new Rect();
            currentButton.getGlobalVisibleRect(currentButtonRect);
            Rect nextButtonRect = new Rect();
            nextButton.getGlobalVisibleRect(nextButtonRect);
//            canvas.drawRect(currentButtonRect, paint);

            canvas.drawLine(currentButtonRect.centerX(), currentButtonRect.centerY(),
                    nextButtonRect.centerX(), nextButtonRect.centerY(), paint);

//            canvas.drawPoint(currentButtonRect.centerX(), currentButtonRect.centerY(), paint);


        }

        setLessonCircleColor(nextButton, betweenColors, betweenColors.length-1);
    }

    private void setLessonCircleColor(Button button, String[] betweenColors, int i) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.map_lesson_circle);
        drawable = drawable.mutate();
        int circleColor = getResources().getIdentifier(betweenColors[i-2], "color", this.getPackageName());
        drawable.setColorFilter(getResources().getColor(circleColor), PorterDuff.Mode.DARKEN);
        button.setBackground(drawable);
    }

    public void goToLesson(View view) {

        // get the lesson id by the text on the pressed button
        int buttonID = view.getId();
        Button button = view.findViewById(buttonID);
        String lessonNumber = button.getText().toString();

        UserProfile.user.setCurrentLessonID(Integer.parseInt(lessonNumber));

        // update current position of the user inside the lessons
        updateCurrentPosition(Integer.parseInt(lessonNumber));

        // start lesson activity
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }

    private void updateCurrentPosition(int lessonNumber) {

        UserProfile.user.setCurrentLessonID(lessonNumber);
        UserProfile.user.setCurrentCourseID(1);

        // update position in server
        new UpdatePositionInServer().execute();
    }

    public void goToPlayground(View view) {
        // calling the app builder activity with lesson id
        Intent appBuilderIntent = new Intent(this, AppBuilderActivity.class);
        startActivity(appBuilderIntent);
    }
}
