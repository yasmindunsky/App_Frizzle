package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;

public class AppBuilderActivity extends AppCompatActivity {

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


        TabLayout tabLayout = findViewById(R.id.tabLayout); // get the reference of TabLayout
        TabLayout.Tab codingTab = tabLayout.newTab().setText("Code Task");
        TabLayout.Tab graphicEditTab = tabLayout.newTab().setText("Graphic Edit");

        final Fragment codingFragment = new CodingFragment();
        final Fragment graphicEditFragment = new GraphicEditFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {

            }

            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;

                if(tab.getPosition() == 0){
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
    }

}
