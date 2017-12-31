package exercise;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 19-Nov-17.
 */

public abstract class Exercise {

    private String type;
    private String question;
    private String imageSource;
    private ArrayList<String> content;
    private ArrayList<String> possibilities;
    private ArrayList<String> answers;

    public Exercise(String type, String question, String imageSource, ArrayList<String> content,
                    ArrayList<String> possibilities, ArrayList<String> answers) {
        this.type = type;
        this.question = question;
        this.imageSource = imageSource;
        this.content = content;
        this.possibilities = possibilities;
        this.answers = answers;
    }

    public static Exercise createInstance(String type, String question, String image, ArrayList<String> content,
                                   ArrayList<String> possibilities, ArrayList<String> answers){
        switch (type){
            case "FreeText":
                return new FreeText(type, question, image, content, possibilities, answers);
            case "SingleResponse":
                return new SingleResponse(type, question, image, content, possibilities, answers);
            case "MultipleResponse":
                return new MultipleResponse(type, question, image, content, possibilities, answers);
            case "FillTheBlanks":
                return new FillTheBlanks(type, question, image, content, possibilities, answers);
            case "WordBank":
                return new WordBank(type, question, image, content, possibilities, answers);
        }

        return null;
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

    abstract public void createLayout(LinearLayout view, Context context);

    abstract public boolean isCorrect(View view);

}
