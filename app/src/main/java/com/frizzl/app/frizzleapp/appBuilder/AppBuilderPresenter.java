package com.frizzl.app.frizzleapp.appBuilder;

import android.util.Log;

import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;

public class AppBuilderPresenter {

    private AppBuilderActivity appBuilderActivity;
    private String codeStart;
    private String codeEnd;
    private int currentAppLevelID;
    private boolean justOpenedApp;

    public AppBuilderPresenter(AppBuilderActivity appBuilderActivity, CodingScreenPresenter codingScreenPresenter,
                               DesignScreenPresenter designScreenPresenter,
                               String codeStart, String codeEnd, int currentAppLevelID){
        this.appBuilderActivity = appBuilderActivity;
        CodingScreenPresenter codingScreenPresenter1 = codingScreenPresenter;
        DesignScreenPresenter designScreenPresenter1 = designScreenPresenter;
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
        this.currentAppLevelID = currentAppLevelID;
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
//        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
//    }

    public void saveProject() {
        appBuilderActivity.saveProject();
        UserProfile.user.storeSerializedObject(appBuilderActivity.getBaseContext());
    }

    public void downloadApk() {
        saveProject();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String code = codeStart + currentUserApp.getCode() + codeEnd;
        String xml = currentUserApp.getXml();
        String manifest = currentUserApp.getManifest();

        Log.d("INSTALL", "in AppBuilderPresenter, downloadApk()");
        new DownloadApkFromServer(output -> {
            if (output == null || output.equals("")){
                output = "no output from server";
            }
            Log.d("INSTALL", "in AppBuilderPresenter, downloadApk(), output:" + output);
            appBuilderActivity.installUsersApp();
        }).execute(code, xml, manifest);
    }

    public void compileAndDownloadApp() {
        appBuilderActivity.getWritePermission();
        appBuilderActivity.hideError();
    }

    public void onResume() {
        if (justOpenedApp) {
            appBuilderActivity.openStartAppPopup();
            justOpenedApp = false;
        }
    }

    public void setAppNameAndIcon(String appName, String icon) {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setName(appName);
        currentUserApp.setIcon(icon);
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        String manifest = layoutXmlWriter.writeManifest(icon, appName);
        currentUserApp.setManifest(manifest);
        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
        appBuilderActivity.updateAppNameAndIcon(appName, icon);
    }

    public void onCreate() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        if (currentUserApp == null){
            currentUserApp = new UserApp(currentAppLevelID);
            UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
            justOpenedApp = true;
        }
    }
}
