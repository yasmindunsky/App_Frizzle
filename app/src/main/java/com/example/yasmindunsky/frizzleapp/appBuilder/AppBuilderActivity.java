package com.example.yasmindunsky.frizzleapp.appBuilder;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.UpdatePositionInServer;
import com.example.yasmindunsky.frizzleapp.UserProfile;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;
import com.example.yasmindunsky.frizzleapp.lesson.Task;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;

public class AppBuilderActivity extends AppCompatActivity {

    final private static int WRITE_PERMISSION = 1;

    File javaFile;
    File xmlFile;
    Fragment graphicEditFragment;
    Fragment codingFragment;
    String currentTask;

    String javaCode;
    String xml;

//    View globalView;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_builder);

        graphicEditFragment = new GraphicEditFragment();
        codingFragment = new CodingFragment();

        // Set Toolbar home button.
        toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.builderToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // Error ExpandableLayout
        ImageButton clickToExpandError = findViewById(R.id.clickToExpandError);
        final ExpandableLayout errorExpandableLayout = findViewById(R.id.errorExpandableLayout);
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
        if (LessonActivity.getCurrentLesson() != null) {
            task = LessonActivity.getCurrentLesson().getTask();
        }
        // Hide if there's no task, for example when arriving straight from the map.
        else {
            clickToExpandTask.setVisibility(View.INVISIBLE);
            taskExpandableLayout.setVisibility(View.INVISIBLE);
            FrameLayout frame = findViewById(R.id.fragmentFrame);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frame.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.tabLayout);
            frame.setLayoutParams(layoutParams);
        }

        TextView taskTextView = (TextView) findViewById(R.id.task);
        taskTextView.setText(task.getText());

        final TabLayout tabLayout = findViewById(R.id.tabLayout); // get the reference of TabLayout
        final TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.graphicEditScreenTitle);
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.codeScreenTitle);


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


        // Open Java and XML files.
        File path = getBaseContext().getFilesDir();
        javaFile = new File(path, getString(R.string.javaFileName));
        xmlFile = new File(path, getString(R.string.xmlFileName));
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
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);

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

        int width = GridLayout.LayoutParams.MATCH_PARENT;
        int height = GridLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(toolbar, Gravity.CENTER, 0, 0);

        Button continueButton = popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentAndTopPosition();
                goToMap();
            }
        });


    }

    private void goToMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    private void updateCurrentAndTopPosition() {
        int nextLesson = UserProfile.user.getCurrentLessonID() + 1;
        if (nextLesson <= 7) {
            UserProfile.user.setCurrentLessonID(nextLesson);
            if (nextLesson > UserProfile.user.getTopLessonID()) {
                UserProfile.user.setTopLessonID(nextLesson);

                // update position in server
                new UpdatePositionInServer().execute();
            }
        }
    }

    public void onPlay(final View view) {
        // update java code String
        String codeWritten = ((CodingFragment) codingFragment).getCode();
        javaCode = getApplicationContext().getResources().getString(R.string.codeStart) +
                codeWritten + getApplicationContext().getResources().getString(R.string.codeEnd);

        // update xml String
        xml = ((GraphicEditFragment) graphicEditFragment).getXml();

        // send java and xml to server for build
        // if succeeded ask user for writing permission and download the apk
         new BuildApkInServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if (output.contains("BUILD SUCCESSFUL")) {
                    getWritePermission(view);
                }
                else {
                    // Build didn't work.
                    displayError(output);
                }
            }
        }).execute(xml, javaCode);
    }

    private void displayError(String output) {
        TextView error = findViewById(R.id.error);
        error.setText(output);
        LinearLayout errorLayout = findViewById(R.id.errorDisplay);
        errorLayout.setVisibility(View.VISIBLE);
    }

    public void goToLesson(View view) {
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
        }).execute(xml, javaCode);

    }

    public void startApk(View view) {
        //get destination
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "frizzle_project1.apk";
        File apkFile = new File(destination);
        Context context = view.getContext();

        // create uri from file
        Uri contentUri = FileProvider.getUriForFile(context, "com.example.yasmindunsky.frizzleapp.fileprovider", apkFile);

        // initial intel
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(contentUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }

}
