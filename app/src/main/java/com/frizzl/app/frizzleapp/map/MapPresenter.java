package com.frizzl.app.frizzleapp.map;

import com.frizzl.app.frizzleapp.UserProfile;

/**
 * Created by Noga on 14/06/2018.
 */

public class MapPresenter {
    private MapActivity mapActivity;

    public MapPresenter(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    public void onClickedApp(int appID){
        int level = (appID == 0) ? 0 : 3; // TODO change
        UserProfile.user.setCurrentLevel(level);
        mapActivity.goToApp(appID);
    }

    public void onClickedPractice(int practiceID){
        UserProfile.user.setCurrentLevel(practiceID);
        mapActivity.goToPractice(practiceID);

    }

    public int getCurrentLevel() {
        return UserProfile.user.getCurrentLevel();
    }

    public int getTopLevel() {
        return UserProfile.user.getTopLevel();
    }
}
