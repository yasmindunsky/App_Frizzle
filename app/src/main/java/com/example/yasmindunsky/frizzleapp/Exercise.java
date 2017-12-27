package com.example.yasmindunsky.frizzleapp;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Exercise {

    private String type;
    private String question;
    private String image;
    private ArrayList<String> possibilities;
    private ArrayList<String> answers;

    public Exercise(String type, String question, String image, ArrayList<String> possibilities, ArrayList<String> answers) {
        this.type = type;
        this.question = question;
        this.image = image;
        this.possibilities = possibilities;
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<String> getPossibilities() {
        return possibilities;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
