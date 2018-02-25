package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.Support;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedTextView extends UserCreatedView {
    TextView thisView;

    public UserCreatedTextView(Context context, int nextViewIndex, int numOfTextViews) {
        this.layout = R.layout.popup_properties_text_view;
        this.context = context;
        int textViewStyle = R.style.usersTextView;
        this.thisView = new TextView(context, null, 0, textViewStyle);
        thisView.setText(R.string.newTextViewText);

        // index in views map in GraphicEditFragment.
        this.index = nextViewIndex;
        thisView.setTag(R.id.usersViewId, index);

        // Set Position in GridLayout and Margins.
        int row = nextViewIndex / 2;
        int col = nextViewIndex % 2;
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(col));
        layoutParams.width = 400;
        layoutParams.setMargins(10,10,10,10);
        thisView.setLayoutParams(layoutParams);

//        // Set properties.
//        newText.setTag(nextViewIndex);

        // Set properties as tags.
//        newText.setTag(R.id.viewType, GraphicEditFragment.viewTypes.TextView);

        this.properties = new HashMap<>();
        properties.put("android:id",  "TextView" + numOfTextViews);
        properties.put("android:layout_width", "wrap_content");
        properties.put("android:layout_height", "wrap_content");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:text", (String) thisView.getText());
        properties.put("android:fontFamily", "serif");
        properties.put("android:textColor", "#535264");
        properties.put("android:background", "@android:color/transparent");
        properties.put("android:layout_column", String.valueOf(col));
        properties.put("android:layout_row", String.valueOf(row));


    }

    public PopupWindow displayPropertiesTable(final Context context) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // ID
        EditText viewId = popupView.findViewById(R.id.viewIdValue);
        viewId.setOnFocusChangeListener(finishedEditingId);
        viewId.setText(properties.get("android:id"));

        // TEXT
        EditText viewText = popupView.findViewById(R.id.viewTextValue);
        viewText.setOnFocusChangeListener(finishedEditingText);
        viewText.setText(properties.get("android:text"));

        // FONT
        Spinner fontSpinner = (Spinner) popupView.findViewById(R.id.viewFontValue);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(context,
                R.array.fonts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        fontSpinner.setAdapter(fontAdapter);
//        fontSpinner.setTag(v.getTag());
        fontSpinner.setOnItemSelectedListener(onFontPicked);

        // FONT COLOR
        final Button chooseFontColor = popupView.findViewById(R.id.viewFontColorValue);
        chooseFontColor.setBackgroundColor(Color.parseColor(properties.get("android:textColor")));
        chooseFontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker((Activity)context);
                colorPicker.setRoundColorButton(true);
                colorPicker.setColors(Support.colorsHexList);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        thisView.setTextColor(color);
                        properties.put("android:textColor", Support.colorsHexList.get(position));

                        int originalValueDrawableRes = R.drawable.table_color_circle;
                        Drawable valueDrawable = ContextCompat.getDrawable(context, originalValueDrawableRes);
                        valueDrawable.setColorFilter(color, PorterDuff.Mode.DARKEN);
                        chooseFontColor.setBackground(valueDrawable);
                    }

                    @Override
                    public void onCancel(){
                    }
                });
            }
        });

        // ALIGNMENT
        popupView.findViewById(R.id.viewTextAlignValueLeft).setOnClickListener(onAlignmentClicked);
        popupView.findViewById(R.id.viewTextAlignValueCenter).setOnClickListener(onAlignmentClicked);
        popupView.findViewById(R.id.viewTextAlignValueRight).setOnClickListener(onAlignmentClicked);

        //DELETE
        Button deleteButton = popupView.findViewById(R.id.deleteButton);
        deleteButton.setTag(index);
//        deleteButton.setOnClickListener(deleteView);

        return popupWindow;
    }

    View.OnClickListener onAlignmentClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button buttonClicked  = (Button) v;
            String direction = (String) buttonClicked.getText();
            switch (direction.toLowerCase()) {
                case "left":
                    thisView.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    thisView.setGravity(Gravity.RIGHT);
                    break;
                case "center":
                    thisView.setGravity(Gravity.CENTER);
                    break;
            }
            properties.put("android:gravity", direction);
        }
    };

    @Override
    public void updateProperties() {

    }

    @Override
    public TextView getThisView() {
        return thisView;
    }

    private EditText.OnFocusChangeListener finishedEditingId = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String id = String.valueOf(((EditText)v).getText());
                properties.put("android:id", id);
            }
        }
    };

    private EditText.OnFocusChangeListener finishedEditingText = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = String.valueOf(((EditText)v).getText());
                thisView.setText(text);
                properties.put("android:text", text);
            }
        }
    };

    private AdapterView.OnItemSelectedListener onFontPicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String fontFamily = parent.getItemAtPosition(position).toString();
            thisView.setTypeface(Typeface.create(fontFamily, Typeface.NORMAL));
            properties.put("android:fontFamily", fontFamily);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

//    AdapterView.OnItemSelectedListener onColorPicked = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String color = parent.getItemAtPosition(position).toString();
//            String fullColorName = getColorFromString(color);
//            int colorIdentifier = context.getResources().getColor(context.getResources().getIdentifier(fullColorName, "color", context.getPackageName()));
//            thisView.setTextColor(colorIdentifier);
//            properties.put("android:textColor", fullColorName);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };


//
//    private String getColorFromString(String color) {
//        Map<String, String> colorNamesMap =  new HashMap<>();
//        colorNamesMap.put("white", "white");
//        colorNamesMap.put("red", "holo_red_light");
//        colorNamesMap.put("blue", "holo_blue_light");
//        colorNamesMap.put("orange", "holo_orange_light");
//        colorNamesMap.put("green", "holo_green_light");
//
//        return colorNamesMap.get(color);
//
//    }
}
