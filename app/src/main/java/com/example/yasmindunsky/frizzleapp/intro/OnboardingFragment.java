package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.Support;
import com.example.yasmindunsky.frizzleapp.UserProfile;


/**
 * Created by Noga on 13/02/2018.
 */

public class OnboardingFragment extends Fragment {
    private int layout;
    private int position;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        position = bundle.getInt("position");

        layout = getResources().getIdentifier("fragment_onboarding" + String.valueOf(position+1), "layout", getContext().getPackageName());

        view = inflater.inflate(layout, container, false);

        if (Support.isRTL()) {
            view.setRotationY(180);
        }

        if (position == 0) {
            EditText usersNameInput = view.findViewById(R.id.usersNameInput);
            usersNameInput.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText editText = (EditText) v;
                    UserProfile.user.setNickName(String.valueOf(editText.getText()));
                }
            });
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String nickName = UserProfile.user.getNickName();
                if (position == 1) {
                    if (nickName != "") {
                        TextView usersName = v.findViewById(R.id.usersName);
                        usersName.setText(nickName + ",");
                    }
                }
                else if (position == 3) {
                    TextView usersName = v.findViewById(R.id.mentorText);
                    usersName.setText(" יאללה" + nickName + ",\nבואי נצא לדרך!");
                }
                return true;
            }
        });

        return view;
    }
}
