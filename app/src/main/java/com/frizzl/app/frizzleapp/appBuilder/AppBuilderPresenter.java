package com.frizzl.app.frizzleapp.appBuilder;

import android.util.Log;

import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AppBuilderPresenter {

    private AppBuilderActivity appBuilderActivity;
    private CodingScreenPresenter codingScreenPresenter;
    private DesignScreenPresenter designScreenPresenter;
    private String codeStart;
    private String codeEnd;
    private int currentAppID;

    final private static int WRITE_PERMISSION = 1;
    final private static int MAX_NICKNAME_LENGTH = 10;

    private FirebaseAnalytics mFirebaseAnalytics;

    public AppBuilderPresenter(AppBuilderActivity appBuilderActivity, CodingScreenPresenter codingScreenPresenter,
                               DesignScreenPresenter designScreenPresenter,
                               String codeStart, String codeEnd, int currentAppID){
        this.appBuilderActivity = appBuilderActivity;
        this.codingScreenPresenter = codingScreenPresenter;
        this.designScreenPresenter = designScreenPresenter;
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
        this.currentAppID = currentAppID;
    }

//    private void updateUserProfileFromActivity() {
//        Map<Integer, UserCreatedView> views = appBuilderActivity.updateViewsFromUserProfileToModel();
//        String xml = appBuilderActivity.getXml();
//        String code = appBuilderActivity.getCode();
//
//        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
//        currentUserApp.setViews(userCreatedViewsModel.updateViewsFromUserProfileToModel(), userCreatedViewsModel.getNumOfButtons(),
//                userCreatedViewsModel.getNumOfTextViews(),  userCreatedViewsModel.getNumOfImageViews(),
//                userCreatedViewsModel.getNextViewIndex());
//        currentUserApp.setXml(xml);
//        currentUserApp.setCode(code);
//        UserProfile.user.setCurrentUserAppID(currentUserApp);
//    }

    public void saveProject() {
        appBuilderActivity.saveProject();
        UserProfile.user.storeSerializedObject(appBuilderActivity.getBaseContext());
    }

    public void downloadApk(String code, String xml, String manifest) {
        new DownloadApkFromServer(output -> {
            if (output == null || output.equals("")){
                output = "no output from server";
            }
            Log.d("INSTALL", "output:" + output);
            appBuilderActivity.installUsersApp();
        }).execute(codeStart + code + codeEnd, xml, manifest);
    }

    public void compileAndDownloadApp() {
        saveProject();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String code = currentUserApp.getCode();
        String xml = currentUserApp.getXml();
        String manifest = currentUserApp.getManifest();

        appBuilderActivity.getWritePermission();
        appBuilderActivity.hideError();

        downloadApk(code, xml, manifest);
//        // send java and xml to server for build
//        // if succeeded ask user for writing permission and download the apk
//        new SaveProjectToServer(new AsyncResponse() {
//            @Override
//            public void processFinish(String output) {
//                if (output.contains("BUILD SUCCESSFUL")) {
//                } else {
//                    // Build didn't work.
//                    appBuilderActivity.displayError(output);
//                }
//            }
//        }).execute(codeStart, codeEnd);

    }

    public void onResume() {
        UserProfile.user.setCurrentUserAppID(currentAppID);
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        if (currentUserApp == null){
            currentUserApp = new UserApp(currentAppID);
            UserProfile.user.setCurrentUserAppID(currentUserApp);
            appBuilderActivity.openStartAppPopup();
        }
    }

    public void setAppNameAndIcon(String appName, String icon) {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setName(appName);
        currentUserApp.setIcon(icon);
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        String manifest = layoutXmlWriter.writeManifest(icon, appName);
        currentUserApp.setManifest(manifest);
        UserProfile.user.setCurrentUserAppID(currentUserApp);
        appBuilderActivity.updateAppNameAndIcon(appName, icon);
    }
}
