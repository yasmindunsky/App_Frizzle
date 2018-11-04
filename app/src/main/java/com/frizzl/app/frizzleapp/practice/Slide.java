package com.frizzl.app.frizzleapp.practice;

import java.util.List;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Slide {
    private String slideText;
    private List<String> imageSources;

    public Slide(String slideText, List<String> imageSources) {
        this.slideText = slideText;
        this.imageSources = imageSources;
    }

    public String getSlideText() {
        return slideText;
    }

    public List<String> getImageSource() {
        return imageSources;
    }

}
