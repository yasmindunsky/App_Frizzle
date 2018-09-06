package com.frizzl.app.frizzleapp;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Noga on 04/09/2018.
 */

public class AnnotationUserCreatedViewType {
    public static final String TEXT_VIEW = "TextView";
    public static final String BUTTON = "Button";
    public static final String IMAGE_VIEW = "ImageView";

    public AnnotationUserCreatedViewType(@UserCreatedViewType String userCreatedViewType) {
        System.out.println("UserCreatedViewType :" + userCreatedViewType);
    }

    @StringDef({TEXT_VIEW, BUTTON, IMAGE_VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserCreatedViewType {
    }

    public static void main(String[] args) {
        AnnotationUserCreatedViewType annotationUserCreatedViewType = new AnnotationUserCreatedViewType(TEXT_VIEW);
    }
}
