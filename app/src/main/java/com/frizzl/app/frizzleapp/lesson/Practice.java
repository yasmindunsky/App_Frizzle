package com.frizzl.app.frizzleapp.lesson;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Practice {

    private int practiceID;
    private ArrayList<PracticeSlide> practiceSlides;

    public Practice(int practiceID) {
        this.practiceID = practiceID;
    }

    public int getID() {
        return practiceID;
    }

    public ArrayList<PracticeSlide> getPracticeSlides() {
        return practiceSlides;
    }

    public int getNumOfSlides() {
        return practiceSlides.size();
    }

    public void setSlides(ArrayList<PracticeSlide> lessonSlides) {
        this.practiceSlides = lessonSlides;
    }
}
