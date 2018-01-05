package com.example.yasmindunsky.frizzleapp.lesson;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.SwipeAdapter;

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

        // create new Lesson by the id received from intent
        Intent intent = getIntent();
        String lesson_id = intent.getStringExtra(MapActivity.ID_KEY);
        int id = Integer.parseInt(lesson_id);
        currentLesson = new Lesson(id);

        // Set lesson title to current number
        TextView lessonTitle = (TextView) findViewById(R.id.lessonTitle);
        lessonTitle.setText(getString(R.string.lessonTitle) + " " + lesson_id);

        // parse xml file to insert content to the currentLesson
        LessonContentParser lessonContentParser = null;
        try {
            lessonContentParser = new LessonContentParser(this);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            lessonContentParser.parseLesson();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start swipe between pages
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

        // Connecting TabLayout with ViewPager to show swipe position
        TabLayout tabLayout = (TabLayout) findViewById(R.id.dotsTabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        int lastIndex = tabLayout.getTabCount() - 1;
        ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(lastIndex).setBackgroundResource(R.drawable.last_dot);
    }
}



