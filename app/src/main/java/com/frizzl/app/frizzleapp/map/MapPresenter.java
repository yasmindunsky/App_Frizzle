package com.frizzl.app.frizzleapp.map;

import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.ContentUtils;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.practice.AppContentParser;
import com.frizzl.app.frizzleapp.practice.AppTasks;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by Noga on 14/06/2018.
 */

class MapPresenter {
    private final MapActivity mapActivity;

    MapPresenter(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    void onClickedApp(int levelID){
        UserProfile.user.storeSerializedObject(mapActivity);
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToApp(levelID);
    }

    void onClickedPractice(int levelID){
        UserProfile.user.storeSerializedObject(mapActivity);
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToPractice(levelID);
    }

    public int getCurrentLevel() {
        return UserProfile.user.getCurrentLevel();
    }

    int getTopLevel() {
        return UserProfile.user.getTopLevel();
    }

    PopupWindow getPrePracticePopup(Runnable startPractice) {
        String explanationText = "";
        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == ContentUtils.ONCLICK_PRACTICE_LEVEL_ID){
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.onclick_practice_explanation_text);
        } else if (currentLevel == ContentUtils.SPEAKOUT_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.speakout_practice_explanation_text);
        } else if (currentLevel == ContentUtils.FIRST_PRACTICE_LEVEL_ID) {
            explanationText = mapActivity.getApplicationContext().getResources().getString(R.string.first_practice_explanation_text);
        }
        return new PrePracticePopupWindow(mapActivity, explanationText, startPractice);
    }

    void onClickedIntro(int levelID) {
        UserProfile.user.setCurrentLevel(levelID);
        mapActivity.goToIntro(levelID);
    }

    public void parseAppAndUpdateUserProfile(int levelID) {
        AppContentParser appContentParser;
        try {
            appContentParser = new AppContentParser();
            AppTasks appTasks = appContentParser.parseAppXml(mapActivity, levelID);
            UserProfile.user.setCurrentAppTasks(appTasks);
            UserProfile.user.setCurrentLevel(levelID);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
