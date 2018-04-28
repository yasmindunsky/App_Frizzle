package com.frizzl.app.frizzleapp.appBuilder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.SecondCourseActivity;
import com.frizzl.app.frizzleapp.UpdatePositionInServer;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.lesson.LessonActivity;
import com.frizzl.app.frizzleapp.lesson.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.Map;

public class AppBuilderActivity extends AppCompatActivity {

    final private static int WRITE_PERMISSION = 1;

    File javaFile;
    File xmlFile;
    Fragment graphicEditFragment;
    Fragment codingFragment;
    String currentTask;
    private ProgressBar progressBar = null;
    private ConstraintLayout constraintLayout;
    private ExpandableLayout errorExpandableLayout = null;
    private FirebaseAnalytics mFirebaseAnalytics;

            //    View globalView;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        super.onCreate(savedInstanceState);
        ConstraintLayout mainLayout = (ConstraintLayout ) this.getLayoutInflater().inflate(R.layout.activity_app_builder, null);
        setContentView(mainLayout);

        // Disable dim
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setForeground(getResources().getDrawable(R.drawable.shade));
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                undimAppBuilderActivity();
            }
        });


        graphicEditFragment = new GraphicEditFragment();
        codingFragment = new CodingFragment();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // Set Toolbar home button.
        toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.builderToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save user project
                updateUserProjectAttributes();

                // Go to map home screen
                updateUserProjectAttributes();
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
        ImageButton doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuccessPopup();
            }
        });

        // Error ExpandableLayout
        ImageButton clickToExpandError = findViewById(R.id.clickToExpandError);
        errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
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

        // Task ExpandableLayout
        ImageButton clickToExpandTask = findViewById(R.id.clickToExpandTask);
        final ExpandableLayout taskExpandableLayout = findViewById(R.id.taskExpandableLayout);
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
        TextView taskTextView = findViewById(R.id.task);
        if (LessonActivity.getCurrentLesson() != null) {
            task = LessonActivity.getCurrentLesson().getTask();
        }
        // Hide if there's no task, for example when arriving straight from the map.
        else {
            clickToExpandTask.setVisibility(View.GONE);
            taskExpandableLayout.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            taskTextView.setVisibility(View.GONE);
//            FrameLayout frame = findViewById(R.id.fragmentFrame);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frame.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.BELOW, R.id.tabLayout);
//            frame.setLayoutParams(layoutParams);
        }

        taskTextView.setText(task.getText());

        final TabLayout tabLayout = findViewById(R.id.tabLayout); // get the reference of TabLayout
        final TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.graphic_edit_screen_title);
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.code_screen_title);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {

            }

            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                if (tab.getPosition() == 0) {
                    fragment = graphicEditFragment;
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
                }
                else {
                    TextView error = popupView.findViewById(R.id.errorPlaceholder);
                    error.setText("קוד לא נכון, נסי שוב");
                }
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
                if (UserProfile.user.getCurrentLessonID() == 7) {
                    popupWindow.dismiss();
                    openFinishedAppPopUp();
                }
                // Else, go to map
                else {
                    updateCurrentAndTopPosition();
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
        constraintLayout.getForeground().setAlpha(0);
    }

    public void dimAppBuilderActivity() {
        constraintLayout.getForeground().setAlpha(220);
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
                openSharePopUp();
            }
        });

        TextView skip = popupView.findViewById(R.id.notReady);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
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
        updateUserProjectAttributes();
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    private void goToNextCourse() {
        updateUserProjectAttributes();
        Intent mapIntent = new Intent(this, SecondCourseActivity.class);
        startActivity(mapIntent);
    }

    private void updateCurrentAndTopPosition() {
        int nextLesson = UserProfile.user.getCurrentLessonID() + 1;
        if (nextLesson <= 13) {
            UserProfile.user.setCurrentLessonID(nextLesson);
            if (nextLesson > UserProfile.user.getTopLessonID()) {
                UserProfile.user.setTopLessonID(nextLesson);

                // update position in server
                new UpdatePositionInServer().execute();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("LESSON", String.valueOf(nextLesson));
        mFirebaseAnalytics.logEvent("LESSON_UP", bundle);
    }

    public void onPlay(final View view) {
        updateUserProjectAttributes();
        progressBar.setVisibility(View.VISIBLE);
        // send java and xml to server for build
        // if succeeded ask user for writing permission and download the apk
         new BuildApkInServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                progressBar.setVisibility(View.GONE);
                if (output.contains("BUILD SUCCESSFUL")) {
                    getWritePermission(view);
                    hideError();
                }
                else {
                    // Build didn't work.
                    displayError(output);
                }
            }
        }).execute(getApplicationContext().getResources().getString(R.string.code_start), getApplicationContext().getResources().getString(R.string.code_end));
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("RUN_APP", bundle);
    }


    private void updateUserProjectAttributes(){
        // update java code String
        String codeWritten = ((CodingFragment) codingFragment).getCode();
        UserProfile.user.setJava(codeWritten);

        // update xml String
        String xml = ((GraphicEditFragment) graphicEditFragment).getXml();
        UserProfile.user.setXml(xml);

        // update views string
        Map<Integer, UserCreatedView> views = ((GraphicEditFragment) graphicEditFragment).getViews();
        UserProfile.user.setViews(views);
    }

    private void hideError() {
        LinearLayout errorLayout = findViewById(R.id.errorDisplay);
        errorLayout.setVisibility(View.GONE);
    }

    private void displayError(String output) {
        TextView error = findViewById(R.id.error);
        error.setText(output);
        LinearLayout errorLayout = findViewById(R.id.errorDisplay);
        errorLayout.setVisibility(View.VISIBLE);
        errorExpandableLayout.expand();
    }

    public void goToLesson(View view) {
        updateUserProjectAttributes();
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }

    private void getWritePermission(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

//            globalView = view;
            // permission from user still isn't granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            // permission was already granted, download apk
            downloadApk(view);
        }
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

    public void downloadApk(final View view) {
        new DownloadApkFromServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                startApk(view);
            }
        }).execute(UserProfile.user.getXml(), UserProfile.user.getJava());

    }

    public void startApk(View view) {
        //get destination
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/frizzle_project1.apk";
        File apkFile = new File(destination);
        Context context = view.getContext();

        // create uri from file
        Uri contentUri = FileProvider.getUriForFile(context, "com.frizzl.app.frizzleapp.fileprovider", apkFile);

        // initial intel
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(contentUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }
}
