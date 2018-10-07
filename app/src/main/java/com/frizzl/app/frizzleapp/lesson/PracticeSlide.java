package com.frizzl.app.frizzleapp.lesson;

import com.frizzl.app.frizzleapp.Code;

import java.io.Serializable;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class PracticeSlide implements Serializable{
    private String infoText;
    private String reminderText;
    private String taskText;
    private Code code;
    private String callToActionText;
    private boolean hasDesign;

    public PracticeSlide(String infoText, String reminderText, String taskText, Code code, String callToActionText, boolean hasDesign) {
        this.infoText = infoText;
        this.reminderText = reminderText;
        this.taskText = taskText;
        this.code = code;
        this.callToActionText = callToActionText;
        this.hasDesign = hasDesign;
    }

    public boolean hasInfoText() {
        return infoText != null;
    }

    public String getInfoText() {
        return infoText;
    }

    public String getReminderText() {
        return reminderText;
    }

    public boolean hasReminderText() {
        return reminderText != null;
    }

    public String getTaskText() {
        return taskText;
    }

    public boolean hasTaskText() {
        return taskText != null;
    }

    public String getCallToActionText() {
        return callToActionText;
    }

    public boolean hasDesign() {
        return hasDesign;
    }

    public boolean hasCode() {
        return code != null;
    }

    public Code getCode() {
        return code;
    }
}