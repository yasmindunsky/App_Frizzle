package com.example.yasmindunsky.frizzleapp;

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

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;
import com.example.yasmindunsky.frizzleapp.intro.RegisterActivity;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_map, null);
        setContentView(mainLayout);

        // Set Toolbar sign-out button.
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.mapToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to sign-out screen
                Intent signOutIntent = new Intent(getBaseContext(), RegisterActivity.class);
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
    }

    private void setMapDesign() {
        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bitmap);

        String[] colors = getResources().getStringArray(R.array.frizzleColors);
        String[] betweenColors = getResources().getStringArray(R.array.frizzleBetweenColors);

        Button nextButton = findViewById(R.id.lesson1);

        int numOfLessons = 7;
        for (int i = 2; i <= numOfLessons; i++) {
            Button currentButton = nextButton;

            String id = "lesson" + String.valueOf(i);
            int identifier = getResources().getIdentifier(id , "id", this.getPackageName());
            nextButton = findViewById(identifier);

            // Draw line to next button.
            drawLessonsLine(canvas, currentButton, nextButton, colors, i);

            // Draw current button.
            drawLessonCircle(canvas, currentButton, betweenColors, i);
        }

        drawLessonCircle(canvas, nextButton, betweenColors, numOfLessons+1);
    }

    private void drawLessonsLine(Canvas canvas, Button currentButton, Button nextButton, String[] colors, int i) {
        Paint paint = new Paint();
        int lineColor = getResources().getIdentifier(colors[i-2], "color", this.getPackageName());
        paint.setColor(getResources().getColor(lineColor));
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
        paint.setColor(getResources().getColor(getResources().getIdentifier(betweenColors[i-2], "color", this.getPackageName())));
        float circleX = button.getX() + (button.getWidth()/2);
        float circleY = button.getY() + (button.getHeight());
        canvas.drawCircle(circleX,circleY,button.getLayoutParams().width/2,paint);
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setTextSize(R.dimen.mapButtontextSize * getResources().getDisplayMetrics().density);
        paint.setTextAlign(Paint.Align.CENTER);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.calibri_regular);
            paint.setTypeface(typeface);
        }
        canvas.drawText(String.valueOf(i-1), circleX, circleY, paint);
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
