package com.example.yasmindunsky.frizzleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
            String email = ((EditText)findViewById(R.id.email)).getText().toString();
            String password = ((EditText)findViewById(R.id.password)).getText().toString();

            if(!inputIsValid(email, password)){
                return;
            }

            new LoginToServer().execute(email, password);

            //TODO set errorMessage text if wrong response from the server
        }
    };

    private boolean inputIsValid(String email, String password){
        TextView errorMessagePlaceHolder = findViewById(R.id.errorMessagePlaceHolder);

        if(email.equals("")){
            errorMessagePlaceHolder.setText("You forgot to enter you email address");
            return false;
        }

        if(password.equals("")){
            errorMessagePlaceHolder.setText("You forgot to enter your password");
            return false;
        }

        return true;
    }
}
