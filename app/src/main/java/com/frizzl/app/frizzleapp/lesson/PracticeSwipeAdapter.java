package com.frizzl.app.frizzleapp.lesson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class PracticeSwipeAdapter extends FragmentStatePagerAdapter {

    private Practice currentPractice;
    private int numOfSlides;

    public PracticeSwipeAdapter(FragmentManager fm, Practice currentPractice) {
        super(fm);
        this.currentPractice = currentPractice;
        numOfSlides = currentPractice.getNumOfSlides()+1;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        // Last slide
        if (position == numOfSlides-1){
            fragment = new PracticeLastSlideFragment();
        }
        else {
            fragment = new PracticeSlideFragment();
            bundle.putSerializable("practice_slide", currentPractice.getPracticeSlides().get(position));
        }
        bundle.putInt("lesson", currentPractice.getID());
        bundle.putInt("index", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfSlides;
    }
}