package com.frizzl.app.frizzleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.frizzl.app.frizzleapp.lesson.AppTasks;
import com.frizzl.app.frizzleapp.lesson.TaskFragment;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class AppTasksSwipeAdapter extends FragmentStatePagerAdapter {

    private AppTasks currentApp;
    private int numOfFragments;

    public AppTasksSwipeAdapter(FragmentManager fm, AppTasks currentApp) {
        super(fm);
        this.currentApp = currentApp;
        numOfFragments = currentApp.getTasksNum();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.getInt("position", position);

        bundle.putInt("task", currentApp.getID());
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