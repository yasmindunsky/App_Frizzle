package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;

import java.util.Locale;


public class MapActivity extends AppCompatActivity {

    public static final String ID_KEY = "frizzle.id.key";
    AnimationDrawable rocketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ImageView rocketImage = (ImageView) findViewById(R.id.imageView);
        rocketImage.setBackgroundResource(R.drawable.animation_test);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();


    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rocketAnimation.start();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void goToLesson(View view){

        // getting the lesson id by the text on the pressed button
        int buttonID = view.getId();
        Button button = view.findViewById(buttonID);
        String lessonNumber = button.getText().toString();

        // calling the lesson activity with lesson id
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        lessonIntent.putExtra(ID_KEY,lessonNumber);

        startActivity(lessonIntent);
    }

    public void goToPlayground(View view){

        // getting the lesson id by the text on the pressed button
        int buttonID = view.getId();
        Button button = view.findViewById(buttonID);
        String lessonNumber = button.getText().toString();

        // calling the lesson activity with lesson id
        Intent lessonIntent = new Intent(this, AppBuilderActivity.class);
        lessonIntent.putExtra(ID_KEY,lessonNumber);

        startActivity(lessonIntent);
    }
}
