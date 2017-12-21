package com.example.yasmindunsky.frizzleapp;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Theory {
    private String lessonText;
    private String imageSource;

    public Theory(String lessonText, String imageSource) {
        this.lessonText = lessonText;
        this.imageSource = imageSource;
    }

    public String getLessonText() {
        return lessonText;
    }

    public void setLessonText(String lessonText) {
        this.lessonText = lessonText;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

}
