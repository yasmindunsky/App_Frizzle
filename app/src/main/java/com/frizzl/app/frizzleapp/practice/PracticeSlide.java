package com.frizzl.app.frizzleapp.practice;

import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.Design;

import java.io.Serializable;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public class PracticeSlide implements Serializable{
    private String infoText;
    private String reminderText;
    private String taskText;
    private Code code;
    private Design design;
    private String callToActionText;

    public PracticeSlide(String infoText, String reminderText, String taskText, Code code, String callToActionText, Design design) {
        this.infoText = infoText;
        this.reminderText = reminderText;
        this.taskText = taskText;
        this.code = code;
        this.callToActionText = callToActionText;
        this.design = design;
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
        return design != null;
    }

    public boolean hasCode() {
        return code != null;
    }

    public Code getCode() {
        return code;
    }

    public Design getDesign() {
        return design;
    }
}