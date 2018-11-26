package com.frizzl.app.frizzleapp.appBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.frizzl.app.frizzleapp.practice.AppTasks;
import com.frizzl.app.frizzleapp.practice.TaskFragment;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

class AppTasksSwipeAdapter extends FragmentStatePagerAdapter {

    private final AppTasks appTasks;
    private final int numOfFragments;

    public AppTasksSwipeAdapter(FragmentManager fm, AppTasks appTasks) {
        super(fm);
        this.appTasks = appTasks;
        numOfFragments = appTasks.getNumberOfTasks();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        bundle.putInt("task", appTasks.getID());
        // set the index as argument of the new fragment
        bundle.putInt("index", position);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfFragments;
    }


}