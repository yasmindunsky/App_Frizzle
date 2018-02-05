package com.example.yasmindunsky.frizzleapp.lesson;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.SwipeAdapter;
import com.example.yasmindunsky.frizzleapp.UserProfile;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LessonActivity extends FragmentActivity {
    private static SwipeAdapter swipeAdapter;
    private static Lesson currentLesson;
    private ViewPager viewPager;

    static public Lesson getCurrentLesson() {
        return currentLesson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Set Toolbar home button.
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to map home screen
                Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

        // create new Lesson by the current lesson id
        currentLesson = new Lesson(UserProfile.user.getCurrentLessonID());

        // Set lesson title to current number.
        TextView lessonTitle = (TextView) findViewById(R.id.lessonTitle);
        lessonTitle.setText(getString(R.string.lessonTitle) + " " + Integer.toString(UserProfile.user.getCurrentLessonID()));

        // parse xml file to insert content to the currentLesson
        LessonContentParser lessonContentParser = null;
        try {
            lessonContentParser = new LessonContentParser(this);
            lessonContentParser.parseLesson();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create SwipeAdapter.
        viewPager = findViewById(R.id.pager);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), currentLesson);
        viewPager.setAdapter(swipeAdapter);
        // Rotation for RTL swiping.
        viewPager.setRotationY(180);

        // Connecting TabLayout with ViewPager to show swipe position in dots.
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.dotsTabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        // Hide Exercise or Slides Tabs according to current fragment.
        final int firstExerciseIndex = currentLesson.getSlidesNumber();
        final int lastExerciseIndex = firstExerciseIndex + currentLesson.getExercisesNumber();
        ChangeDotsVisibility(tabLayout, View.VISIBLE, firstExerciseIndex, lastExerciseIndex);
        // TODO: Create a customized viewPager with these overridden methods and use it instead.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageSelected(final int position) {
                if (position == firstExerciseIndex) {
                    ChangeDotsVisibility(tabLayout, View.GONE, firstExerciseIndex, lastExerciseIndex);
                }
                if (position == firstExerciseIndex - 1) {
                    ChangeDotsVisibility(tabLayout, View.VISIBLE, firstExerciseIndex, lastExerciseIndex);
                }
            }
        });
    }


    /**
     * Alternates visibility of dots before and after switchingIndex in TabLayout. Dots before
     * switchingIndex will receive firstPartVisibility, and dots after will receive the opposite.
     * Also alternates between the check-mark and back arrow icons.
     *
     * @param tabLayout
     * @param firstPartVisibility
     * @param switchingIndex
     * @param lastIndex
     */
    private void ChangeDotsVisibility(
            TabLayout tabLayout, int firstPartVisibility, int switchingIndex, int lastIndex) {
        int secondPartVisibility = (firstPartVisibility == View.GONE) ? View.VISIBLE : View.GONE;
        LinearLayout layout = (LinearLayout) tabLayout.getChildAt(0);
        for (int i = 0; i <= lastIndex; i++) {
            int visibility = (i < switchingIndex) ? firstPartVisibility : secondPartVisibility;
            layout.getChildAt(i).setVisibility(visibility);
        }

        // The icon dot should always be visible, and change it's icon according to visibility:
        // if first part is visible, display slides_last_dot, otherwise display exercise_first_dot
        View iconDot = layout.getChildAt(switchingIndex - 1);
        iconDot.setVisibility(View.VISIBLE);
        int icon =
                (firstPartVisibility == View.VISIBLE) ?
                        R.drawable.slides_last_dot :
                        R.drawable.exercise_first_dot;
        iconDot.setBackgroundResource(icon);
    }
}



