package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.frizzl.app.frizzleapp.AppTasksSwipeAdapter;
import com.frizzl.app.frizzleapp.CustomViewPager;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.map.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tooltip.OnDismissListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;

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
//    private ExpandableLayout taskExpandableLayout;
    private TabLayout tabLayout;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ImageButton playButton;
    private Button moveOnButton;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton clickToExpandError;
//    private ImageButton clickToExpandTask;
//    private TextView taskTextView;
//    private AppTasks currentApp;
    private Tutorial tutorial;

    private FirebaseAnalytics mFirebaseAnalytics;

    final private static int WRITE_PERMISSION = 1;
    private static final int MAX_NICKNAME_LENGTH = 10;

    private boolean activityCreated = false;
    private CustomViewPager viewPager;
    private AppTasksSwipeAdapter swipeAdapter;
    private int currentAppID;
    private static boolean showMovedOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
        currentAppID = getIntent().getIntExtra("appID", 0);

        RelativeLayout mainLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.activity_app_builder, null);
        setContentView(mainLayout);

        designFragment = new DesignScreenFragment();
        designScreenPresenter = new DesignScreenPresenter(designFragment);
        designFragment.setPresenter(designScreenPresenter);
        codingFragment = new CodingScreenFragment();
        codingScreenPresenter = new CodingScreenPresenter(codingFragment);
        codingFragment.setPresenter(codingScreenPresenter);

        startAppPopupWindow = new StartAppPopupWindow(this);

        errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
//        taskExpandableLayout = findViewById(R.id.taskExpandableLayout);
        clickToExpandError = findViewById(R.id.clickToExpandError);
        relativeLayout = findViewById(R.id.appBuilderLayout);
        progressBar = findViewById(R.id.progressBar);
        playButton = findViewById(R.id.play);
        moveOnButton = findViewById(R.id.moveOnButton);
        nextButton = findViewById(R.id.nextTask);
        prevButton = findViewById(R.id.prevTask);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.builderToolbar);

        disableNextArrow();

        moveOnButton.setVisibility(showMovedOn ? View.VISIBLE : View.INVISIBLE);
        moveOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile.user.finishedApp(currentAppID);
                showMovedOn = false;
                onBackPressed();
            }
        });

        // Disable dim
        relativeLayout.setForeground(getResources().getDrawable(R.drawable.shade));
        relativeLayout.getForeground().setAlpha(0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save user project in profile and server
                appBuilderPresenter.saveProject();

                // Go to map home screen
                Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

        // Error ExpandableLayout
        clickToExpandError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorExpandableLayout.isExpanded()) {
                    errorExpandableLayout.collapse();
                } else {
                    errorExpandableLayout.expand();
                }
            }
        });

        // Set Task text.

        // Create SwipeAdapter.
        viewPager = findViewById(R.id.viewPager);
        swipeAdapter = new AppTasksSwipeAdapter(getSupportFragmentManager(), UserProfile.user.getCurrentAppTasks());
        viewPager.setAdapter(swipeAdapter);
        // Prevent swiping.
        viewPager.setPagingEnabled(false);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewPager.setCurrentItem(getItem(1), true);
                disableNextArrow();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(-1),true);
            }
        });
        // Rotation for RTL swiping.
//        if (Support.isRTL()) {
//            viewPager.setRotationY(180);
//        }

//        // Connecting TabLayout with ViewPager to show swipe position in dots.
//        final TabLayout tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager, true);

        final TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.design);
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.code);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
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

        appBuilderPresenter = new AppBuilderPresenter(this,
                codingScreenPresenter, designScreenPresenter,
                getApplicationContext().getResources().getString(R.string.code_start),
                getApplicationContext().getResources().getString(R.string.code_end), currentAppID);

        tutorial = new Tutorial(getApplicationContext());
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
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("RUN_APP", bundle);
        appBuilderPresenter.compileAndDownloadApp();
        setProgressBarVisibility(View.VISIBLE);
    }

    public void presentPopup(PopupWindow popupWindow, Runnable runOnDismiss){
        dimAppBuilderActivity();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                undimAppBuilderActivity();
                if (runOnDismiss != null) {
                    runOnDismiss.run();
                }
            }
        });
        // In order to show popUp after activity has been created
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
    }

    private Runnable afterSuccessPopupClosed (){
        return new Runnable(){
            public void run(){
                tutorial.presentTooltip(playButton, getString(R.string.tooltip_install_app), null, Gravity.BOTTOM);
                showMovedOn = true;
                moveOnButton.setVisibility(View.VISIBLE);
            }
        };
    }

    public void openTaskSuccessPopup() {
        PopupWindow successPopupWindow = new TaskSuccessPopupWindow(getApplicationContext());
        presentPopup(successPopupWindow, afterSuccessPopupClosed());
    }

    public void openStartAppPopup() {
        presentPopup(startAppPopupWindow, null);
    }

    public void undimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(0);
    }

    public void dimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(220);
    }

    public void getWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

//            globalView = view;
            // permission from user still isn't granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }

//        else {
//            // permission was already granted, download apk
//            appBuilderPresenter.downloadApk();
//        }
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
                    // permission granted, download apk from server
//                    downloadApk();

                } else {
                    // permission denied

                }
            }
        }
    }

    public String getXml() {
        return designFragment.getXml();
    }

    public void setProgressBarVisibility(int visible) {
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
            // create uri from file
            Uri contentUri = FileProvider.getUriForFile(context,
                    "com.frizzl.app.frizzlapp.fileprovider", apkFile);

            // initial intel
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(contentUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            startActivityForResult(intent, INSTALLED_APP_ABOVE_N);
        } else {
            Uri contentUri = Uri.fromFile(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
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
        if (currentAppID == 0) {
            presentNextTutorialMessage();
        }
    }

    public void presentNextTutorialMessage() {
        OnDismissListener listener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                designFragment.presentTuturialMessage();
            }
        };
        tutorial.presentTooltip(viewPager, getString(R.string.tooltip_see_task), listener, Gravity.BOTTOM);
    }

    public void updateAppNameAndIcon(String appName, String iconDrawable) {
        designFragment.setAppName(appName);
        designFragment.setAppIcon(iconDrawable);
    }

    private void disableNextArrow(){
        Drawable drawable = getResources().getDrawable(R.drawable.ic_task_arrow_icon_forward);
        nextButton.setEnabled(false);
        nextButton.setImageDrawable(drawable);
    }

    public void enableNextArrow(){
        Drawable drawable = getResources().getDrawable(R.drawable.task_arrow_animated);
        nextButton.setEnabled(true);
        nextButton.setImageDrawable(drawable);
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        if (UserProfile.user.getCurrentTaskNum() == 0 && UserProfile.user.getCurrentLevel() == 0) {
            tutorial.presentTooltip(nextButton, getString(R.string.tooltip_move_on), null, Gravity.BOTTOM);
        }
    }

    public String getCode() {
        return codingFragment.getCode();
    }

    public void taskCompleted() {
        // If not the last task
        if (UserProfile.user.getCurrentTaskNum() < UserProfile.user.getCurrentAppTasks().getTasksNum() - 1){
            enableNextArrow();
            UserProfile.user.setCurrentTaskNum(UserProfile.user.getCurrentTaskNum() + 1);
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
        UserProfile.user.setCurrentUserAppID(currentUserApp);
    }

    private String getManifest() {
        return designFragment.getManifest();
    }
}
