package com.example.yasmindunsky.frizzleapp.intro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.R;

public class LoginActivity extends AppCompatActivity {
    TextView messagePlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        messagePlaceholder = findViewById(R.id.loginMessagePlaceholder);
    }

    public void loginUser(View view) {
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (!inputIsValid(email, password)) {
            return;
        }

        loginToServer(email, password);
    }

    private boolean inputIsValid(String email, String password) {

        if (email.equals("")) {
            messagePlaceholder.setText("You forgot to enter you email address");
            return false;
        }

        if (password.equals("")) {
            messagePlaceholder.setText("You forgot to enter your password");
            return false;
        }

        return true;
    }

    private void loginToServer(String email, String password) {
        new LoginToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                messagePlaceholder.setText(output);
                //TODO if login was successful, take data from server and save to profile
            }
        }).execute(email, password);
    }
}
