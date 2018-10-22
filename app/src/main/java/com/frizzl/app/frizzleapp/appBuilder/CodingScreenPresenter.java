package com.frizzl.app.frizzleapp.appBuilder;


import android.support.v4.app.Fragment;

import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;


/**
 * A simple {@link Fragment} subclass.
 */
public class CodingScreenPresenter {

    private CodingScreenFragment codingScreenFragment;

    public CodingScreenPresenter(CodingScreenFragment codingScreenFragment) {
        this.codingScreenFragment = codingScreenFragment;
    }

    public void getAndPresentCode() {
        String currentCode = UserProfile.user.getCurrentUserApp().getCode();
        if (currentCode.equals("")) {
            codingScreenFragment.showEmptyCode();
        } else {
            codingScreenFragment.showCode(currentCode);
        }
    }

    public void saveState() {
        String currentCode = codingScreenFragment.getCode();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setCode(currentCode);
        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
    }

    public void onPause() {
        saveState();
    }
}
