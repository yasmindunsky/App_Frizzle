package com.frizzl.app.frizzleapp.notifications;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Noga on 21/06/2018.
 */
public class NotificationUtilsTest {
    @Test
    public void remindUser() throws Exception {
    }

    @Test
    public void createBeautifulFraction() throws Exception {
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(1,8), "1/8");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(2,8), "1/4");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(3,8), "3/8");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(4,8), "1/2");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(5,8), "5/8");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(6,8), "3/4");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(7,8), "7/8");
        Assert.assertEquals(NotificationUtils.createBeautifulFraction(8,8), "1/1");
    }

    @Test
    public void generateNotificationText() throws Exception {
//        NotificationUtils.generateNotificationText();
//        NotificationUtils.generateNotificationText();
//        NotificationUtils.generateNotificationText();
//        NotificationUtils.generateNotificationText();
//        NotificationUtils.generateNotificationText();
//        NotificationUtils.generateNotificationText();
    }

}