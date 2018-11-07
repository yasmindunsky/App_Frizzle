package com.frizzl.app.frizzleapp.practice;


import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.airbnb.lottie.LottieAnimationView;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.Code;
import com.frizzl.app.frizzleapp.CodeCheckUtils;
import com.frizzl.app.frizzleapp.CodeKeyboard;
import com.frizzl.app.frizzleapp.CodeSection;
import com.frizzl.app.frizzleapp.FrizzlApplication;
import com.frizzl.app.frizzleapp.Design;
import com.frizzl.app.frizzleapp.DesignSection;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Utils;
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
    private PracticeNotificationView errorView;
    private PracticeNotificationView notificationView;
    private int currentLevel;
    private int currentSlide;
    private String originalCode;
    private int lastIDbeforeCTA;

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

        if (Utils.isRTL()) {
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
        int prevID = holder.getId();
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        if (practiceSlide.hasInfoText()) {
            int infoTextStyle = R.style.Text_PracticeSlide_infoText;
            AppCompatTextView infoText = new AppCompatTextView(new ContextThemeWrapper(context, infoTextStyle));
            infoText.setId(R.id.infoText);

            SpannableStringBuilder spannableInfoText = new SpannableStringBuilder(practiceSlide.getInfoText());
            spannableInfoText = Utils.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true);
            spannableInfoText = Utils.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            infoText.setText(spannableInfoText);

            constraintLayout.addView(infoText);
            setConstraints(set, infoText.getId(), prevID, SIDES_MARGIN);
            prevID = infoText.getId();
        }

        if (practiceSlide.hasTaskText()) {
            int taskTextStyle = R.style.Text_PracticeSlide_taskText;
            AppCompatTextView taskText = new AppCompatTextView(new ContextThemeWrapper(context, taskTextStyle));
            taskText.setId(R.id.taskText);

            SpannableStringBuilder spannableTaskText = new SpannableStringBuilder(practiceSlide.getTaskText());
            spannableTaskText = Utils.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true);
            spannableTaskText = Utils.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true);
            taskText.setText(spannableTaskText);

            constraintLayout.addView(taskText);
            setConstraints(set, taskText.getId(), prevID, SIDES_MARGIN);
            prevID = taskText.getId();
        }

        if (practiceSlide.hasReminderText()) {
            int reminderTextStyle = R.style.Text_PracticeSlide_reminderText;
            AppCompatTextView reminderText = new AppCompatTextView(new ContextThemeWrapper(context, reminderTextStyle));
            reminderText.setId(R.id.reminderText);
            reminderText.setText(practiceSlide.getReminderText());

            constraintLayout.addView(reminderText);
            setConstraints(set, reminderText.getId(), prevID, SIDES_MARGIN);
            prevID = reminderText.getId();

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
            CodeSection codeSection = new CodeSection(context, originalCode, code.getRunnable(), mutable, code.getWaitForCTA(), codeKeyboard, ((PracticeActivity)getActivity()).getMainLayout());
            if (!mutable) {
                codeSection.setEditorOnClickListener(v ->
                        presentNotification(context, "Here we'll write code after we'll learn how.", set));
            }
            codeSection.setId(R.id.codeSection);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            codeSection.setLayoutParams(layoutParams);
            constraintLayout.addView(codeSection);
            setConstraints(set, codeSection.getId(), prevID, SIDES_MARGIN);
            prevID = codeSection.getId();
            codeSection.setReadyForCTAListener(() -> enableCTAButton(true));
            codeSection.setPresentNotificationListener((notification) ->
                    presentNotification(context, notification, set));
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
            designSection.setLayoutParams(layoutParams);

            constraintLayout.addView(designSection);
            setConstraints(set, designSection.getId(), prevID, SIDES_MARGIN);
            prevID = designSection.getId();

            designSection.setDisplayNotificationListener((PopupWindow popupWindow) -> {
                if (UserProfile.user.getCurrentLevel() == Utils.ONCLICK_PRACTICE_LEVEL_ID
                        && getCurrentSlide() == 1) {
                    popupWindow.dismiss();
                    presentNotification(context, context.getResources().getString(R.string.our_button_does_nothing) , set);
                }
            });
        }

        // Add CTA button.
        int ctaButtonStyle = R.style.Button_PracticeCTA;
        callToActionButton = new AppCompatButton(new ContextThemeWrapper(context, ctaButtonStyle));
        callToActionButton.setText(practiceSlide.getCallToActionText());
        callToActionButton.setBackground(getResources().getDrawable(R.drawable.check_button_background));
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
                    if (currentLevel == Utils.ONCLICK_PRACTICE_LEVEL_ID  &&
                            getCurrentSlide() == 9){
                        presentError(context, FrizzlApplication.resources.getString(R.string.error_set_onclick), set);
                    }
                    // Checking code.
                    else {
                        String error = PracticeErrorManager.check(currentLevel, getCurrentSlide(),
                                getOriginalCode(), getCurrentCode());
                        presentError(context, error, set);
                        if (buttonText.equals(tryAgainText)) button.setText(R.string.next);
                        else {
                            button.setBackground(getResources().getDrawable(R.drawable.button_background_pink));
                            button.setText(tryAgainText);
                        }
                    }
                }
            }
        });
        constraintLayout.addView(callToActionButton);
        setConstraints(set, callToActionButton.getId(), prevID, SIDES_MARGIN * 4);

        lottieAnimationView = new LottieAnimationView(getContext());
        lottieAnimationView.setId(R.id.animation);
        lottieAnimationView.setAnimation(R.raw.stars);
        lottieAnimationView.setSpeed(1.6f);

        lastIDbeforeCTA = prevID;
        int CTAid = callToActionButton.getId();
        int thisID = lottieAnimationView.getId();
        set.constrainWidth(thisID, ConstraintSet.MATCH_CONSTRAINT);
        set.constrainHeight(thisID, ConstraintSet.WRAP_CONTENT);
        set.connect(thisID, ConstraintSet.START, CTAid, ConstraintSet.START, SIDES_MARGIN * 2);
        set.connect(thisID, ConstraintSet.END, CTAid, ConstraintSet.END, SIDES_MARGIN * 2);
        set.connect(thisID, ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
        constraintLayout.setConstraintSet(set);

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

    private void setConstraints(ConstraintSet set, int thisID, int prevID, int sidesMargins) {
        set.constrainWidth(thisID, ConstraintSet.MATCH_CONSTRAINT);
        set.constrainHeight(thisID, ConstraintSet.WRAP_CONTENT);
        set.connect(thisID, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, sidesMargins);
        set.connect(thisID, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, sidesMargins);
        set.connect(thisID, ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
        constraintLayout.setConstraintSet(set);
    }

    private void presentError(Context context, String error, ConstraintSet set) {
        // Add error place
        if (errorView == null) {
            errorView = new PracticeNotificationView(context, true);
            errorView.setId(R.id.errorView);
            constraintLayout.addView(errorView);
            int thisID = errorView.getId();
            set.constrainWidth(thisID, ConstraintSet.MATCH_CONSTRAINT);
            set.constrainHeight(thisID, ConstraintSet.WRAP_CONTENT);
            set.connect(thisID, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, SIDES_MARGIN);
            set.connect(thisID, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, SIDES_MARGIN);
            set.connect(callToActionButton.getId(), ConstraintSet.TOP, thisID, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
            set.connect(thisID, ConstraintSet.TOP, lastIDbeforeCTA, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
            constraintLayout.setConstraintSet(set);
        }
        errorView.setText(error);
    }

    private void presentNotification(Context context, String notification, ConstraintSet set) {
        // Add error place
        if (notificationView == null) {
            notificationView = new PracticeNotificationView(context, false);
            notificationView.setId(R.id.notificationView);
            constraintLayout.addView(notificationView);
            int thisID = notificationView.getId();
            set.constrainWidth(thisID, ConstraintSet.MATCH_CONSTRAINT);
            set.constrainHeight(thisID, ConstraintSet.WRAP_CONTENT);
            set.connect(thisID, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, SIDES_MARGIN);
            set.connect(thisID, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, SIDES_MARGIN);
            set.connect(callToActionButton.getId(), ConstraintSet.TOP, thisID, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
            set.connect(thisID, ConstraintSet.TOP, lastIDbeforeCTA, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
            constraintLayout.setConstraintSet(set);
        }
        notificationView.setText(notification);
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
        if (currentLevel == Utils.SPEAKOUT_PRACTICE_LEVEL_ID) {
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
        } else if (currentLevel == Utils.ONCLICK_PRACTICE_LEVEL_ID){
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
