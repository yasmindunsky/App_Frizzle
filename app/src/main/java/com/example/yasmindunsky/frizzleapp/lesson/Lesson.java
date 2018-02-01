package com.example.yasmindunsky.frizzleapp.lesson;

import java.util.ArrayList;

import com.example.yasmindunsky.frizzleapp.lesson.exercise.Exercise;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Lesson {

    private int lessonID;
    private ArrayList<Slide> lessonSlides;
    private ArrayList<Exercise> lessonExercise;
    private Task task;
    private boolean inSlides = true;

    public Lesson(int lessonID) {
        this.lessonID = lessonID;
    }

    public int getID() {
        return lessonID;
    }

    public boolean isInSlides() {
        return inSlides;
    }

    public void setInSlides(boolean inSlides) {
        this.inSlides = inSlides;
    }

    public ArrayList<Slide> getLessonSlides() {
        return lessonSlides;
    }

    public int getSlidesNumber() {
        return lessonSlides.size();
    }

    public void setSlides(ArrayList<Slide> lessonSlides) {
        this.lessonSlides = lessonSlides;
    }

    public ArrayList<Exercise> getExercises() {
        return lessonExercise;
    }

    public void setExercises(ArrayList<Exercise> lessonExercise) {
        this.lessonExercise = lessonExercise;
    }

    public int getExercisesNumber() {
        return lessonExercise.size();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
