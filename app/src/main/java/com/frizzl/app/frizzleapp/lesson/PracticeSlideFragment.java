package com.frizzl.app.frizzleapp.lesson;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.Design;
import com.frizzl.app.frizzleapp.DesignSection;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.CodeSection;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;
import com.frizzl.app.frizzleapp.appBuilder.UserCreatedButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeSlideFragment extends Fragment {
    private int index;
    private int lessonNum;
    private PracticeSlide practiceSlide;
    private RelativeLayout relativeLayout;
    private static final int SIDES_MARGIN = 50;
    private static final int TOP_DOWN_MARGIN = 15;
    private AppCompatButton callToActionButton;
    private boolean waitForCTA = false;

    public PracticeSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice_slide, container, false);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        relativeLayout.setFocusableInTouchMode(true);

        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        lessonNum = bundle.getInt("lesson");
        practiceSlide = (PracticeSlide) bundle.getSerializable("practice_slide");

        // Create elements by what's needed
        Context context = getContext();
        int currentAddedViewID = R.id.holder;

        if (practiceSlide.hasInfoText()){
            int infoTextStyle = R.style.Text_PracticeSlide_infoText;
            AppCompatTextView infoText = new AppCompatTextView(new ContextThemeWrapper(context, infoTextStyle));
            infoText.setId(R.id.infoText);

            SpannableStringBuilder spannableInfoText = new SpannableStringBuilder(practiceSlide.getInfoText());
            spannableInfoText = Support.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$light_blue$", getResources().getColor(R.color.frizzle_light_blue), true);
            spannableInfoText = Support.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            infoText.setText(spannableInfoText);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.addRule(RelativeLayout.BELOW, currentAddedViewID);
            infoText.setLayoutParams(layoutParams);
            relativeLayout.addView(infoText);
            currentAddedViewID = infoText.getId();
        }

        if (practiceSlide.hasTaskText()){
            int taskTextStyle = R.style.Text_PracticeSlide_taskText;
            AppCompatTextView taskText = new AppCompatTextView(new ContextThemeWrapper(context, taskTextStyle));
            taskText.setId(R.id.taskText);
            taskText.setText(practiceSlide.getTaskText());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.addRule(RelativeLayout.BELOW, currentAddedViewID);
            taskText.setLayoutParams(layoutParams);
            relativeLayout.addView(taskText);
            currentAddedViewID = taskText.getId();
        }

        if (practiceSlide.hasReminderText()){
            int reminderTextStyle = R.style.Text_PracticeSlide_reminderText;
            AppCompatTextView reminderText = new AppCompatTextView(new ContextThemeWrapper(context, reminderTextStyle));
            reminderText.setId(R.id.reminderText);
            reminderText.setText(practiceSlide.getReminderText());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.addRule(RelativeLayout.BELOW, currentAddedViewID);
            reminderText.setLayoutParams(layoutParams);
            relativeLayout.addView(reminderText);
            currentAddedViewID = reminderText.getId();
        }

        CodeKeyboard codeKeyboard = null;
        if (practiceSlide.hasCode()){
            Code code = practiceSlide.getCode();
            waitForCTA = code.getWaitForCTA();
            boolean mutable = code.getMutable();
            if (mutable){
                codeKeyboard = new CodeKeyboard(context);
                codeKeyboard.setId(R.id.codeKeyboard);
                codeKeyboard.setOrientation(LinearLayout.VERTICAL);
                RelativeLayout.LayoutParams keyboardLayoutParams = new
                        RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                keyboardLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                keyboardLayoutParams.setMargins(0,TOP_DOWN_MARGIN,0,0);
                codeKeyboard.setLayoutParams(keyboardLayoutParams);
                relativeLayout.addView(codeKeyboard);
            }
            CodeSection codeSection = new CodeSection(context, code.getCode(), code.getRunnable(), mutable, code.getWaitForCTA(), codeKeyboard);
            codeSection.setId(R.id.codeSection);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.addRule(RelativeLayout.BELOW, currentAddedViewID);
            codeSection.setLayoutParams(layoutParams);
            relativeLayout.addView(codeSection);
            currentAddedViewID = codeSection.getId();

            codeSection.setReadyForCTAListener(() -> enableCTAButton(true));
        }

        if (practiceSlide.hasDesign()) {
            Design design = practiceSlide.getDesign();
            boolean runnable = design.getRunnable();
            DesignSection designSection = new DesignSection(context, runnable);
            designSection.setBackgroundLayout(relativeLayout);
            designSection.setId(R.id.designSection);
            designSection.setPadding(SIDES_MARGIN, SIDES_MARGIN, SIDES_MARGIN, SIDES_MARGIN);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            designSection.setLayoutParams(layoutParams);
            layoutParams.setMargins(SIDES_MARGIN, 0, SIDES_MARGIN, TOP_DOWN_MARGIN);
            layoutParams.addRule(RelativeLayout.BELOW, currentAddedViewID);
            designSection.setLayoutParams(layoutParams);
            relativeLayout.addView(designSection);
            currentAddedViewID = designSection.getId();
        }

        int ctaButtonStyle = R.style.Button_PracticeCTA;
        callToActionButton = new AppCompatButton(new ContextThemeWrapper(context, ctaButtonStyle));
        callToActionButton.setText(practiceSlide.getCallToActionText());
        callToActionButton.setBackground(getResources().getDrawable(R.drawable.check_button_background));
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (codeKeyboard != null) {
            layoutParams.addRule(RelativeLayout.ABOVE, codeKeyboard.getId());
        }
        else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0,TOP_DOWN_MARGIN,0,TOP_DOWN_MARGIN*4);
        callToActionButton.setLayoutParams(layoutParams);
        callToActionButton.setOnClickListener(v -> {
            Button button = (Button) v;
            boolean moveOn = true;
            String buttonText = button.getText().toString().trim();
            String checkText = getActivity().getApplicationContext().getResources().getString(R.string.check);
            String tryAgainText = getActivity().getApplicationContext().getResources().getString(R.string.try_again);
            if (buttonText.equals(checkText) || buttonText.equals(tryAgainText)){
                moveOn = checkPractice();
            }
            if (moveOn) {
                moveToNextFragment();
            }
            else {
                if (buttonText.equals(tryAgainText)) button.setText(R.string.next);
                else button.setText(tryAgainText);
            }
        });
        relativeLayout.addView(callToActionButton);
        if (waitForCTA) enableCTAButton(false);

        return view;
    }

    private boolean checkPractice() {
        boolean correct = true;

        // What slide are we at
        ViewPager viewPager = getActivity().findViewById(R.id.pager);
        int currentSlide = viewPager.getCurrentItem();

        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == Support.SPEAKOUT_PRACTICE_LEVEL_ID) {
            if (currentSlide == 2) {
                CodeSection codeSection = relativeLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsSpeakOutAndString(code, "this is so cool");
            }
        } else if (currentLevel == Support.ONCLICK_PRACTICE_LEVEL_ID){
            if (currentSlide == 1){
                CodeSection codeSection = relativeLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct &= checkIfContainsSpeakOutAndString(code, "goodbye");
            }
            if (currentSlide == 2){
                CodeSection codeSection = relativeLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct &= checkIfContainsFunctionWithName(code, "sayMyName");
                correct &= checkIfContainsSpeakOutAndString(code, "");

            }
            if (currentSlide == 3){
                DesignSection designSection = relativeLayout.findViewById(R.id.designSection);
                UserCreatedButton userCreatedButton = designSection.getUserCreatedButton();
                correct &= userCreatedButton.getOnClick().equals("sayHello");
            }
        }
        return correct;
    }

    private boolean checkIfContainsFunctionWithName(String code, String functionName) {
        boolean correct = true;
        correct &= code.contains(functionName.trim());
        // Contains 'speakOut'
        correct &= code.contains("public void");
        return correct;
    }

    private boolean checkIfContainsSpeakOutAndString(String code, String expectedString) {
        boolean correct = true;
        correct &= code.contains(expectedString.trim().toLowerCase());
        // Contains 'speakOut'
        correct &= code.contains("speakOut");
        // Contains '("'
        code = code.replaceAll("\\s+", "");
        correct &= code.contains("(\"");
        // Contains '");'
        correct &= code.contains("\");");
        return correct;
    }

    private void moveToNextFragment() {
        // Change to next fragment onClick
        ViewPager viewPager = getActivity().findViewById(R.id.pager);
        int i = viewPager.getCurrentItem() + 1;
        viewPager.setCurrentItem(i);
        RoundCornerProgressBar progressBar = getActivity().findViewById(R.id.progressBar);
        progressBar.setProgress(i);
    }

    public void enableCTAButton(boolean enabled){
        int alpha = enabled ? 255 : 220;
        if (callToActionButton != null){
            callToActionButton.getBackground().setAlpha(alpha);
            callToActionButton.setEnabled(enabled);
        }
    }
}
