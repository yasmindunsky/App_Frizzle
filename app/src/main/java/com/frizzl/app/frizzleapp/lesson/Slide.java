package com.frizzl.app.frizzleapp.lesson;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Slide {
    private String slideText;
    private String imageSource;

    public Slide(String slideText, String imageSource) {
        this.slideText = slideText;
        this.imageSource = imageSource;
    }

    public String getSlideText() {
        return slideText;
    }

    public String getImageSource() {
        return imageSource;
    }

}
