package com.example.yasmindunsky.frizzleapp;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Lesson {

    private int lessonID;
    private ArrayList<Theory> lessonTheory;
    private ArrayList<Exercise> lessonExercise;

    //TODO enter to state object?
    private boolean inTheory = true;

    public Lesson(int lessonID) {
        this.lessonID = lessonID;
    }

    public int getLessonID() {
        return lessonID;
    }

    public boolean isInTheory() {
        return inTheory;
    }

    public void setInTheory(boolean inTheory) {
        this.inTheory = inTheory;
    }

    public ArrayList<Theory> getLessonTheory() {
        return lessonTheory;
    }

    public int getLessonTheoryPageNumber() {
        return lessonTheory.size();
    }

    public void setLessonTheory(ArrayList<Theory> lessonTheory) {
        this.lessonTheory = lessonTheory;
    }

    public ArrayList<Exercise> getLessonExercise() {
        return lessonExercise;
    }

    public void setLessonExercise(ArrayList<Exercise> lessonExercise) {
        this.lessonExercise = lessonExercise;
    }

    public int getLessonExercisePageNumber() {
        return lessonExercise.size();
    }
}
