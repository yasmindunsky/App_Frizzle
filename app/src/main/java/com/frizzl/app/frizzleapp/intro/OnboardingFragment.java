package com.frizzl.app.frizzleapp.intro;

import android.content.Intent;
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
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;


/**
 * Created by Noga on 13/02/2018.
 */

public class OnboardingFragment extends Fragment {
    public static final String POISITION = "position";

    private int layout;
    private int position;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        position = bundle.getInt(OnboardingFragment.POISITION);

        layout = getResources().getIdentifier("fragment_onboarding" + String.valueOf(position), "layout", getContext().getPackageName());

        view = inflater.inflate(layout, container, false);


        if (Support.isRTL()) {
            view.setRotationY(180);
        }

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
        String nickName = UserProfile.user.getNickName();
        if (position == 1) {
            if (nickName != "") {
                TextView mentorText = view.findViewById(R.id.mentorText);
                mentorText.setText(nickName + ",\n" + getResources().getString(R.string.onboardingMentorText1));
            }
        }
        else if (position == 3) {
            TextView usersName = view.findViewById(R.id.mentorText);
            usersName.setText("יאללה " + nickName + ", בואי נצא לדרך!");
        }
    }


}
