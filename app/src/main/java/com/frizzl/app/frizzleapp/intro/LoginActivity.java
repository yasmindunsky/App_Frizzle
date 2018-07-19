package com.frizzl.app.frizzleapp.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity {
    private LoginPresenter presenter;

    private TextView messagePlaceholder;
    private Button registerButton;
    private Button loginButton;
    private EditText username;
    private EditText password;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        messagePlaceholder = findViewById(R.id.mentorText);
        registerButton = findViewById(R.id.goToRegisterButton);
        loginButton = findViewById(R.id.loginButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), OnboardingActivity.class));
            }
        });

        loginButton.setOnClickListener(loginUser);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    View.OnClickListener loginUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            login();

            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        }
    };

    public void showMessage(String message){
        messagePlaceholder.setText(message);
    }

    public void login() {
        presenter.login(username.getText().toString(), password.getText().toString());
    }

    public void navigateToMap() {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void setPasswordError() {
        showMessage(getString(R.string.no_password_login));
    }

    public void setUsernameError() {
        showMessage(getString(R.string.no_username_login));
    }
}
