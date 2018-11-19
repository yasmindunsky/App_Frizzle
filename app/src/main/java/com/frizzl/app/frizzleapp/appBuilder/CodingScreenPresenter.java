package com.frizzl.app.frizzleapp.appBuilder;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.ContentUtils;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 */
class CodingScreenPresenter {

    private final CodingScreenFragment codingScreenFragment;
    private DefinedFunctionsViewModel definedFunctionsViewModel;

    CodingScreenPresenter(CodingScreenFragment codingScreenFragment) {
        this.codingScreenFragment = codingScreenFragment;
    }

    void getAndPresentCode() {
        String currentCode = UserProfile.user.getCurrentUserApp().getCode();
        if (currentCode.equals("")) {
            codingScreenFragment.showEmptyCode();
        } else {
            codingScreenFragment.showCode(currentCode);
        }
    }

    void saveState() {
        String currentCode = codingScreenFragment.getCode();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setCode(currentCode);
        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
    }

    void onPause() {
        saveState();
        extractDefinedFunctionsAndUpdateViewModel();
    }

    private void extractDefinedFunctionsAndUpdateViewModel() {
        String code = codingScreenFragment.getCode();
        definedFunctionsViewModel.clearFunctions();

        String functionIdentification = "public void";
        int index = code.indexOf(functionIdentification);
        while (index >= 0) {
            String substring = code.substring(index, code.length());
            int functionNameEnd = substring.indexOf("(");
            if (functionNameEnd > 0) {
                String function = code.substring(
                        index + functionIdentification.length() + 1,
                        index + functionNameEnd);
                definedFunctionsViewModel.addFunction(function.trim());
            }
            index = code.indexOf(functionIdentification, index + 1);
        }

    }

    void onResume() {
        FragmentActivity activity = codingScreenFragment.getActivity();
        definedFunctionsViewModel =
                ViewModelProviders.of(activity).get(DefinedFunctionsViewModel.class);
    }

    boolean isTaskCompleted(String code) {
        boolean taskCompleted = false;
        UserProfile user = UserProfile.user;
        int currentAppTaskNum = user.getCurrentAppTaskNum();
        if (user.getCurrentLevel() == ContentUtils.POLLY_APP_LEVEL_ID) {
            if (currentAppTaskNum == 2) {
                int beforeName = code.indexOf("public void") + String.valueOf("public void").length();
                int afterName = code.indexOf("(View view)");
                if (afterName - beforeName > 2) {
                    taskCompleted = true;
                }
                taskCompleted &= !CodeCheckUtils.checkIfContainsFunctionWithName(code, "nameYouChoose");
            } else if (currentAppTaskNum == 3) {
                int beforeTextToSay = code.indexOf("speakOut\"") + String.valueOf("speakOut\"").length();
                int afterTextToSay = code.indexOf("\");");
                if (afterTextToSay - beforeTextToSay > 0){
                    taskCompleted = true;
                }
                taskCompleted &= !CodeCheckUtils.checkIfSpeakoutIsEmpty(code);
            }
        }
        return taskCompleted;
    }
}
