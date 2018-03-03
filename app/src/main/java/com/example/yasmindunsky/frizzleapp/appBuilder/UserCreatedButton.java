package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;
import com.example.yasmindunsky.frizzleapp.Support;
import com.example.yasmindunsky.frizzleapp.UserProfile;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedButton extends UserCreatedView {
    Button thisView;

    public UserCreatedButton(Context context, Map<String, String> properties, int index){
        this.context = context;
        this.index = index;
        this.layout = R.layout.popup_properties_button;
        int buttonStyle = R.style.usersButton;
        this.viewType = ViewType.Button;


        this.thisView = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
        thisView.setText(properties.get("android:text"));
//        thisView.setId(Integer.parseInt(properties.get("android:id")));

        String textColorHex = properties.get("android:textColor");
        thisView.setTextColor(Color.parseColor(textColorHex));

        String bgColorHex = properties.get("android:backgroundTint");
        int originalButtonDrawableRes = R.drawable.user_button_background;
        Drawable buttonDrawable = ContextCompat.getDrawable(context, originalButtonDrawableRes);
        buttonDrawable.setColorFilter(Color.parseColor(bgColorHex), PorterDuff.Mode.DARKEN);
        thisView.setBackground(buttonDrawable);

        thisView.setPadding(16,10,16,10);

        int column = Integer.parseInt(properties.get("android:layout_column"));
        int row = Integer.parseInt(properties.get("android:layout_row"));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_button_width);
        String marginString = properties.get("android:layout_margin");
        int margin = dpStringToPixel(marginString);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);

        // TODO: load font.

        thisView.setTag(R.id.usersViewId, index);
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        this.properties = properties;
    }

    private int dpStringToPixel(String dp) {
        dp = dp.replaceAll("[^\\d.]", "");
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  Integer.parseInt(dp), context.getResources().getDisplayMetrics());
    }

    public UserCreatedButton(Context context, int nextViewIndex, int numOfButtons) {
        this.context = context;
        int buttonStyle = R.style.usersButton;
        this.layout = R.layout.popup_properties_button;
        this.viewType = ViewType.Button;
        this.thisView = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
        thisView.setText(R.string.newButtonText);

        // index in views map in GraphicEditFragment.
        this.index = nextViewIndex;
        thisView.setTag(R.id.usersViewId, index);

        // Set Position in GridLayout and Margins.
        int row = nextViewIndex / 2;
        int column = nextViewIndex % 2;
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_button_width);
        layoutParams.setMargins(10,10,10,10);

        thisView.setLayoutParams(layoutParams);

        this.properties = new HashMap<>();
        properties.put("android:id",  "Button" + numOfButtons);
        properties.put("android:layout_width", "150dp");
        properties.put("android:layout_height", "wrap_content");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:text", (String) thisView.getText());
        properties.put("android:fontFamily", "serif");
        properties.put("android:textColor", "#535264");
        properties.put("android:background", "@drawable/user_button_background");
        properties.put("android:backgroundTint", "#f4f4f4");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));

        // These will be inserted to the properties table right before XML creation.
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);
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
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Set closing button.
        ImageButton closeButton = popupView.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


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

        // BG COLOR
        final Button chooseBgColor = popupView.findViewById(R.id.viewBgColorValue);
        chooseBgColor.setBackgroundColor(Color.parseColor(properties.get("android:backgroundTint")));
        chooseBgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker((Activity)context);
                colorPicker.setRoundColorButton(true);
                colorPicker.setColors(Support.colorsHexList);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        int originalButtonDrawableRes = R.drawable.user_button_background;
                        Drawable buttonDrawable = ContextCompat.getDrawable(context, originalButtonDrawableRes);
                        buttonDrawable.setColorFilter(color, PorterDuff.Mode.DARKEN);
                        thisView.setBackground(buttonDrawable);

                        int originalValueDrawableRes = R.drawable.table_color_circle;
                        Drawable valueDrawable = ContextCompat.getDrawable(context, originalValueDrawableRes);
                        valueDrawable.setColorFilter(color, PorterDuff.Mode.DARKEN);
                        chooseBgColor.setBackground(valueDrawable);

                        properties.put("android:backgroundTint", Support.colorsHexList.get(position));
                    }

                    @Override
                    public void onCancel(){
                    }
                });
            }
        });

        // ONCLICK
        TableRow tableRow = popupView.findViewById(R.id.onClickTableRow);
        if (UserProfile.user.getTopLessonID() < 6) {
            tableRow.setVisibility(View.INVISIBLE);
        } else {
            EditText onClickFuncName = popupView.findViewById(R.id.viewOnClickValue);
            onClickFuncName.setOnFocusChangeListener(finishedOnClick);
            onClickFuncName.setHint("שם הפונקציה");
        }

        //DELETE
        ImageButton deleteButton = popupView.findViewById(R.id.delete);
        deleteButton.setTag(index);
//        deleteButton.setOnClickListener(deleteView);

        return popupWindow;
    }

    @Override
    public void updateProperties() {

    }

    @Override
    public Button getThisView() {
        return thisView;
    }

    EditText.OnFocusChangeListener finishedEditingId = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String id = String.valueOf(((EditText)v).getText());
                properties.put("android:id", id);
            }
        }
    };

    EditText.OnFocusChangeListener finishedEditingText = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = String.valueOf(((EditText)v).getText());
                thisView.setText(text);
                properties.put("android:text", text);
            }
        }
    };

    EditText.OnFocusChangeListener finishedOnClick = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String onclickFuncName = String.valueOf(((EditText)v).getText());
                properties.put("android:onClick", onclickFuncName);
            }
        }
    };

    AdapterView.OnItemSelectedListener onFontPicked = new AdapterView.OnItemSelectedListener() {
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

    AdapterView.OnItemSelectedListener onColorPicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String color = parent.getItemAtPosition(position).toString();
            String fullColorName = getColorFromString(color);
            int colorIdentifier = context.getResources().getColor(context.getResources().getIdentifier(fullColorName, "color", context.getPackageName()));
            int originalDrawable = R.drawable.user_button_background;
            Drawable drawable = ContextCompat.getDrawable(context, originalDrawable);
            drawable.setColorFilter(colorIdentifier, PorterDuff.Mode.DARKEN);
            thisView.setBackground(drawable);
            properties.put("android:backgroundTint", fullColorName);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };



    private String getColorFromString(String color) {
        Map<String, String> colorNamesMap =  new HashMap<>();
        colorNamesMap.put("white", "white");
        colorNamesMap.put("red", "holo_red_light");
        colorNamesMap.put("blue", "holo_blue_light");
        colorNamesMap.put("orange", "holo_orange_light");
        colorNamesMap.put("green", "holo_green_light");

        return colorNamesMap.get(color);

    }
}
