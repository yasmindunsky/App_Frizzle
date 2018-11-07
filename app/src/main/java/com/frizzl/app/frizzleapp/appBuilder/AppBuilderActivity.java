package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.AnalyticsUtils;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Utils;
import com.frizzl.app.frizzleapp.SwipeDirection;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.tooltip.OnDismissListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;

public class AppBuilderActivity extends AppCompatActivity {
    private static final int INSTALLED_APP_ABOVE_N = 1;
    private static final int INSTALLED_APP_BELOW_N = 2;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    private AppBuilderPresenter appBuilderPresenter;
    private DesignScreenPresenter designScreenPresenter;
    private CodingScreenPresenter codingScreenPresenter;
    private DesignScreenFragment designFragment;
    private CodingScreenFragment codingFragment;

    private PopupWindow startAppPopupWindow;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private ExpandableLayout errorExpandableLayout;
    private TabLayout tabLayout;
    private ImageButton playButton;
    private Button moveOnButton;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton clickToExpandError;
    private ir.neo.stepbarview.StepBarView stepBarView;
    private com.airbnb.lottie.LottieAnimationView checkMark;

    private Tutorial tutorial;

    final private static int WRITE_PERMISSION = 1;
    private static final int MAX_NICKNAME_LENGTH = 10;

    private boolean activityCreated = false;
    private TaskViewPager viewPager;
    private AppTasksSwipeAdapter swipeAdapter;
    private int currentAppLevelID;
    private static boolean showMovedOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentAppLevelID = getIntent().getIntExtra("appLevelID", 0);

        RelativeLayout mainLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.activity_app_builder, null);
        setContentView(mainLayout);

        errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
        clickToExpandError = findViewById(R.id.clickToExpandError);
        relativeLayout = findViewById(R.id.appBuilderLayout);
        progressBar = findViewById(R.id.progressBar);
        playButton = findViewById(R.id.play);
        moveOnButton = findViewById(R.id.moveOnButton);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.builderToolbar);
        stepBarView = findViewById(R.id.stepBarView);
        checkMark = findViewById(R.id.checkMark);

        designFragment = new DesignScreenFragment();
        designScreenPresenter = new DesignScreenPresenter(designFragment);
        designFragment.setPresenter(designScreenPresenter);
        codingFragment = new CodingScreenFragment();
        codingScreenPresenter = new CodingScreenPresenter(codingFragment);
        codingFragment.setPresenter(codingScreenPresenter);

        checkMark.setSpeed(0.8f);
        checkMark.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    moveToNextTask();
                    checkMark.setVisibility(View.INVISIBLE);
                }, 500); // 1s
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        appBuilderPresenter = new AppBuilderPresenter(this,
                codingScreenPresenter, designScreenPresenter,
                getApplicationContext().getResources().getString(R.string.code_start),
                getApplicationContext().getResources().getString(R.string.code_end), currentAppLevelID);
        appBuilderPresenter.onCreate();

        startAppPopupWindow = new StartAppPopupWindow(this);

        moveOnButton.setVisibility(showMovedOn ? View.VISIBLE : View.INVISIBLE);
        moveOnButton.setOnClickListener(v -> {
            UserProfile.user.finishedApp(currentAppLevelID);
            showMovedOn = false;
            onBackPressed();
        });

        // Undim
        relativeLayout.setForeground(getResources().getDrawable(R.drawable.shade));
        relativeLayout.getForeground().setAlpha(0);

        toolbar.setNavigationOnClickListener(v -> {
            // save user project in profile and server
            appBuilderPresenter.saveProject();

            // Go to map home screen
            Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
            startActivity(mapIntent);
        });

        // Error ExpandableLayout
        clickToExpandError.setOnClickListener(v -> {
            if (errorExpandableLayout.isExpanded()) {
                errorExpandableLayout.collapse();
            } else {
                errorExpandableLayout.expand();
            }
        });

        // Set Task text.
        // Create SwipeAdapter.
        viewPager = findViewById(R.id.viewPager);
        swipeAdapter = new AppTasksSwipeAdapter(getSupportFragmentManager(), UserProfile.user.getCurrentAppTasks());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                stepBarView.setReachedStep(stepBarView.getMaxCount()-position);

                SwipeDirection swipeDirection;
                if (position < UserProfile.user.getCurrentAppTaskNum()) {
                    swipeDirection = SwipeDirection.all;
                }
                else {
                    swipeDirection = SwipeDirection.left; // This is the setting also for RTL
                }
                viewPager.setAllowedSwipeDirection(swipeDirection);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

