package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yasmindunsky.frizzleapp.R;

public class AppBuilderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_builder);

        TabLayout tabLayout = findViewById(R.id.tabLayout); // get the reference of TabLayout
        TabLayout.Tab codingTab = tabLayout.newTab().setText("Code");
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
