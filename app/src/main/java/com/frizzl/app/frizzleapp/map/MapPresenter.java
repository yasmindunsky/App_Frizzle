package com.frizzl.app.frizzleapp.map;

import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
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

    public PopupWindow getPrePracticePopup() {
        String explanationText = "";
        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == Support.ONCLICK_PRACTICE_LEVEL_ID){
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.onclick_practice_explanation_text);
        } else if (currentLevel == Support.SPEAKOUT_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.speakout_practice_explanation_text);
        } else if (currentLevel == Support.FIRST_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.first_practice_explanation_text);
        }
        return new PrePracticePopupWindow(mapActivity, explanationText);
    }
}
