package com.frizzl.app.frizzleapp.practice;


import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroSlideFragment extends Fragment {
    private int index;
    private int levelID;
    private PracticeSlide practiceSlide;
    private static final int SIDES_MARGIN = 50;
    private static final int TOP_DOWN_MARGIN = 25;
    private AppCompatButton callToActionButton;
    private TextToSpeech tts;
    private int numOfSlides;
    private boolean waitForCTA = false;
    private ConstraintLayout constraintLayout;

    public IntroSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practice_slide, container, false);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        constraintLayout.setFocusableInTouchMode(true);
        int constraintLayoutId = constraintLayout.getId();

        if (ViewUtils.isRTL()) {
            view.setRotationY(180);
        }

        TextToSpeech.OnInitListener onInitListener = status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported");
                }
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        };
        tts = new TextToSpeech(getContext(), onInitListener, "com.google.android.tts");
//        Voice voice = new Voice();
//        tts.setVoice(voice);

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
            levelID = bundle.getInt("lesson");
            numOfSlides = bundle.getInt("numOfSlides");
            practiceSlide = (PracticeSlide) bundle.getSerializable("intro_slide");
        }

        // Create elements by what's needed
        Context context = getContext();
        Button holder = constraintLayout.findViewById(R.id.holder);
        int prevID = holder.getId();
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        if (practiceSlide.hasInfoText()){
            int infoTextStyle = R.style.Text_PracticeSlide_infoText;
            AppCompatTextView infoText = new AppCompatTextView(new ContextThemeWrapper(context, infoTextStyle));
            infoText.setId(R.id.infoText);

            SpannableStringBuilder spannableInfoText = new SpannableStringBuilder(practiceSlide.getInfoText());
            spannableInfoText = ViewUtils.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true, true, context);
            spannableInfoText = ViewUtils.markWithColorBetweenTwoSymbols(spannableInfoText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true, true, context);
            infoText.setText(spannableInfoText);

            constraintLayout.addView(infoText);
            setConstraints(set, infoText.getId(), prevID, SIDES_MARGIN);
            prevID = infoText.getId();
        }

        if (practiceSlide.hasTaskText()){
            int taskTextStyle = R.style.Text_PracticeSlide_taskText;
            AppCompatTextView taskText = new AppCompatTextView(new ContextThemeWrapper(context, taskTextStyle));
            taskText.setId(R.id.taskText);

            SpannableStringBuilder spannableTaskText = new SpannableStringBuilder(practiceSlide.getTaskText());
            spannableTaskText = ViewUtils.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$green$", getResources().getColor(R.color.frizzle_green), true, false, context);
            spannableTaskText = ViewUtils.markWithColorBetweenTwoSymbols(spannableTaskText,
                    "$orange$", getResources().getColor(R.color.frizzle_orange), true, false, context);
            taskText.setText(spannableTaskText);
            constraintLayout.addView(taskText);
            setConstraints(set, taskText.getId(), prevID, SIDES_MARGIN);
            prevID = taskText.getId();

        }

        View.OnClickListener speak = v -> {
            if (ViewUtils.volumeIsLow(context)) ViewUtils.presentVolumeToast(context);
            String textToSay = (String) v.getTag();
            tts.speak(textToSay, TextToSpeech.QUEUE_ADD, null);
            enableCTAButton(true);
        };

        if (practiceSlide.hasDesign()){
            waitForCTA = practiceSlide.getDesign().getRunnable();
            View pollyApp = getLayoutInflater().inflate(R.layout.polly_app_demo, null);
            AppCompatButton nameButton = pollyApp.findViewById(R.id.nameButton);
            AppCompatButton jokeButton = pollyApp.findViewById(R.id.jokeButton);
            nameButton.setOnClickListener(speak);
            jokeButton.setOnClickListener(speak);
            constraintLayout.addView(pollyApp);
            setConstraints(set, pollyApp.getId(), prevID, SIDES_MARGIN*2);
            prevID = pollyApp.getId();

        }

        int ctaButtonStyle = R.style.Button_PracticeCTA;
        callToActionButton = new AppCompatButton(new ContextThemeWrapper(context, ctaButtonStyle));
        callToActionButton.setText(practiceSlide.getCallToActionText());
        callToActionButton.setBackground(getResources().getDrawable(R.drawable.check_button_background));
        callToActionButton.setOnClickListener(v -> {
            Button button = (Button) v;
            UserProfile.user.finishedCurrentSlideInLevel();
            moveToNextFragment();
        });
        constraintLayout.addView(callToActionButton);
        setConstraints(set, callToActionButton.getId(), prevID, SIDES_MARGIN*4);

        if (waitForCTA) enableCTAButton(false);

        return view;
    }

    public void enableCTAButton(boolean enabled){
        int alpha = enabled ? 255 : 220;
        if (callToActionButton != null){
            callToActionButton.getBackground().setAlpha(alpha);
            callToActionButton.setEnabled(enabled);
        }
    }

    private void moveToNextFragment() {
        // Change to next fragment onClick
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ViewPager viewPager = activity.findViewById(R.id.pager);
            int i = viewPager.getCurrentItem() + 1;

            // If last slide
            if (index == numOfSlides - 1){
                UserProfile.user.finishedPractice(levelID);
                ((IntroActivity)getActivity()).goBack();
            }
            viewPager.setCurrentItem(i);
            RoundCornerProgressBar progressBar = activity.findViewById(R.id.progressBar);
            progressBar.setProgress(i);
        }
    }

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        if(tts != null) {
            tts.stop();
            tts.shutdown();
            Log.d("tts", "TTS Destroyed");
        }
        super.onDestroy();
    }

    private void setConstraints(ConstraintSet set, int thisID, int prevID, int sidesMargins) {
        set.constrainWidth(thisID, ConstraintSet.MATCH_CONSTRAINT);
        set.constrainHeight(thisID, ConstraintSet.WRAP_CONTENT);
        set.connect(thisID, ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, sidesMargins);
        set.connect(thisID, ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, sidesMargins);
        set.connect(thisID, ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, TOP_DOWN_MARGIN);
        constraintLayout.setConstraintSet(set);
    }
}
