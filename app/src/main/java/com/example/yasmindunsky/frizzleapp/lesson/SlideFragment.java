package com.example.yasmindunsky.frizzleapp.lesson;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

    public SlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        fragmentText = view.findViewById(R.id.slideText);
        fragmentImage = view.findViewById(R.id.slideImage);
        Bundle bundle = getArguments();

        Slide slide = LessonActivity.currentLesson.getLessonSlides().get(bundle.getInt("index"));
        String message = slide.getSlideText();
        fragmentText.setText(message);
        String imageSrc = slide.getImageSource();
        int imageDrawable = getResources().getIdentifier(imageSrc , "drawable", getActivity().getPackageName());
        fragmentImage.setImageResource(imageDrawable);

//        //for debugging: show current position in swipe
//        TextView showPosition = view.findViewById(R.id.position);
//        showPosition.setText("Page " + bundle.getInt("index") + "from " + LessonActivity.swipeAdapter.getCount());

        return view;
    }

}
