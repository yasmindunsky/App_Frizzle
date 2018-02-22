package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.UserProfile;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;
import com.example.yasmindunsky.frizzleapp.lesson.Task;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppBuilderActivity extends AppCompatActivity {

    File javaFile;
    File xmlFile;
    Fragment graphicEditFragment;
    Fragment codingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_builder);

        graphicEditFragment = new GraphicEditFragment();
        codingFragment = new CodingFragment();

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

        // ExpandableLayout
        ImageButton clickToExpand = findViewById(R.id.clickToExpand);
        final ExpandableLayout expandableLayout = findViewById(R.id.expandable_layout);
        clickToExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                } else {
                    expandableLayout.expand();
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
            clickToExpand.setVisibility(View.INVISIBLE);
            expandableLayout.setVisibility(View.INVISIBLE);
            FrameLayout frame = findViewById(R.id.fragmentFrame);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)frame.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.tabLayout);
            frame.setLayoutParams(layoutParams);
        }

        TextView taskTextView = (TextView)findViewById(R.id.task);
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

        tabLayout.addTab(graphicEditTab,true);
        tabLayout.addTab(codingTab);
        graphicEditTab.select();


        // Open Java and XML files.
        File path = getBaseContext().getFilesDir();
        javaFile = new File(path, getString(R.string.javaFileName));
        xmlFile = new File(path, getString(R.string.xmlFileName));
    }

    public void onPlay(View view) {
        // Write code written in EditText to java file.
        String codeWritten = ((CodingFragment) codingFragment).getCode();
        String codeToSave = getApplicationContext().getResources().getString(R.string.codeStart) +
                codeWritten + getApplicationContext().getResources().getString(R.string.codeEnd);
        writeToFile(javaFile, codeWritten);

        // Write xml to xml file.
        String xmlWritten = ((GraphicEditFragment) graphicEditFragment).getXml();
        writeToFile(xmlFile, xmlWritten);

//        new SendFilesToServer(new AsyncResponse() {
//            @Override
//            public void processFinish(String output) {
//                //TODO get server response
//            }
//        }).execute(xmlFile.toString(), javaFile.toString());
    }

    private void writeToFile(File file, String data) {
        try {
            FileOutputStream stream = new FileOutputStream(file, false);
            stream.write(data.getBytes());
            stream.close();
        } catch (IOException e) {
            Log.e("Exception", "File " + file.toString() + " write failed: " + e.toString());
        }
    }

    private String readFromFile(File file) {
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (IOException e) {
            Log.e("Exception", "File " + file.toString() + " read failed: " + e.toString());
        }
        return new String(bytes);
    }

    public void goToLesson(View view) {
        Intent lessonIntent = new Intent(this, LessonActivity.class);
        startActivity(lessonIntent);
    }
}
