package com.example.yasmindunsky.frizzleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

            String password = ((EditText)findViewById(R.id.password)).getText().toString();
            String email = ((EditText)findViewById(R.id.email)).getText().toString();

            if(!inputIsValid(password, email)){
                return;
            }

            //TODO just for debugging, delete after adding a response check from the server
            TextView errorMessagePlaceHolder = findViewById(R.id.errorMessagePlaceHolder);
            errorMessagePlaceHolder.setText("Values has been validated");

            new CreateNewUser().execute(password, email);
        }
    };

    private boolean inputIsValid(String password, String email){
        TextView errorMessagePlaceHolder = findViewById(R.id.errorMessagePlaceHolder);

        if(password.equals("")){
            errorMessagePlaceHolder.setText("You forgot to choose a password");
            return false;
        }

        if(email.equals("")){
            errorMessagePlaceHolder.setText("You forgot to fill out your email address");
            return false;
        }

        //TODO check if mail already exists?
        //TODO check that mail is valid? before or after server response

        return true;
    }

}
