package com.frizzl.app.frizzleapp.appBuilder;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.UpdatePositionInServer;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

public class AppBuilderPresenter {

    private AppBuilderActivity appBuilderActivity;
    private String codeStart;
    private String codeEnd;
    private int currentAppID;

    final private static int WRITE_PERMISSION = 1;
    final private static int MAX_NICKNAME_LENGTH = 10;

    private FirebaseAnalytics mFirebaseAnalytics;

    public AppBuilderPresenter(AppBuilderActivity appBuilderActivity, String codeStart, String codeEnd, int currentAppID){
        this.appBuilderActivity = appBuilderActivity;
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
        this.currentAppID = currentAppID;
    }

    public int updateCurrentAndTopPosition() {
        int nextLesson = UserProfile.user.getCurrentLessonID() + 1;
        if (nextLesson <= 13) {
            UserProfile.user.setCurrentLessonID(nextLesson);
            if (nextLesson > UserProfile.user.getTopLessonID()) {
                UserProfile.user.setTopLessonID(nextLesson);
                new UpdatePositionInServer().execute();
            }
        }
        return nextLesson;
    }

    private void updateUserProfileFromActivity() {
        Map<Integer, UserCreatedView> views = appBuilderActivity.getViews();
        UserProfile.user.setViews(views);
        String xml = appBuilderActivity.getXml();
        UserProfile.user.setXml(xml);
    }

    public void saveProject() {
        updateUserProfileFromActivity();
        new SaveProjectToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
            }
        }).execute(codeStart, codeEnd);
    }

    public void downloadApk() {
        new DownloadApkFromServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                appBuilderActivity.startApk();
            }
        }).execute(UserProfile.user.getXml(), UserProfile.user.getJava());
    }

    public void compileAndDownloadApp() {
        saveProject();

        // send java and xml to server for build
        // if succeeded ask user for writing permission and download the apk
        new SaveProjectToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if (output.contains("BUILD SUCCESSFUL")) {
                    appBuilderActivity.getWritePermission();
                    appBuilderActivity.hideError();
                } else {
                    // Build didn't work.
                    appBuilderActivity.displayError(output);
                }
            }
        }).execute(codeStart, codeEnd);

    }

    public void onResume() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        if (currentUserApp == null){
            currentUserApp = new UserApp(currentAppID);
            UserProfile.user.setCurrentUserApp(currentUserApp);
            appBuilderActivity.openStartAppPopup();
        }
    }

    public void setAppNameAndIcon(String appName, String iconDrawable) {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setName(appName);
        currentUserApp.setIcon(iconDrawable);
        UserProfile.user.setCurrentUserApp(currentUserApp);
        appBuilderActivity.updateAppNameAndIcon(appName, iconDrawable);
    }
}
