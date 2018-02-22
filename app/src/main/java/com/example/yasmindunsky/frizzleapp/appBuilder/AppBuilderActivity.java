package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;
import com.example.yasmindunsky.frizzleapp.lesson.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppBuilderActivity extends AppCompatActivity {

    final private static int WRITE_PERMISSION = 1;

    File javaFile;
    File xmlFile;
    Fragment graphicEditFragment;
    Fragment codingFragment;
    String currentTask;

    String javaCode;
    String xml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_builder);

        // Set Toolbar home button.
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.builderToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to map home screen
                Intent mapIntent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

        // Set Task text.
        Task task = new Task("");
        if (LessonActivity.getCurrentLesson() != null) {
            task = LessonActivity.getCurrentLesson().getTask();
        }
        TextView taskTextView = (TextView) findViewById(R.id.task);
        taskTextView.setText(task.getText());

        TabLayout tabLayout = findViewById(R.id.tabLayout); // get the reference of TabLayout
        TabLayout.Tab codingTab = tabLayout.newTab().setText(R.string.codeScreenTitle);
        TabLayout.Tab graphicEditTab = tabLayout.newTab().setText(R.string.graphicEditScreenTitle);

        codingFragment = new CodingFragment();
        graphicEditFragment = new GraphicEditFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {

            }

            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;

                if (tab.getPosition() == 0) {
                    fragment = codingFragment;
                } else {
                    fragment = graphicEditFragment;
                }

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentFrame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            public void onTabUnselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.addTab(codingTab, true);
        tabLayout.addTab(graphicEditTab);

        // Open Java and XML files.
        File path = getBaseContext().getFilesDir();
        javaFile = new File(path, getString(R.string.javaFileName));
        xmlFile = new File(path, getString(R.string.xmlFileName));
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
                    getWritePermission();
                }
            }
        }).execute(xml, javaCode);
    }

    private void getWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // permission from user still isn't granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            // permission was already granted, download apk
            new DownloadApkFromServer().execute(xml, javaCode);
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
                    new DownloadApkFromServer().execute(xml, javaCode);

                } else {
                    // permission denied

                }
                return;
            }
        }
    }

}
