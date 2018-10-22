package com.frizzl.app.frizzleapp.lesson;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.CodeSection;
import com.frizzl.app.frizzleapp.Design;
import com.frizzl.app.frizzleapp.DesignSection;
import com.frizzl.app.frizzleapp.R;
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
    private ConstraintLayout constraintLayout;
    private static final int SIDES_MARGIN = 50;
    private static final int TOP_DOWN_MARGIN = 30;
    private AppCompatButton callToActionButton;
    private boolean waitForCTA = false;

    public PracticeSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice_slide, container, false);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        constraintLayout.setFocusableInTouchMode(true);
        int constraintLayoutId = constraintLayout.getId();

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
            lessonNum = bundle.getInt("lesson");
            practiceSlide = (PracticeSlide) bundle.getSerializable("practice_slide");
        }

        // Create elements by what's needed
        Context context = getContext();
        Button holder = constraintLayout.findViewById(R.id.holder);
        int prevId = holder.getId();

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

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            prevId = infoText.getId();
            infoText.setLayoutParams(layoutParams);
            constraintLayout.addView(infoText);
        }

        if (practiceSlide.hasTaskText()){
            int taskTextStyle = R.style.Text_PracticeSlide_taskText;
            AppCompatTextView taskText = new AppCompatTextView(new ContextThemeWrapper(context, taskTextStyle));
            taskText.setId(R.id.taskText);

            SpannableStringBuilder spannableTaskText = new SpannableStringBuilder(practiceSlide.getTaskText());
            spannableTaskText = Support.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$light_blue$", getResources().getColor(R.color.frizzle_light_blue), true);
            spannableTaskText = Support.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            taskText.setText(spannableTaskText);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            taskText.setLayoutParams(layoutParams);
            layoutParams.topToBottom = prevId;
            prevId = taskText.getId();
            constraintLayout.addView(taskText);
        }

        if (practiceSlide.hasReminderText()){
            int reminderTextStyle = R.style.Text_PracticeSlide_reminderText;
            AppCompatTextView reminderText = new AppCompatTextView(new ContextThemeWrapper(context, reminderTextStyle));
            reminderText.setId(R.id.reminderText);
            reminderText.setText(practiceSlide.getReminderText());
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            prevId = reminderText.getId();
            reminderText.setLayoutParams(layoutParams);
            constraintLayout.addView(reminderText);
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
                ConstraintLayout.LayoutParams keyboardLayoutParams = new
                        ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                keyboardLayoutParams.setMargins(0,TOP_DOWN_MARGIN,0,0);
                keyboardLayoutParams.bottomToBottom = constraintLayoutId;
                codeKeyboard.setLayoutParams(keyboardLayoutParams);
            }
            CodeSection codeSection = new CodeSection(context, code.getCode(), code.getRunnable(), mutable, code.getWaitForCTA(), codeKeyboard);
            codeSection.setId(R.id.codeSection);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN,TOP_DOWN_MARGIN,SIDES_MARGIN,TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            prevId = codeSection.getId();
            codeSection.setLayoutParams(layoutParams);
            constraintLayout.addView(codeSection);
            codeSection.setReadyForCTAListener(() -> enableCTAButton(true));
        }

        if (practiceSlide.hasDesign()) {
            Design design = practiceSlide.getDesign();
            boolean runnable = design.getRunnable();
            boolean withOnClickSet = design.getWithOnClickSet();
            DesignSection designSection = new DesignSection(context, runnable, withOnClickSet);
            designSection.setBackgroundLayout(constraintLayout);
            designSection.setId(R.id.designSection);
            designSection.setPadding(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            prevId = designSection.getId();
            designSection.setLayoutParams(layoutParams);
            constraintLayout.addView(designSection);
            designSection.setDisplayErrorListener(() -> {
                if (UserProfile.user.getCurrentLevel() == Support.ONCLICK_PRACTICE_LEVEL_ID
                        && getCurrentSlide() == 1) {
                    LayoutInflater errorInflater = (LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = errorInflater.inflate(R.layout.popup_code_section_run_error, null);
                    TextView errorText = popupView.findViewById(R.id.errorText);
                    errorText.setText(R.string.our_button_does_nothing);
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    if(((Activity) context).isFinishing()) return;
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    ImageButton closeButton = popupView.findViewById(R.id.close);
                    closeButton.setOnClickListener(v -> popupWindow.dismiss());
                }
            });
        }

        int ctaButtonStyle = R.style.Button_PracticeCTA;
        callToActionButton = new AppCompatButton(new ContextThemeWrapper(context, ctaButtonStyle));
        callToActionButton.setText(practiceSlide.getCallToActionText());
        callToActionButton.setBackground(getResources().getDrawable(R.drawable.check_button_background));
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,TOP_DOWN_MARGIN * 2,0,TOP_DOWN_MARGIN*4);
        layoutParams.topToBottom = prevId;
        layoutParams.startToStart = constraintLayoutId;
        layoutParams.endToEnd = constraintLayoutId;
        prevId = callToActionButton.getId();
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
        constraintLayout.addView(callToActionButton);

        if (codeKeyboard != null) {
            constraintLayout.addView(codeKeyboard);
        }

        if (waitForCTA) enableCTAButton(false);

        return view;
    }

    private boolean checkPractice() {
        boolean correct = true;

        // What slide are we at
        int currentSlide = getCurrentSlide();

        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == Support.SPEAKOUT_PRACTICE_LEVEL_ID) {
            if (currentSlide == 1) {
                // Check if speakOut was added and written 'this is so cool'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsSpeakOutAndString(code, "Hello", false);
            } else if (currentSlide == 2) {
                // Check if speakOut was added and written 'this is so cool'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsSpeakOutAndString(code, "this is so cool", true);
            }
        } else if (currentLevel == Support.ONCLICK_PRACTICE_LEVEL_ID){
            if (currentSlide == 5){
                // Check if new function was added.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsFunctionWithName(code, "");
            }
            else if (currentSlide == 7){
                // Check function name changed to 'myFunction'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsFunctionWithName(code, "myFunction");
            }
            else if (currentSlide == 8){
                // Check if speakOut was written inside function.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = checkIfContainsSpeakOutAndString(code, "Hello", true);
            }
            else if (currentSlide == 9){
                // Check if 'myFunction' was written as the onClick value.
                DesignSection designSection = constraintLayout.findViewById(R.id.designSection);
                UserCreatedButton userCreatedButton = designSection.getUserCreatedButton();
                correct = userCreatedButton.getOnClick().trim().equals("myFunction");
            }
        }
        return correct;
    }

    private int getCurrentSlide() {
        ViewPager viewPager = getActivity().findViewById(R.id.pager);
        return viewPager.getCurrentItem();
    }

    private boolean checkIfContainsFunctionWithName(String code, String functionName) {
        boolean correct = code.contains(functionName.trim());
        // Contains 'speakOut'
        correct &= code.contains("public void");
        return correct;
    }

    private boolean checkIfContainsSpeakOutAndString(String code, String string, boolean shouldContain) {
        boolean correct;
        string = string.trim().toLowerCase();
        boolean containsString = code.toLowerCase().contains(string);
        correct = (shouldContain == containsString);
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ViewPager viewPager = activity.findViewById(R.id.pager);
            int i = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(i);
            RoundCornerProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setProgress(i);
        }
    }

    public void enableCTAButton(boolean enabled){
        int alpha = enabled ? 255 : 220;
        if (callToActionButton != null){
            callToActionButton.getBackground().setAlpha(alpha);
            callToActionButton.setEnabled(enabled);
        }
    }
}
