package com.frizzl.app.frizzleapp.map;

import android.view.View;
import android.widget.Button;

import com.frizzl.app.frizzleapp.UserProfile;

/**
 * Created by Noga on 14/06/2018.
 */

public class MapPresenter {
    private MapActivity mapActivity;

    public MapPresenter(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    public void onClickedApp(View view){
        mapActivity.goToApp();
    }

    public void onClickedPractice(View view){

    }

    public int getCurrentLevel() {
        return UserProfile.user.getCurrentLevel();
    }
}
