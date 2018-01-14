package com.example.yasmindunsky.frizzleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.yasmindunsky.frizzleapp.lesson.Lesson;
import com.example.yasmindunsky.frizzleapp.lesson.exercise.ExerciseFragment;
import com.example.yasmindunsky.frizzleapp.lesson.SlideFragment;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class SwipeAdapter extends FragmentStatePagerAdapter {

    private Lesson currentLesson;
    private int numOfSlides;
    private int numOfFragments;

    public SwipeAdapter(FragmentManager fm, Lesson currentLesson) {
        super(fm);
        this.currentLesson = currentLesson;
        numOfSlides = currentLesson.getSlidesNumber();
        numOfFragments = numOfSlides + currentLesson.getExercisesNumber() + 1;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        // create the right type of fragment by the current position inside the lesson
        if (position < numOfSlides) {
            fragment = new SlideFragment();
            currentLesson.setInSlides(true);
        } else if (position < getCount() - 1) {
            fragment = new ExerciseFragment();
            currentLesson.setInSlides(false);
        } else {
            fragment = new NavigationFragment();
        }

        // set the index as argument of the new fragment
        if (currentLesson.isInSlides()) {
            bundle.putInt("index", position);
        } else {
            bundle.putInt("index", position - currentLesson.getSlidesNumber());
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        //TODO added 1 for the last page in the lesson - consider this
        return numOfFragments;
    }

}