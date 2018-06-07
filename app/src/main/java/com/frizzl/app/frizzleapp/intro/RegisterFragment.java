package com.frizzl.app.frizzleapp.intro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.AsyncResponse;
import com.frizzl.app.frizzleapp.MapActivity;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UpdatePositionInServer;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegisterFragment extends Fragment {

    private static final int DEFAULT_BIRTH_YEAR = 2000;
    private static final int DEFAULT_BIRTH_DAY_OF_MONTH = 1;
    private static final int DEFAULT_BIRTH_MONTH = Calendar.JANUARY;
    TextView messagePlaceholder;
    View view;
    String nickName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        view = inflater.inflate(R.layout.fragment_onboarding3, container, false);

        if (Support.isRTL()) {
            view.setRotationY(180);
        }

        nickName = UserProfile.user.getNickName();
        TextView usersName = view.findViewById(R.id.mentorText);
        if (!nickName.equals("")) {
            usersName.setText(getResources().getString(R.string.onboardingMentorText3Part1) + " " +
                nickName + ", " + getResources().getString(R.string.onboardingMentorText3Part2));
        }

        messagePlaceholder = view.findViewById(R.id.mentorText);

        Button registerButton = view.findViewById(R.id.registerButton);
        Button loginButton = view.findViewById(R.id.goToLoginButton);

        // Register New User.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take the parameters of the new user
                String password = ((EditText) view.findViewById(R.id.password)).getText().toString();
                final String email = ((EditText) view.findViewById(R.id.username)).getText().toString();

                // validate the parameters
                if (!inputIsValid(password, email)) {
                    return;
                }

                // Send to firebase
                mFirebaseAnalytics.setUserProperty("birth_year", String.valueOf(myCalendar.get(Calendar.YEAR)));


                // register the user and go to map activity
                registerToServer(password, email, nickName, view);
            }
        });

        // Go to login.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });


        Button birthDate = view.findViewById(R.id.birthdate);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        birthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, DEFAULT_BIRTH_YEAR);
                myCalendar.set(Calendar.MONTH, DEFAULT_BIRTH_MONTH);
                myCalendar.set(Calendar.DAY_OF_MONTH, DEFAULT_BIRTH_DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.MySpinnerDatePickerStyle, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();


            }
        });

        return view;
    }

    private boolean inputIsValid(String password, String email) {

        if (password.equals("")) {
            messagePlaceholder.setText(R.string.no_password);
            return false;
        }

        if (email.equals("")) {
            messagePlaceholder.setText(R.string.no_email);
            return false;
        }

        return true;
    }

    private void registerToServer(String password, final String email, String nickName, final View view) {
        final String newNickName;

        // get birthDate string from button
        Button birthDate = view.findViewById(R.id.birthdate);
        final String birthDateString = birthDate.getText().toString();


        if(nickName.equals("")) {
            newNickName = email;
        } else {
            newNickName = nickName;
        }

        new RegisterToServer(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                // in case of success, create new user instance and go to map
                if (output.equals("Registration succeeded")) {

                    // after successful registration, save username and nickname of current user
                    UserProfile.user.setUsername(email);
                    UserProfile.user.setNickName(newNickName);
                    UserProfile.user.setBirthDate(birthDateString);

                    // initial position of user in UserProfile and server
                    initPosition();

                    // go to map
                    Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
                    startActivity(mapIntent);

                } else {

                    // print failed registration output message
                    messagePlaceholder.setText(output);
                }
            }
        }).execute(password, email, birthDateString, newNickName);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    private void initPosition(){
        UserProfile.user.setCurrentLessonID(1);
        UserProfile.user.setTopLessonID(1);
        UserProfile.user.setCurrentCourseID(1);
        UserProfile.user.setTopCourseID(1);

        new UpdatePositionInServer().execute();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Button birthDate = view.findViewById(R.id.birthdate);
        birthDate.setText(sdf.format(myCalendar.getTime()));
    }

//    private void initProject(){
//        UserProfile.user.setJava("");
//        UserProfile.user.set
//    }
}