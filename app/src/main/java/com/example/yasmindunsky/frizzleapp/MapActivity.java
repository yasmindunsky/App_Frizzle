package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;


public class MapActivity extends AppCompatActivity {

    public static final String ID_KEY = "frizzle.id.key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        new CreateNewUser().execute();
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
