package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.frizzl.app.frizzleapp.AnalyticsUtils;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.SwipeDirection;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.ViewUtils;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.tooltip.OnDismissListener;

import net.cachapa.expandablelayout.ExpandableLayout;

public class AppBuilderActivity extends AppCompatActivity {
    private static final int INSTALLED_APP_ABOVE_N = 1;
    private static final int INSTALLED_APP_BELOW_N = 2;
    private AppBuilderPresenter appBuilderPresenter;
    private DesignScreenPresenter designScreenPresenter;
    private CodingScreenPresenter codingScreenPresenter;
    private DesignScreenFragment designFragment;
    private CodingScreenFragment codingFragment;

    private PopupWindow startAppPopupWindow;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private ExpandableLayout errorExpandableLayout;
    private ImageButton playButton;
    private Button moveOnButton;
    private ir.neo.stepbarview.StepBarView stepBarView;
    private com.airbnb.lottie.LottieAnimationView checkMark;
    private PopupWindow downloadingAppPopupWindow;
    private Tutorial tutorial;

    final private static int WRITE_PERMISSION = 1;

    private TaskViewPager viewPager;
    private int currentAppLevelID;
    private static boolean showMovedOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentAppLevelID = intent.getIntExtra("appLevelID", 0);
        showMovedOn = intent.getBooleanExtra("returnedFromUsersApp", false);

        RelativeLayout mainLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.activity_app_builder, null);
        setContentView(mainLayout);

        errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
        ImageButton clickToExpandError = findViewById(R.id.clickToExpandError);
        relativeLayout = findViewById(R.id.appBuilderLayout);
        progressBar = findViewById(R.id.progressBar);
        playButton = findViewById(R.id.play);
        moveOnButton = findViewById(R.id.moveOnButton);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.builderToolbar);
        stepBarView = findViewById(R.id.stepBarView);
        checkMark = findViewById(R.id.checkMark);

        designFragment = new DesignScreenFragment();
        designScreenPresenter = new DesignScreenPresenter(designFragment);
        designFragment.setPresenter(designScreenPresenter);
        codingFragment = new CodingScreenFragment();
        codingScreenPresenter = new CodingScreenPresenter(codingFragment);
        codingFragment.setPresenter(codingScreenPresenter);

        checkMark.setSpeed(1f);
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
                }, 250);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        appBuilderPresenter = new AppBuilderPresenter(this,
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
        relativeLayout.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.shade, null));
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
        AppTasksSwipeAdapter swipeAdapter = new AppTasksSwipeAdapter(getSupportFragmentManager(), UserProfile.user.getCurrentAppTasks());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                stepBarView.setReachedStep(stepBarView.getMaxCount() - position);

                SwipeDirection swipeDirection;
                if (position < UserProfile.user.getCurrentAppTaskNum()) {
                    swipeDirection = SwipeDirection.all;
                } else {
                    swipeDirection = SwipeDirection.left; // This is the setting also for RTL
                }
                viewPager.setAllowedSwipeDirection(swipeDirection);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


//         Rotation for RTL swiping.
        if (ViewUtils.isRTL()) {
            viewPager.setRotationY(180);
        }

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
        stepBarView.setRtl(!ViewUtils.isRTL());
        int tasksNum = UserProfile.user.getCurrentAppTasks().getTasksNum();
        stepBarView.setMaxCount(tasksNum);
        stepBarView.setReachedStep(tasksNum);
        stepBarView.setAllowTouchStepTo(0);

        int currentTask = UserProfile.user.getCurrentAppTaskNum();
        if (currentTask != 0) {
            viewPager.setCurrentItem(currentTask);
            stepBarView.setReachedStep(stepBarView.getMaxCount() - currentTask);
        }

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_installing, null);
        downloadingAppPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        tutorial = new Tutorial(AppBuilderActivity.this);
        boolean activityCreated = true;
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

    public void presentPopup(PopupWindow popupWindow, Runnable runOnDismiss) {
        ViewUtils.presentPopup(popupWindow, runOnDismiss, relativeLayout, relativeLayout, this);
    }

    private Runnable afterSuccessPopupClosed() {
        return () -> {
            tutorial.presentTooltip(playButton, getString(R.string.tooltip_install_app), null, Gravity.BOTTOM);
        };
    }

    private void openTaskSuccessPopup() {
        PopupWindow successPopupWindow = new TaskSuccessPopupWindow(AppBuilderActivity.this);
        ViewUtils.presentPopup(successPopupWindow, afterSuccessPopupClosed(), relativeLayout, relativeLayout, this);
    }

    private void openRequestPermissionPopup() {
        Runnable requestPermission = () -> {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        };
        PopupWindow permissionPopupWindow = new RequestPermissionPopupWindow(AppBuilderActivity.this, requestPermission);
        ViewUtils.presentPopup(permissionPopupWindow, null, relativeLayout, relativeLayout, this);
    }

    public void openStartAppPopup() {
        relativeLayout.post(() -> ViewUtils.presentPopup(startAppPopupWindow, null, relativeLayout, relativeLayout, this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, download APK from server.
                    appBuilderPresenter.downloadApk();
                }
            }
        }
    }

    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INSTALLED_APP_ABOVE_N:
                Log.d("INSTALL", "resultCode: " + resultCode);
                if (resultCode == RESULT_OK) {
                    Log.d("INSTALL", "Package Installation Success.");
                    startUsersApp();
                } else if (resultCode == RESULT_FIRST_USER) {
                    Log.d("INSTALL", "RESULT_FIRST_USER, Installation Failed");
                } else if (resultCode == RESULT_CANCELED) {
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

    public void taskCompleted() {
        // Will move to next Task when animation finishes.
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            checkMark.setVisibility(View.VISIBLE);
            checkMark.playAnimation();
        }, 1000); // 1s
        UserProfile user = UserProfile.user;
        int currentTaskNum = user.getCurrentAppTaskNum();
        int currentLevel = user.getCurrentLevel();
        AnalyticsUtils.logCompletedTaskEvent(currentLevel, currentTaskNum);
        // If not the last task
        if (currentTaskNum < user.getCurrentAppTasks().getTasksNum() - 1) {
            UserProfile.user.setCurrentAppTaskNum(currentTaskNum + 1);
        } else {
            handler.postDelayed(this::openTaskSuccessPopup, 1000); // 1s
        }
    }

    public String getCode() {
        return codingFragment.getCode();
    }

    public String getManifest() {
        return designFragment.getManifest();
    }

    public String getXml() {
        return designFragment.getXml();
    }

    private void moveToNextTask() {
//        stepBarView.setReachedStep(stepBarView.getReachedStep()-1);
        viewPager.setCurrentItem(getItem(1), true);
    }

    private void moveToPrevTask() {
        viewPager.setCurrentItem(getItem(-1), true);
    }

    public void showLoaderAnimation(boolean show) {
        if (show)
            ViewUtils.presentPopup(downloadingAppPopupWindow,
                    null, relativeLayout, relativeLayout, this);
        else if (downloadingAppPopupWindow != null)
            downloadingAppPopupWindow.dismiss();
    }

    public void presentConnectionNeededPopup() {
        showLoaderAnimation(false);
        PopupWindow internetRequiredPopupWindow = new InternetRequiredPopupWindow(AppBuilderActivity.this);
        ViewUtils.presentPopup(internetRequiredPopupWindow, null, relativeLayout, relativeLayout, this);
    }
}
