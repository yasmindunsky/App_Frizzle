package com.frizzl.app.frizzleapp.appBuilder;

import android.support.test.rule.ActivityTestRule;

import com.frizzl.app.frizzleapp.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Noga on 17/07/2018.
 */
public class AppBuilderActivityTest {
    @Rule
    public ActivityTestRule<AppBuilderActivity> mActivityRule =
            new ActivityTestRule<>(AppBuilderActivity.class);

    @Test
    public void onChooseColorWithoutColorChosenExpectedBehaviour() {
        onView(withText("NEW BUTTON")).perform(click());
        onView(withId(R.id.viewFontColorValue)).perform(click());
        onView(withText("OK")).perform(click());
    }

}