package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.frizzl.app.frizzleapp.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.SecondCourseActivity;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.lesson.LessonActivity;
import com.frizzl.app.frizzleapp.lesson.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public class AppBuilderActivity extends AppCompatActivity {

    private AppBuilderPresenter appBuilderPresenter;

    private DesignScreenFragment designFragment;
    private CodingScreenFragment codingFragment;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private ExpandableLayout errorExpandableLayout;
    private ExpandableLayout taskExpandableLayout;
    private TabLayout tabLayout;
    private ImageButton nextButton;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton clickToExpandError;
    private ImageButton clickToExpandTask;
    private TextView taskTextView;

    private FirebaseAnalytics mFirebaseAnalytics;

    final private static int WRITE_PERMISSION = 1;
    private static final int MAX_NICKNAME_LENGTH = 10;

    String currentTask;
    File javaFile;
    File xmlFile;
    private boolean activityCreated = false;

    //    View globalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
        RelativeLayout mainLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.activity_app_builder, null);
        setContentView(mainLayout);

        appBuilderPresenter = new AppBuilderPresenter(this, getApplicationContext().getResources().getString(R.string.code_start),
                getApplicationContext().getResources().getString(R.string.code_end));
        designFragment = new DesignScreenFragment();
        codingFragment = new CodingScreenFragment();

        errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
        taskExpandableLayout = findViewById(R.id.taskExpandableLayout);
        clickToExpandError = findViewById(R.id.clickToExpandError);
        clickToExpandTask = findViewById(R.id.clickToExpandTask);
        relativeLayout = findViewById(R.id.constraintLayout);
        taskTextView = findViewById(R.id.task);
        progressBar = findViewById(R.id.progressBar);
        nextButton = findViewById(R.id.nextTask);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.builderToolbar);


        // Disable dim
        relativeLayout.setForeground(getResources().getDrawable(R.drawable.shade));
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                undimAppBuilderActivity();
            }
        });

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

        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openInstructorPopup();
                return false;
            }
        });

        // Set Done Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuccessPopup();
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

        clickToExpandTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskExpandableLayout.isExpanded()) {
                    taskExpandableLayout.collapse();
                } else {
                    taskExpandableLayout.expand();
                }
            }
        });

        // Set Task text.
        Task task = new Task("");

        if (LessonActivity.getCurrentLesson() != null) {
            task = LessonActivity.getCurrentLesson().getTask();
        }
        // Hide if there's no task, for example when arriving straight from the map.
        else {
            clickToExpandTask.setVisibility(View.GONE);
            taskExpandableLayout.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            taskTextView.setVisibility(View.GONE);
        }
        taskTextView.setText(task.getText());


        final TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.graphic_edit_screen_title);
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.code_screen_title);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                if (activityCreated) {
                    appBuilderPresenter.saveProject();
                }
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

        activityCreated = true;
    }

    public void onPlay(final View view) {
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("RUN_APP", bundle);
        appBuilderPresenter.compileAndDownloadApp();
        setProgressBarVisibility(View.VISIBLE);
    }

    private void openInstructorPopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_instructor, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        toolbar.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
        dimAppBuilderActivity();

        Button confirmButton = popupView.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText code = popupView.findViewById(R.id.codeValue);
                if ("beyonce".equals(code.getText().toString().toLowerCase())) {
                    openSuccessPopup();
                } else {
                    TextView error = popupView.findViewById(R.id.errorPlaceholder);
                    error.setText("קוד לא נכון, נסי שוב");
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                undimAppBuilderActivity();
            }
        });

    }

    private void openSuccessPopup() {
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_task_success, null);

        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        toolbar.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
        dimAppBuilderActivity();

        Button continueButton = popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If this is the last lesson, go to share screen
                if (UserProfile.user.getCurrentLessonID() == 8) {
                    popupWindow.dismiss();
                    openFinishedAppPopUp();
                }
                // Else, go to map
                else {
                    int nextLesson = appBuilderPresenter.updateCurrentAndTopPosition();
                    Bundle bundle = new Bundle();
                    bundle.putLong(FirebaseAnalytics.Param.LEVEL, nextLesson);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);
                    Crashlytics.setInt("current_lesson", nextLesson);
                    goToMap();
                }
            }
        });

        TextView notReady = popupView.findViewById(R.id.notReady);
        notReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                undimAppBuilderActivity();
            }
        });
    }

    public void undimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(0);
    }

    public void dimAppBuilderActivity() {
        relativeLayout.getForeground().setAlpha(220);
    }

    private void openFinishedAppPopUp() {
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_finished_app, null);

        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        toolbar.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
        dimAppBuilderActivity();

        Button shareButton = popupView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // User chose to share!
                Bundle bundle = new Bundle();
                bundle.putString("SHARED", String.valueOf(true));
                mFirebaseAnalytics.logEvent("SHARED_APP", bundle);

                openSharePopUp();
            }
        });

        TextView skip = popupView.findViewById(R.id.notReady);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                goToNextCourse();

                // User chose not to share
                Bundle bundle = new Bundle();
                bundle.putString("SHARED", String.valueOf(false));
                mFirebaseAnalytics.logEvent("SHARED_APP", bundle);
            }
        });

        // Personalize photo
        String usersNickname = UserProfile.user.getNickName();
        if (!usersNickname.isEmpty() && usersNickname.length() < MAX_NICKNAME_LENGTH) {
            TextView myTriviaApp = popupView.findViewById(R.id.myTriviaApp);
            if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.toLanguageTag())){
                myTriviaApp.setText(usersNickname + "'s Trivia App");
            }
            else {
                myTriviaApp.setText("אפליקציית הטריוויה\nשל " + usersNickname);
            }
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                undimAppBuilderActivity();
            }
        });

    }

    public void getWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

//            globalView = view;
            // permission from user still isn't granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            // permission was already granted, download apk
            appBuilderPresenter.downloadApk();
        }
    }

    private void openSharePopUp() {
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_share, null);

        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        toolbar.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);
            }
        });
        dimAppBuilderActivity();

        Button gotItButton = popupView.findViewById(R.id.gotItButton);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextCourse();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                undimAppBuilderActivity();
            }
        });
    }

    private void goToMap() {
        appBuilderPresenter.saveProject();
        navigateToMap();
    }

    private void goToNextCourse() {
        appBuilderPresenter.saveProject();
        navigateToSecondCourse();
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
                return;
            }
        }
    }

    public void goToLesson(View view) {
        appBuilderPresenter.saveProject();
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }

    public void navigateToMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    public void navigateToSecondCourse() {
        Intent mapIntent = new Intent(this, SecondCourseActivity.class);
        startActivity(mapIntent);
    }

    public Map<Integer,UserCreatedView> getViews() {
        return designFragment.getViews();
    }

    public String getXml() {
        return designFragment.getXml();
    }

    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    public void startApk() {
        setProgressBarVisibility(View.GONE);

        //get destination
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/frizzle_project1.apk";
        File apkFile = new File(destination);
        Context context = getApplicationContext();

        // create uri from file
        Uri contentUri = FileProvider.getUriForFile(context, "com.frizzl.app.frizzleapp.fileprovider", apkFile);

        // initial intel
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(contentUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