//         Rotation for RTL swiping.
        if (Utils.isRTL()) {
            viewPager.setRotationY(180);
        }

//        // Connecting TabLayout with ViewPager to show swipe position in dots.
//        final TabLayout tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager, true);

        final TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.design);
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.code);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                if (tab.getPosition() == 0) {
                    fragment = designFragment;
                } else {
                    fragment = codingFragment;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentFrame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.addTab(graphicEditTab, true);
        tabLayout.addTab(codingTab);
        graphicEditTab.select();

        // Set stepBarView
        stepBarView.setRtl(!Utils.isRTL());
        int tasksNum = UserProfile.user.getCurrentAppTasks().getTasksNum();
        stepBarView.setMaxCount(tasksNum);
        stepBarView.setReachedStep(tasksNum);
        stepBarView.setAllowTouchStepTo(0);

        tutorial = new Tutorial(AppBuilderActivity.this);
        activityCreated = true;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBuilderPresenter.onResume();
    }

    public void onPlay(final View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission from user still isn't granted, ask for permission.
            openRequestPermissionPopup();


        }

        // Permission was already granted, download APK.
        else {
            appBuilderPresenter.downloadApk();
        }

        AnalyticsUtils.logRunAppEvent();
        designScreenPresenter.saveState();
        codingScreenPresenter.saveState();
    }

    public void presentPopup(PopupWindow popupWindow, Runnable runOnDismiss){
        Utils.presentPopup(popupWindow, runOnDismiss, relativeLayout, relativeLayout, this);
    }

    private Runnable afterSuccessPopupClosed (){
        return () -> {
            tutorial.presentTooltip(playButton, getString(R.string.tooltip_install_app), null, Gravity.BOTTOM);
            showMovedOn = true;
            moveOnButton.setVisibility(View.VISIBLE);
        };
    }

    private void openTaskSuccessPopup() {
        PopupWindow successPopupWindow = new TaskSuccessPopupWindow(AppBuilderActivity.this);
        Utils.presentPopup(successPopupWindow, afterSuccessPopupClosed(), relativeLayout, relativeLayout, this);
    }

    private void openRequestPermissionPopup() {
        Runnable requestPermission = () -> {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            setProgressBarVisibility(View.VISIBLE);
        };
        PopupWindow permissionPopupWindow = new RequestPermissionPopupWindow(AppBuilderActivity.this, requestPermission);
        Utils.presentPopup(permissionPopupWindow, null, relativeLayout, relativeLayout, this);
    }

    public void openStartAppPopup() {
        relativeLayout.post(()-> presentPopup(startAppPopupWindow, null));
    }

    private void undimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(0);
    }

    private void dimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(220);
    }

    public void getWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission from user still isn't granted, ask for permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }

        // Permission was already granted, download APK.
        else {
            appBuilderPresenter.downloadApk();
        }
    }



    public void hideError() {
        LinearLayout errorLayout = findViewById(R.id.errorDisplay);
        errorLayout.setVisibility(View.GONE);
    }

    public void displayError(String output) {
        TextView error = findViewById(R.id.error);
        error.setText(output);
        LinearLayout errorLayout = findViewById(R.id.errorDisplay);
        errorLayout.setVisibility(View.VISIBLE);
        errorExpandableLayout.expand();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, download APK from server.
                    appBuilderPresenter.downloadApk();
                } else {
                    // Permission denied.
                }
            }
        }
    }

    private String getXml() {
        return designFragment.getXml();
    }

    private void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    public void installUsersApp() {
        setProgressBarVisibility(View.GONE);


        String destination =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/frizzl_project.apk";
        File apkFile = new File(destination);
        Log.e("INSTALL", "apkFile length: " + apkFile.length() / 1024);
        Context context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context,
                    "com.frizzl.app.frizzlapp.fileprovider", apkFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!getPackageManager().canRequestPackageInstalls()) {
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                            .setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
                }
            }
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE)
                    .setData(contentUri)
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .putExtra(Intent.EXTRA_RETURN_RESULT, true)
                    .putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            startActivityForResult(intent, INSTALLED_APP_ABOVE_N);
        } else {
            Uri contentUri = Uri.fromFile(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(contentUri, "application/vnd.android.package-archive")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Intent.EXTRA_RETURN_RESULT, true);
            startActivityForResult(intent, INSTALLED_APP_BELOW_N);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case INSTALLED_APP_ABOVE_N:
                Log.d("INSTALL", "resultCode: " + resultCode);
                if(resultCode == RESULT_OK){
                    Log.d("INSTALL", "Package Installation Success.");
                    startUsersApp();
                } else if(resultCode == RESULT_FIRST_USER){
                    Log.d("INSTALL", "RESULT_FIRST_USER, Installation Failed");
                } else if (resultCode == RESULT_CANCELED){
                    Log.d("INSTALL", "Installation Cancelled.");
                }
                break;
            case INSTALLED_APP_BELOW_N:
                startUsersApp();
        }
    }

    private void startUsersApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final ComponentName cn = new ComponentName("com.frizzl.frizzlproject3", "com.frizzl.frizzlproject3.MainActivity");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onStartButtonFromStartAppPopup(String appName, String iconDrawable) {
        appBuilderPresenter.setAppNameAndIcon(appName, iconDrawable);
        if (currentAppLevelID == 0) {
            presentNextTutorialMessage();
        }
    }

    private void presentNextTutorialMessage() {
        OnDismissListener listener = () -> designFragment.presentTutorialMessage();
        tutorial.presentTooltip(viewPager, getString(R.string.tooltip_see_task), listener, Gravity.BOTTOM);
    }

    public void updateAppNameAndIcon(String appName, String iconDrawable) {
        designFragment.setAppName(appName);
        designFragment.setAppIcon(iconDrawable);
    }

    private String getCode() {
        return codingFragment.getCode();
    }

    public void taskCompleted() {
        // Will mive to next Task when animation finishes.
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            checkMark.setVisibility(View.VISIBLE);
            checkMark.playAnimation();
        }, 1000); // 1s
        int currentTaskNum = UserProfile.user.getCurrentAppTaskNum();
        int currentLevel = UserProfile.user.getCurrentLevel();
        AnalyticsUtils.logCompletedTaskEvent(currentLevel, currentTaskNum);
        // If not the last task
        if (currentTaskNum < UserProfile.user.getCurrentAppTasks().getTasksNum() - 1){
//            moveToNextTask();
            UserProfile.user.setCurrentAppTaskNum(currentTaskNum + 1);
        }
        else {
            openTaskSuccessPopup();
        }
    }

    public void saveProject() {
        // update user profile from activity
        String xml = getXml();
        String code = getCode();
        String manifest = getManifest();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
//        currentUserApp.setViews(designFragment.getViewsFromModel(), designFragment.getNumOfButtons(),
//                designFragment.getNumOfTextViews(),  designFragment.getNumOfImageViews(),
//                designFragment.getNextViewIndex());
        currentUserApp.setXml(xml);
        currentUserApp.setCode(code);
        currentUserApp.setManifest(manifest);
        UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
    }

    private String getManifest() {
        return designFragment.getManifest();
    }

    private void moveToNextTask(){
//        stepBarView.setReachedStep(stepBarView.getReachedStep()-1);
        viewPager.setCurrentItem(getItem(1), true);
    }

    private void moveToPrevTask(){
        viewPager.setCurrentItem(getItem(-1),true);
    }
}
