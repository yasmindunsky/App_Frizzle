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

        Fragment fragment = new lessonFragment();
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        // switch between theory state to exercise state by the current position
        if (position + 1 > LessonActivity.currentLesson.getLessonTheoryPageNumber()) {
            LessonActivity.currentLesson.setInTheory(false);
        } else {
            LessonActivity.currentLesson.setInTheory(true);
        }

        // set the index as argument of the new fragment
        if (LessonActivity.currentLesson.isInTheory()) {
            bundle.putInt("index", position);
        } else {
            bundle.putInt("index", position - LessonActivity.currentLesson.getLessonTheoryPageNumber());
        }

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return LessonActivity.currentLesson.getLessonTheoryPageNumber() + LessonActivity.currentLesson.getLessonExercisePageNumber();
    }
}
