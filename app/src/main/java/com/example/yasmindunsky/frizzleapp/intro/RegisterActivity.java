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

import java.net.HttpURLConnection;

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
        //TODO set to real nickname
        final String nickName = "mini";

        // validate the parameters
        if (!inputIsValid(password, email)) {
            return;
        }

        // register the user to the server and print status message
        registerToServer(password, email, nickName);
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

    private void registerToServer(String password, final String email, final String nickName) {
        new RegisterToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                    // case of success
                if (output.equals(Integer.toString(HttpURLConnection.HTTP_OK))) {
                    messagePlaceholder.setText("Registration Succeeded");

                    // after successful registration, save username and nickname of current user
                    UserProfile.user.setUsername(email);
                    UserProfile.user.setNickName(nickName);

                    // case that email is already exists in the DB
                } else if (output.equals(Integer.toString(HttpURLConnection.HTTP_CONFLICT))) {
                    messagePlaceholder.setText("email already exists");

                    // case of any other error
                } else {
                    messagePlaceholder.setText("Something went wrong. Status: " + output);
                }
            }
        }).execute(password, email, nickName);
    }
}