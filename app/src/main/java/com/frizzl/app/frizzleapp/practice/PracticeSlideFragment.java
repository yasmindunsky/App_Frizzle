package com.frizzl.app.frizzleapp.practice;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import com.airbnb.lottie.LottieAnimationView;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.CodeSection;
import com.frizzl.app.frizzleapp.FrizzlApplication;
import com.frizzl.app.frizzleapp.PracticeViewPager;
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
    private PracticeViewPager viewPager;
    private com.airbnb.lottie.LottieAnimationView lottieAnimationView;
    private AppCompatTextView errorTextView;
    private int currentLevel;
    private int currentSlide;
    private String originalCode;

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
        viewPager = getActivity().findViewById(R.id.pager);
        currentLevel = UserProfile.user.getCurrentLevel();
        currentSlide = getCurrentSlide();

        if (Support.isRTL()) {
            view.setRotationY(180);
        }

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

        if (practiceSlide.hasInfoText()) {
            int infoTextStyle = R.style.Text_PracticeSlide_infoText;
            AppCompatTextView infoText = new AppCompatTextView(new ContextThemeWrapper(context, infoTextStyle));
            infoText.setId(R.id.infoText);

            SpannableStringBuilder spannableInfoText = new SpannableStringBuilder(practiceSlide.getInfoText());
            spannableInfoText = Support.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true);
            spannableInfoText = Support.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            infoText.setText(spannableInfoText);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            prevId = infoText.getId();
            infoText.setLayoutParams(layoutParams);
            constraintLayout.addView(infoText);
        }

        if (practiceSlide.hasTaskText()) {
            int taskTextStyle = R.style.Text_PracticeSlide_taskText;
            AppCompatTextView taskText = new AppCompatTextView(new ContextThemeWrapper(context, taskTextStyle));
            taskText.setId(R.id.taskText);

            SpannableStringBuilder spannableTaskText = new SpannableStringBuilder(practiceSlide.getTaskText());
            spannableTaskText = Support.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true);
            spannableTaskText = Support.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            taskText.setText(spannableTaskText);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
            taskText.setLayoutParams(layoutParams);
            layoutParams.topToBottom = prevId;
            layoutParams.startToStart = constraintLayoutId;
            prevId = taskText.getId();
            constraintLayout.addView(taskText);
        }

        if (practiceSlide.hasReminderText()) {
            int reminderTextStyle = R.style.Text_PracticeSlide_reminderText;
            AppCompatTextView reminderText = new AppCompatTextView(new ContextThemeWrapper(context, reminderTextStyle));
            reminderText.setId(R.id.reminderText);
            reminderText.setText(practiceSlide.getReminderText());
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
            layoutParams.topToBottom = prevId;
            layoutParams.startToStart = constraintLayoutId;
            prevId = reminderText.getId();
            reminderText.setLayoutParams(layoutParams);
            constraintLayout.addView(reminderText);
        }

        CodeKeyboard codeKeyboard = null;
        if (practiceSlide.hasCode()) {
            Code code = practiceSlide.getCode();
            waitForCTA = code.getWaitForCTA();
            boolean mutable = code.getMutable();
            if (mutable) {
                codeKeyboard = new CodeKeyboard(context);
                codeKeyboard.setId(R.id.codeKeyboard);
                codeKeyboard.setOrientation(LinearLayout.VERTICAL);
                ConstraintLayout.LayoutParams keyboardLayoutParams = new
                        ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                keyboardLayoutParams.setMargins(0, TOP_DOWN_MARGIN, 0, 0);
                keyboardLayoutParams.bottomToBottom = constraintLayoutId;
                codeKeyboard.setLayoutParams(keyboardLayoutParams);
            }
            originalCode = code.getCode();
            CodeSection codeSection = new CodeSection(context, originalCode, code.getRunnable(), mutable, code.getWaitForCTA(), codeKeyboard);
            codeSection.setId(R.id.codeSection);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
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
            String onClickFunction = design.getOnClickFunction();
            DesignSection designSection = new DesignSection(context, runnable, withOnClickSet, onClickFunction, getActivity());
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
                    if (((Activity) context).isFinishing()) return;
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    ImageButton closeButton = popupView.findViewById(R.id.close);
                    closeButton.setOnClickListener(v -> popupWindow.dismiss());
                }
            });
        }

        // Add error place
        int errorTextStyle = R.style.Text_PracticeSlide_error;
        errorTextView = new AppCompatTextView(new ContextThemeWrapper(context, errorTextStyle));
        errorTextView.setId(R.id.errorTextView);
        ConstraintLayout.LayoutParams errorLayoutParams =
                new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        errorLayoutParams.setMargins(SIDES_MARGIN, TOP_DOWN_MARGIN, SIDES_MARGIN, TOP_DOWN_MARGIN);
        errorLayoutParams.topToBottom = prevId;
        errorTextView.setVisibility(View.GONE);
        errorTextView.setLayoutParams(errorLayoutParams);
        prevId = errorTextView.getId();
        constraintLayout.addView(errorTextView);

        // Add CTA button.
        int ctaButtonStyle = R.style.Button_PracticeCTA;
        callToActionButton = new AppCompatButton(new ContextThemeWrapper(context, ctaButtonStyle));
        callToActionButton.setText(practiceSlide.getCallToActionText());
        callToActionButton.setBackground(getResources().getDrawable(R.drawable.check_button_background));
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, TOP_DOWN_MARGIN * 2, 0, TOP_DOWN_MARGIN * 4);
        layoutParams.topToBottom = prevId;
        layoutParams.startToStart = constraintLayoutId;
        layoutParams.endToEnd = constraintLayoutId;
        callToActionButton.setLayoutParams(layoutParams);
        callToActionButton.setOnClickListener(v -> {
            Button button = (Button) v;
            boolean moveOn = true;
            String buttonText = button.getText().toString().trim();
            final String checkText = getActivity().getApplicationContext().getResources().getString(R.string.check);
            final String tryAgainText = getActivity().getApplicationContext().getResources().getString(R.string.try_again);
            final String gotItText = getActivity().getApplicationContext().getResources().getString(R.string.got_it);
            final String nextText = getActivity().getApplicationContext().getResources().getString(R.string.next);
            if (buttonText.equals(gotItText) || buttonText.equals(nextText)) {
                moveToNextFragment();
            }
            if (buttonText.equals(checkText) || buttonText.equals(tryAgainText)) {
                if (practiceCorrect()) {
                    changeButtonToGreenAndShowAnimation(button);
                } else {
                    // Special case of checking design and not code. Wrong result from
                    // practiceCorrect is enough here, no additional test required.
                    if (currentLevel == Support.ONCLICK_PRACTICE_LEVEL_ID  &&
                            getCurrentSlide() == 9){
                        presentError(FrizzlApplication.resources.getString(R.string.error_set_onclick));
                    }
                    // Checking code.
                    else {
                        String error = PracticeErrorManager.check(currentLevel, getCurrentSlide(),
                                getOriginalCode(), getCurrentCode());
                        presentError(error);
                        if (buttonText.equals(tryAgainText)) button.setText(R.string.next);
                        else {
                            button.setText(tryAgainText);
                        }
                    }
                }
            }
        });
        constraintLayout.addView(callToActionButton);
        lottieAnimationView = new LottieAnimationView(getContext());
        lottieAnimationView.setAnimation(R.raw.stars);
        lottieAnimationView.setSpeed(1.5f);
        layoutParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, TOP_DOWN_MARGIN * 2, 0, TOP_DOWN_MARGIN * 4);
        int CTAid = callToActionButton.getId();
        layoutParams.topToTop = CTAid;
        layoutParams.bottomToBottom = CTAid;
        layoutParams.startToStart = constraintLayoutId;
        layoutParams.endToEnd = constraintLayoutId;
        lottieAnimationView.setLayoutParams(layoutParams);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveToNextFragment();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        constraintLayout.addView(lottieAnimationView);

        if (codeKeyboard != null) {
            constraintLayout.addView(codeKeyboard);
        }

        if (waitForCTA) enableCTAButton(false);

        return view;
    }

    private void presentError(String error) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(error);
    }

    private void changeButtonToGreenAndShowAnimation(Button button) {
                button.setBackground(getResources().getDrawable(R.drawable.button_background_green));
                lottieAnimationView.playAnimation();
    }

    private boolean practiceCorrect() {
        boolean correct = true;

        // What slide are we at
        int currentSlide = getCurrentSlide();

        int currentLevel = UserProfile.user.getCurrentLevel();
        if (currentLevel == Support.SPEAKOUT_PRACTICE_LEVEL_ID) {
            if (currentSlide == 1) {
                // Check if speakOut was added and written 'this is so cool'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "Hello", false);
            } else if (currentSlide == 2) {
                // Check if speakOut was added and written 'this is so cool'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "this is so cool", true);
            }
        } else if (currentLevel == Support.ONCLICK_PRACTICE_LEVEL_ID){
            if (currentSlide == 5){
                // Check if new function was added.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = CodeCheckUtils.checkIfContainsFunctionWithName(code, "");
            }
            else if (currentSlide == 7){
                // Check function name changed to 'myFunction'.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = CodeCheckUtils.checkIfContainsFunctionWithName(code, "myFunction");
            }
            else if (currentSlide == 8){
                // Check if speakOut was written inside function.
                CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
                String code = codeSection.getCode();
                correct = CodeCheckUtils.checkIfContainsSpeakOutAndString(code, "Hello", true);
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
        return viewPager.getCurrentItem();
    }


    private void moveToNextFragment() {
        UserProfile.user.finishedCurrentSlideInLevel();
        // Change to next fragment onClick
        FragmentActivity activity = getActivity();
        if (activity != null) {
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

    public String getOriginalCode() {
        if (originalCode == null) return "";
        return originalCode;
    }

    public String getCurrentCode() {
        CodeSection codeSection = constraintLayout.findViewById(R.id.codeSection);
        if (codeSection == null) return null;
        return codeSection.getCode();
    }
}
