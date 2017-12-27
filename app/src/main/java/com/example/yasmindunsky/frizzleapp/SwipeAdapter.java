package com.example.yasmindunsky.frizzleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class SwipeAdapter extends FragmentStatePagerAdapter {

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        // switch between theory state to exercise state by the current position
        if (position + 1 > LessonActivity.currentLesson.getSlidesNumber()) {
            fragment = new ExerciseFragment();
            LessonActivity.currentLesson.setInSlides(false);
        } else {
            fragment = new SlideFragment();
            LessonActivity.currentLesson.setInSlides(true);
        }

        // set the index as argument of the new fragment
        if (LessonActivity.currentLesson.isInSlides()) {
            bundle.putInt("index", position);
        } else {
            bundle.putInt("index", position - LessonActivity.currentLesson.getSlidesNumber());
        }

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return LessonActivity.currentLesson.getSlidesNumber() + LessonActivity.currentLesson.getExercisesNumber();
    }
}
