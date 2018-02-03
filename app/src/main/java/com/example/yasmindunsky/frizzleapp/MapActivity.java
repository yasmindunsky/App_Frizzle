package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yasmindunsky.frizzleapp.appBuilder.AppBuilderActivity;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public void goToLesson(View view) {

        // get the lesson id by the text on the pressed button
        int buttonID = view.getId();
        Button button = view.findViewById(buttonID);
        String lessonNumber = button.getText().toString();

        // update current position and top position if needed
        updatePositions(Integer.parseInt(lessonNumber));

        // start lesson activity
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }

    private void updatePositions(int lessonNumber) {

        // update positions in userProfile
        if (lessonNumber > UserProfile.user.getTopLessonID()) {
            UserProfile.user.setTopLessonID(lessonNumber);
            UserProfile.user.setTopCourseID(1);
        }
        UserProfile.user.setCurrentLessonID(lessonNumber);
        UserProfile.user.setCurrentCourseID(1);

        // update positions in server
        new UpdatePositionInServer().execute();
    }

    public void goToPlayground(View view) {
        // calling the app builder activity with lesson id
        Intent appBuilderIntent = new Intent(this, AppBuilderActivity.class);
        startActivity(appBuilderIntent);
    }
}
