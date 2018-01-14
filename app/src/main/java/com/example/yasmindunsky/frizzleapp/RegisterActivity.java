package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

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
    }

    View.OnClickListener createNewUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userName = ((EditText)findViewById(R.id.username)).getText().toString();
            String email = ((EditText)findViewById(R.id.email)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            new CreateNewUser().execute(userName, email, password);
        }
    };
}
