package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.GetPositionFromServer;
import com.example.yasmindunsky.frizzleapp.MapActivity;
import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.UserProfile;

public class LoginActivity extends AppCompatActivity {
    TextView messagePlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        messagePlaceholder = findViewById(R.id.mentorText);

        // Go to login.
        Button registerButton = findViewById(R.id.goToRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(v.getContext(), OnboardingActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public void loginUser(View view) {
        // take the parameters of the user
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        // validate the parameters
        if (!inputIsValid(email, password)) {
            return;
        }

        // register the user to the server and print status message
        loginToServer(email, password, view);
    }

    private boolean inputIsValid(String email, String password) {

        if (email.equals("")) {
            messagePlaceholder.setText(R.string.noEmail);
            return false;
        }

        if (password.equals("")) {
            messagePlaceholder.setText(R.string.noPassword);
            return false;
        }

        return true;
    }

    private void loginToServer(final String email, String password, final View view) {
        new LoginToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                // in case of success, create new user instance, restore users data and go to map
                if (output.equals("Authentication succeeded")) {

                    // after successful login, update username of current user
                    UserProfile.user.setUsername(email);


                    // update user project from server
                    new GetProjectFromServer(view.getContext()).execute(email, "views");
                    new GetProjectFromServer(view.getContext()).execute(email, "xml");
                    new GetProjectFromServer(view.getContext()).execute(email, "code");

                    // update user position from server, when done go to map
                    new GetPositionFromServer(new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
                            startActivity(mapIntent);
                        }
                    }).execute(email);
                } else {

                    // print failed login output message
                    messagePlaceholder.setText(output);
                }
            }
        }).execute(email, password);
    }

    public void goToMap() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }
}
