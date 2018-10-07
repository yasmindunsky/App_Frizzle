package com.frizzl.app.frizzleapp;

import org.junit.Test;

/**
 * Created by Noga on 14/06/2018.
 */
public class UserProfileTest {
    @Test
    public void restartUserProfile_valid_assertRestarted() throws Exception {
        UserProfile.user.init();
        assertEquals(1, UserProfile.user.getTopLessonID());
        assertEquals(1, UserProfile.user.getTopCourseID());
        assertEquals(1, UserProfile.user.getCurrentLessonID());
        assertEquals(1, UserProfile.user.getCurrentCourseID());
        assertEquals("", UserProfile.user.getJava());
        assertEquals("", UserProfile.user.getXml());
    }

    @Test
    public void isLessonOpen_invalidLesson_false() throws Exception {
        UserProfile.user.setTopLessonID(2);
        assertEquals(false, UserProfile.user.isLessonOpen(6));
    }

    @Test
    public void isLessonOpen_validLesson_true() throws Exception {
        UserProfile.user.setTopLessonID(5);
        assertEquals(true, UserProfile.user.isLessonOpen(2));
    }

}