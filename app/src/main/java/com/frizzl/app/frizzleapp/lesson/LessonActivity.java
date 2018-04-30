package com.frizzl.app.frizzleapp.lesson;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.SwipeAdapter;
import com.frizzl.app.frizzleapp.UserProfile;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LessonActivity extends FragmentActivity {
    private static SwipeAdapter swipeAdapter;
    private static Lesson currentLesson;
    private static CustomViewPager viewPager;
    private boolean isAroundFragmentVisible;

    static public Lesson getCurrentLesson() {
        return currentLesson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Set Toolbar home button.
        android.support.v7.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to map home screen
                Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(mapIntent);
                finish();
            }
        });

        isAroundFragmentVisible = true;

        // create new Lesson by the current lesson id
        currentLesson = new Lesson(UserProfile.user.getCurrentLessonID());

        // Set lesson title to current number.
        TextView lessonTitle = findViewById(R.id.lessonTitle);
        lessonTitle.setText(getString(R.string.lesson_title) + " " + Integer.toString(UserProfile.user.getCurrentLessonID()));

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
        if (Support.isRTL()) {
            viewPager.setRotationY(180);
        }

        // Connecting TabLayout with ViewPager to show swipe position in dots.
        final TabLayout tabLayout = findViewById(R.id.dotsTabLayout);
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
                if (position == lastExerciseIndex || !isAroundFragmentVisible) {
                    switchAroundFragmentVisibilty();
                }
            }
        });
    }

    private void switchAroundFragmentVisibilty() {
        int newVisibility = isAroundFragmentVisible ? View.INVISIBLE : View.VISIBLE;

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(newVisibility);
        TabLayout dotsTabLayout = findViewById(R.id.dotsTabLayout);
        dotsTabLayout.setVisibility(newVisibility);

        FrameLayout frameLayout = findViewById(R.id.fragment);
        RelativeLayout.LayoutParams layoutParams = null;
        if (isAroundFragmentVisible) {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.dimen.slide_height);
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.dotsTabLayout);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
            layoutParams.addRule(RelativeLayout.ALIGN_END, R.id.toolbar);
        }
        frameLayout.setLayoutParams(layoutParams);

        isAroundFragmentVisible = !isAroundFragmentVisible;
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

    public static void setPagingEnabled(boolean b) {
        viewPager.setPagingEnabled(b);
    }
}



