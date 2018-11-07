package com.frizzl.app.frizzleapp.map;

import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Utils;
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

    public PopupWindow getPrePracticePopup(Runnable startPractice) {
        String explanationText = "";
        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == Utils.ONCLICK_PRACTICE_LEVEL_ID){
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.onclick_practice_explanation_text);
        } else if (currentLevel == Utils.SPEAKOUT_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.speakout_practice_explanation_text);
        } else if (currentLevel == Utils.FIRST_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.first_practice_explanation_text);
        }
        return new PrePracticePopupWindow(mapActivity, explanationText, startPractice);
    }

    public void onClickedIntro(int levelID) {
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToIntro(levelID);
    }
}
