package com.frizzl.app.frizzleapp;

import android.view.View;
import android.widget.Button;

/**
 * Created by Noga on 14/06/2018.
 */

public class MapPresenter {
    private MapActivity mapActivity;

    public MapPresenter(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    public void onClickedLesson(View view){
        Button button = (Button)view;
        int lessonID = (int) button.getTag();

        if (UserProfile.user.isLessonOpen(lessonID)) {
            UserProfile.user.setCurrentLessonID(lessonID);
            mapActivity.navigateToLesson();
        }
    }
}
