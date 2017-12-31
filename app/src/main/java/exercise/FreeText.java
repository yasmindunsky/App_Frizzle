package exercise;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.yasmindunsky.frizzleapp.R;

import java.util.ArrayList;

/**
 * Created by yasmin.dunsky on 29-Dec-17.
 */

public class FreeText extends Exercise {
    public FreeText(String type, String question, String imageSource, ArrayList<String> content, ArrayList<String> possibilities, ArrayList<String> answers) {
        super(type, question, imageSource, content, possibilities, answers);
    }

    public void createLayout(LinearLayout view, Context context){
        EditText inputText = new EditText(context);
        inputText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        inputText.setId(R.id.userAnswerInput);
        view.addView(inputText);
    }

    public boolean isCorrect(View view) {
        EditText freeTextAnswer = view.findViewById(R.id.userAnswerInput);
        String userFreeTextAnswer = freeTextAnswer.getText().toString();
        return this.getAnswers().contains(userFreeTextAnswer);
    }


}
