package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.UserProfile;


public class RegisterActivity extends AppCompatActivity {

    TextView messagePlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        messagePlaceholder = findViewById(R.id.registerMessagePlaceholder);
    }

    public void goToLogin(View view) {
        Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(loginIntent);
    }

    public void goToMap(View view) {
        Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
        startActivity(mapIntent);
    }

    public void registerNewUser(View view) {

        // take the parameters of the new user
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        final String email = ((EditText) findViewById(R.id.email)).getText().toString();
        final String nickName = "mini";

        // validate the parameters
        if (!inputIsValid(password, email)) {
            return;
        }

        // register the user and go to map activity
        registerToServer(password, email, nickName, view);
    }

    private boolean inputIsValid(String password, String email) {

        if (password.equals("")) {
            messagePlaceholder.setText("You forgot to choose a password");
            return false;
        }

        if (email.equals("")) {
            messagePlaceholder.setText("You forgot to fill out your email address");
            return false;
        }

        return true;
    }

    private void registerToServer(String password, final String email, final String nickName, final View view) {
        new RegisterToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                // in case of success, create new user instance and go to map
                if (output.equals("Registration Succeeded")) {

                    // after successful registration, save username and nickname of current user
                    UserProfile.user.setUsername(email);
                    UserProfile.user.setNickName(nickName);

                    // go to map
//                    Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
//                    startActivity(mapIntent);
//
                } else {

                    // print failed registration output message
                    messagePlaceholder.setText(output);
                }
            }
        }).execute(password, email, nickName);
    }
}