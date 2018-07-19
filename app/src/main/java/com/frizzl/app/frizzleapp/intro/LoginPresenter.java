package com.frizzl.app.frizzleapp.intro;

import android.content.Context;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.preferences.SaveSharedPreference;

/**
 * Created by Noga on 18/07/2018.
 */

public class LoginPresenter {
    private LoginActivity loginActivity;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void onDestroy() {
        loginActivity = null;
    }

    public void login(String username, String password) {
        if (username.equals("")) {
            onUsernameError();
            return;
        }

        if (password.equals("")) {
            onPasswordError();
            return;
        }

        loginToServer(username, password);
    }

    private void loginToServer(final String username, String password) {
        new LoginToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                // in case of success, create new user instance, restore users data and go to map
                if (output.equals("Authentication succeeded")) {
                    onSuccess(username);
                } else {
                    onFail(output);
                }
            }
        }).execute(username, password);
    }

    public void onUsernameError() {
        if (loginActivity != null) {
            loginActivity.setUsernameError();
        }
    }

    public void onPasswordError() {
        if (loginActivity != null) {
            loginActivity.setPasswordError();
        }
    }

    public void onSuccess(String username) {
        Context context = loginActivity.getApplicationContext();
        loginActivity.showMessage(context.getString(R.string.connecting));

        // after successful login, update username of current user
        UserProfile.user.setUsername(username);

        //TODO change to real nickname from server
        UserProfile.user.setNickName(username);

        UserProfile.user.updateProfileFromServerAndGoToMap(context);
        SaveSharedPreference.setUsername(context, username);
    }

    public void onFail(String output) {
        if (loginActivity != null) {
            loginActivity.showMessage(output);
        }
    }
}
