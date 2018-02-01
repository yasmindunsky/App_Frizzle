package com.example.yasmindunsky.frizzleapp.lesson;

/**
 * Created by yasmin.dunsky on 01-Feb-18.
 */
public class Task {
    private String text;

    public Task(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
