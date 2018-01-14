package com.example.yasmindunsky.frizzleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginOnClick);

    }

    View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userName = ((EditText)findViewById(R.id.username)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            new LoginToServer().execute(userName, password);
        }
    };
}
