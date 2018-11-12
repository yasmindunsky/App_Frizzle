package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;

import java.io.File;

public class AppBuilderPresenter {
    private static final int INSTALLED_APP_ABOVE_N = 1;
    private static final int INSTALLED_APP_BELOW_N = 2;

    private AppBuilderActivity appBuilderActivity;
    private String codeStart;
    private String codeEnd;
    private int currentAppLevelID;
    private boolean justOpenedApp;

    AppBuilderPresenter(AppBuilderActivity appBuilderActivity,
                               String codeStart, String codeEnd, int currentAppLevelID){
        this.appBuilderActivity = appBuilderActivity;
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

    void saveProject() {
        // update user profile from activity
        String xml = appBuilderActivity.getXml();
        String code = appBuilderActivity.getCode();
        String manifest = appBuilderActivity.getManifest();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
//        currentUserApp.setViews(designFragment.getViewsFromModel(), designFragment.getNumOfButtons(),
//                designFragment.getNumOfTextViews(),  designFragment.getNumOfImageViews(),
//                designFragment.getNextViewIndex());
        currentUserApp.setXml(xml);
        currentUserApp.setCode(code);
        currentUserApp.setManifest(manifest);
        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
        UserProfile.user.storeSerializedObject(appBuilderActivity.getBaseContext());
    }

    void downloadApk() {
        appBuilderActivity.showLoaderAnimation(true);
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
            installUsersApp();
        }).execute(code, xml, manifest);
    }

    private void installUsersApp() {
        String destination =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/frizzl_project.apk";
        File apkFile = new File(destination);
        Log.e("INSTALL", "apkFile length: " + apkFile.length() / 1024);
        Context context = appBuilderActivity.getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context,
                    "com.frizzl.app.frizzlapp.fileprovider", apkFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!appBuilderActivity.getPackageManager().canRequestPackageInstalls()) {
                    appBuilderActivity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                            .setData(Uri.parse(String.format("package:%s", appBuilderActivity.getPackageName()))), 1234);
                }
            }
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE)
                    .setData(contentUri)
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .putExtra(Intent.EXTRA_RETURN_RESULT, true)
                    .putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            appBuilderActivity.startActivityForResult(intent, INSTALLED_APP_ABOVE_N);
            appBuilderActivity.showLoaderAnimation(false);
        } else {
            Uri contentUri = Uri.fromFile(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(contentUri, "application/vnd.android.package-archive")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Intent.EXTRA_RETURN_RESULT, true);
            appBuilderActivity.startActivityForResult(intent, INSTALLED_APP_BELOW_N);
        }
    }

    void onResume() {
        if (justOpenedApp) {
            appBuilderActivity.openStartAppPopup();
            justOpenedApp = false;
        }
    }

    void setAppNameAndIcon(String appName, String icon) {
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
