package com.example.yasmindunsky.frizzleapp.lesson;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {
    TextView fragmentText;
    ImageView fragmentImage;
    AnimationDrawable animationDrawable;
    boolean isAnimated;

    public SlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);

        // Rotation for RTL swiping.
        view.setRotationY(180);

        fragmentText = view.findViewById(R.id.slideText);
        fragmentImage = view.findViewById(R.id.slideImage);
        Bundle bundle = getArguments();

        Slide slide = LessonActivity.getCurrentLesson().getLessonSlides().get(bundle.getInt("index"));
        String message = slide.getSlideText();
        fragmentText.setText(message);
        String imageSrc = slide.getImageSource();
        int imageDrawable = getResources().getIdentifier(imageSrc , "drawable", getActivity().getPackageName());

        Class<? extends Drawable> drawableClass = getResources().getDrawable(imageDrawable).getClass();
        if (drawableClass.equals(BitmapDrawable.class)) {
            fragmentImage.setImageResource(imageDrawable);
            isAnimated = false;
        } else if (drawableClass.equals(AnimationDrawable.class)) {
            fragmentImage.setBackgroundResource(imageDrawable);
            animationDrawable = (AnimationDrawable) fragmentImage.getBackground();
            isAnimated = true;
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isAnimated) {
                    animationDrawable.start();
                    return true;
                }
                return true;
            }
        });

        return view;
    }
}
