package com.example.yasmindunsky.frizzleapp.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.yasmindunsky.frizzleapp.R;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class OnboardingSwipeAdapter extends FragmentPagerAdapter {
    private int numOfFragments;

    public OnboardingSwipeAdapter(FragmentManager fm) {
        super(fm);
        numOfFragments = 4;
    }

    @Override
    public Fragment getItem(int position) {
        OnboardingFragment onboardingFragment = new OnboardingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        onboardingFragment.setArguments(bundle);
        return onboardingFragment;
    }

    @Override
    public int getCount() {
        return numOfFragments;
    }
}

