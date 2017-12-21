package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LessonActivity extends FragmentActivity {

    public static Lesson currentLesson;
    public ViewPager viewPager;
    public static SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // TODO consider having a state with current lesson id
        // create new Lesson by the id received from intent
        Intent intent = getIntent();
        String lesson_id = intent.getStringExtra(MapActivity.ID_KEY);
        int id = Integer.parseInt(lesson_id);
        currentLesson = new Lesson(id);

        // parse xml file to insert content to the currentLesson
        LessonContentParser lessonContentParser = new LessonContentParser(this);
        try {
            lessonContentParser.parseLesson();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start swipe view of theoretical pages
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }
}



