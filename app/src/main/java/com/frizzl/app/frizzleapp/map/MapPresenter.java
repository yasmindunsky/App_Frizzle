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

    public void onClickedApp(int levelID){
//        int level = (appID == 0) ? 0 : 3; // TODO change
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToApp(levelID);
    }

    public void onClickedPractice(int levelID){
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToPractice(levelID);

    }

    public int getCurrentLevel() {
        return UserProfile.user.getCurrentLevel();
    }

    public int getTopLevel() {
        return UserProfile.user.getTopLevel();
    }
}
