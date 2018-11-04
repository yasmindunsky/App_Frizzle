package com.frizzl.app.frizzleapp.practice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class IntroSwipeAdapter extends FragmentStatePagerAdapter {

    private Practice introPractice;
    private int numOfSlides;

    public IntroSwipeAdapter(FragmentManager fm, Practice introPractice) {
        super(fm);
        this.introPractice = introPractice;
        numOfSlides = introPractice.getNumOfSlides();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        fragment = new IntroSlideFragment();
        bundle.putSerializable("intro_slide", introPractice.getPracticeSlides().get(position));

        bundle.putInt("lesson", introPractice.getID());
        bundle.putInt("index", position);
        bundle.putInt("numOfSlides", numOfSlides);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfSlides;
    }
}