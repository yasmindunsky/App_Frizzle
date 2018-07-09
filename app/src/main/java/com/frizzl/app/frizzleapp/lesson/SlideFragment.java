package com.frizzl.app.frizzleapp.lesson;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {
    TextView fragmentText;
    ImageView fragmentImage;
    AnimationDrawable animationDrawable;
    boolean isAnimated;
    private FirebaseAnalytics mFirebaseAnalytics;
    int index;
    int lessonNum;

    public SlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);

        // Rotation for RTL swiping.
        if (Support.isRTL()) {
            view.setRotationY(180);
        }

        fragmentText = view.findViewById(R.id.slideText);
        fragmentImage = view.findViewById(R.id.slideImage);

        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        lessonNum = bundle.getInt("lesson");
        Slide slide = LessonActivity.getCurrentLesson().getLessonSlides().get(index);
        String message = slide.getSlideText();
        fragmentText.setText(message);
        List<String> imageSources = slide.getImageSource();
        int index = 0;
        if (imageSources.size() > 1) {
            index = UserProfile.user.getCurrentAppTypeNum();
        }
        String imageSrc = imageSources.get(index);

        int imageIdentifier = getResources().getIdentifier(imageSrc , "drawable", getActivity().getPackageName());
        Drawable drawable = getResources().getDrawable(imageIdentifier);
        fragmentImage.setImageDrawable( drawable);

        Class<? extends Drawable> drawableClass = drawable.getClass();
        if (drawableClass.equals(BitmapDrawable.class)) {
            isAnimated = false;
        } else if (drawableClass.equals(AnimationDrawable.class)) {
            animationDrawable = (AnimationDrawable)drawable;
            isAnimated = true;
        }

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                 @Override
                                                                 public void onGlobalLayout() {
                                                                     if (isAnimated) {
                                                                         animationDrawable.start();
                                                                     }
                                                                 }
                                                             });
//                view.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN && isAnimated) {
//                            animationDrawable.start();
//                            return true;
//                        }
//                        return true;
//                    }
//                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName()+lessonNum+"_"+index);
    }

    public void showTextAndIllustration(String text){
        fragmentText.setText(text);
    }
}
