package com.example.yasmindunsky.frizzleapp;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class Exercise {

    private String type;
    private String question;
    private String imageSource;
    private ArrayList<String> content;
    private ArrayList<String> possibilities;
    private ArrayList<String> answers;

    public Exercise(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        this.type = type;
        this.question = question;
        this.imageSource = imageSource;
        this.content = content;
        this.possibilities = possibilities;
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String getImageSource() {
        return imageSource;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public ArrayList<String> getPossibilities() {
        return possibilities;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
