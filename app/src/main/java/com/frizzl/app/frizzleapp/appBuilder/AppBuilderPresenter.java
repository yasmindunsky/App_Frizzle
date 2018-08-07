package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UpdatePositionInServer;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.util.Map;

public class AppBuilderPresenter {

    private AppBuilderActivity appBuilderActivity;
    private String codeStart;
    private String codeEnd;

    final private static int WRITE_PERMISSION = 1;
    final private static int MAX_NICKNAME_LENGTH = 10;

    private FirebaseAnalytics mFirebaseAnalytics;

    public AppBuilderPresenter(AppBuilderActivity appBuilderActivity, String codeStart, String codeEnd){
        this.appBuilderActivity = appBuilderActivity;
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
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
        appBuilderActivity.setProgressBarVisibility(View.VISIBLE);

        // send java and xml to server for build
        // if succeeded ask user for writing permission and download the apk
        new SaveProjectToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                appBuilderActivity.setProgressBarVisibility(View.GONE);
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
}
