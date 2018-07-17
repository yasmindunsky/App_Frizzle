package com.frizzl.app.frizzleapp.intro;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.ArrayList;


/**
 * Created by Noga on 13/02/2018.
 */

public class OnboardingFragment extends Fragment {
    public static final String POISITION = "position";
    private static final int FRIENDSHIP_TEST_APP_NUM = 0;
    private static final int TRIVIA_MASTER_APP_NUM = 1;
    private static final int FLASH_CARDS_APP_NUM = 2;

    private int layout;
    private int position;
    private View view;
    ArrayList<RelativeLayout> appLayouts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        position = bundle.getInt(OnboardingFragment.POISITION);

        layout = getResources().getIdentifier("fragment_onboarding" + String.valueOf(position), "layout", getContext().getPackageName());

        view = inflater.inflate(layout, container, false);


//        if (Support.isRTL()) {
//            view.setRotationY(180);
//        }

        if (position == 0) {
            Button loginButton = view.findViewById(R.id.goToLoginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            });

            final EditText usersNameInput = view.findViewById(R.id.usersNameInput);
            usersNameInput.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                public void afterTextChanged(Editable s) {
                    if(usersNameInput.getText().toString().equals("")){
                        usersNameInput.setTextColor(getResources().getColor(R.color.TextLightGrey));
                    } else {
                        usersNameInput.setTextColor(getResources().getColor(R.color.TextGrey));
                        String nickname = String.valueOf(usersNameInput.getText());
                        UserProfile.user.setNickName(nickname);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (2 == position) {
            ImageView mapAnimation = (ImageView) view.findViewById(R.id.mapAnimation);
            mapAnimation.setBackgroundResource(R.drawable.onboarding_map);
            AnimationDrawable animationDrawable = (AnimationDrawable) mapAnimation.getBackground();
            animationDrawable.start();

        }
        else if (1 == position) {
            final RelativeLayout relativeLayout1 = view.findViewById(R.id.app1_button);
            final RelativeLayout relativeLayout2 = view.findViewById(R.id.app2_button);
            final RelativeLayout relativeLayout3 = view.findViewById(R.id.app3_button);
            appLayouts.add(relativeLayout1);
            appLayouts.add(relativeLayout2);
            appLayouts.add(relativeLayout3);

            relativeLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppSelected(FRIENDSHIP_TEST_APP_NUM);
                }
            });
            relativeLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppSelected(TRIVIA_MASTER_APP_NUM);
                }
            });
            relativeLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppSelected(FLASH_CARDS_APP_NUM);
                }
            });
        }
    }

    public void onAppSelected(int selectedAppNum){
        for (int i = 0; i < appLayouts.size(); i++) {
            if (selectedAppNum == i) {
                appLayouts.get(i).setSelected(true);
                UserProfile.user.setCurrentAppTypeNum(selectedAppNum);

                View mentorText1 = view.findViewById(R.id.mentorText1);
                mentorText1.setVisibility(View.INVISIBLE);
                View mentorText2 = view.findViewById(R.id.mentorText2);
                mentorText2.setVisibility(View.VISIBLE);
            }
            else {
                appLayouts.get(i).setSelected(false);
            }
        }
    }
}
