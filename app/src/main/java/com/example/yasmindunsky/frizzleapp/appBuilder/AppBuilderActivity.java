package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.lesson.Lesson;
import com.example.yasmindunsky.frizzleapp.lesson.LessonActivity;
import com.example.yasmindunsky.frizzleapp.lesson.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppBuilderActivity extends AppCompatActivity {

    File javaFile;
    File xmlFile;
    Fragment graphicEditFragment;
    Fragment codingFragment;
    String currentTask;

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
//        Task task = LessonActivity.getCurrentLesson().getTask();
//        TextView taskTextView = (TextView)findViewById(R.id.task);
//        taskTextView.setText(task.getText());

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

    public void onPlay(View view) {
        // Write code written in EditText to java file.
        String codeWritten = ((CodingFragment) codingFragment).getCode();
        String codeToSave = getApplicationContext().getResources().getString(R.string.codeStart) +
                codeWritten + getApplicationContext().getResources().getString(R.string.codeEnd);
        writeToFile(javaFile, codeWritten);

        // Write xml to xml file.
        String xmlWritten = ((GraphicEditFragment) graphicEditFragment).getXml();
        writeToFile(xmlFile, xmlWritten);

        new SendFilesToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //TODO get server response
            }
        }).execute(((GraphicEditFragment) graphicEditFragment).getXml(), codeToSave);
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
}
