package com.example.yasmindunsky.frizzleapp.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.AsyncResponse;
import com.example.yasmindunsky.frizzleapp.R;

public class RegisterActivity extends AppCompatActivity {

    TextView messagePlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button createUserButton = findViewById(R.id.registerButton);
        createUserButton.setOnClickListener(createNewUser);

        Button goToLogin = findViewById(R.id.goToLoginButton);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        messagePlaceholder = findViewById(R.id.registerMessagePlaceholder);
    }

    View.OnClickListener createNewUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            String email = ((EditText) findViewById(R.id.email)).getText().toString();
            //TODO set to real nickname
            String nickName = "mini";

            if (!inputIsValid(password, email)) {
                return;
            }

            new RegisterToServer(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    messagePlaceholder.setText(output);
                }
            }).execute(password, email, nickName);
        }
    };

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

}