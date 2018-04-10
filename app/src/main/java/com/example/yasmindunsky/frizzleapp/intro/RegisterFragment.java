package com.example.yasmindunsky.frizzleapp.intro;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.Support;
import com.example.yasmindunsky.frizzleapp.UpdatePositionInServer;
import com.example.yasmindunsky.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;


public class RegisterFragment extends Fragment {

    TextView messagePlaceholder;
    View view;
    String nickName;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        view = inflater.inflate(R.layout.fragment_onboarding3, container, false);

        if (Support.isRTL()) {
            view.setRotationY(180);
        }

        nickName = UserProfile.user.getNickName();
        TextView usersName = view.findViewById(R.id.mentorText);
        usersName.setText("יאללה " + nickName + ", בואי נצא לדרך!");

        messagePlaceholder = view.findViewById(R.id.mentorText);

        Button registerButton = view.findViewById(R.id.registerButton);
        Button loginButton = view.findViewById(R.id.goToLoginButton);

        // Register New User.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take the parameters of the new user
                String password = ((EditText) view.findViewById(R.id.password)).getText().toString();
                final String email = ((EditText) view.findViewById(R.id.email)).getText().toString();

                // validate the parameters
                if (!inputIsValid(password, email)) {
                    return;
                }

                // register the user and go to map activity
                registerToServer(password, email, nickName, view);
            }
        });

        // Go to login.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        return view;
    }

    private boolean inputIsValid(String password, String email) {

        if (password.equals("")) {
            messagePlaceholder.setText(R.string.noPassword);
            return false;
        }

        if (email.equals("")) {
            messagePlaceholder.setText(R.string.noEmail);
            return false;
        }

        return true;
    }

    private void registerToServer(String password, final String email, String nickName, final View view) {
        final String newNickName;

        if(nickName.equals("")) {
            newNickName = email;
        } else {
            newNickName = nickName;
        }

        new RegisterToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                // in case of success, create new user instance and go to map
                if (output.equals("Registration succeeded")) {

                    // after successful registration, save username and nickname of current user
                    UserProfile.user.setUsername(email);
                    UserProfile.user.setNickName(newNickName);

                    // initial position of user in UserProfile and server
                    initPosition();

                    // go to map
                    Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
                    startActivity(mapIntent);

                } else {

                    // print failed registration output message
                    messagePlaceholder.setText(output);
                }
            }
        }).execute(password, email, newNickName);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    private void initPosition(){
        UserProfile.user.setCurrentLessonID(1);
        UserProfile.user.setTopLessonID(1);
        UserProfile.user.setCurrentCourseID(1);
        UserProfile.user.setTopCourseID(1);

        new UpdatePositionInServer().execute();
    }

//    private void initProject(){
//        UserProfile.user.setJava("");
//        UserProfile.user.set
//    }
}