package com.frizzl.app.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedButton extends UserCreatedView {
    private Button thisView;
    private ChangedTextListener changedTextListener;
    private Set<String> functions;
    private boolean displayOnClick = false;
    private int selectedTextColorButtonID = R.id.color1;
    private int selectedBGColorButtonID = R.id.bgColor1;
    private boolean textChanged = false;
    private boolean onclickChanged = false;

    UserCreatedButton(Context context, Map<String, String> properties, int index){
        this.context = context;
        this.index = index;
        this.layout = R.layout.popup_properties_button;
        int buttonStyle = R.style.Button_UserCreated;
        this.viewType = "Button";
        this.functions = new HashSet<>();

        this.thisView = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
        Drawable buttonDrawable = ContextCompat.getDrawable(context, R.drawable.user_button_background);
        assert buttonDrawable != null;
        buttonDrawable.setColorFilter(context.getResources().getColor(R.color.frizzle_pink), PorterDuff.Mode.MULTIPLY);
        thisView.setBackground(buttonDrawable);

        thisView.setText(properties.get("android:text"));
//        thisView.setId(Integer.parseInt(properties.get("android:id")));

        String textColorHex = properties.get("android:textColor");
        thisView.setTextColor(Color.parseColor(textColorHex));

        String bgColorHex = properties.get("android:backgroundTint");
        setBackgroundColor(bgColorHex);

        thisView.setPadding(16,10,16,10);

        int column = Integer.parseInt(properties.get("android:layout_column"));
        int row = Integer.parseInt(properties.get("android:layout_row"));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_button_width);
        String marginString = properties.get("android:layout_margin");
        int margin = ViewUtils.dpStringToPixel(context, marginString);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);

        thisView.setTag(R.id.usersViewId, index);
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        this.properties = properties;
    }

    public UserCreatedButton(Context context, int nextViewIndex, int numOfButtons) {
        this.context = context;
        int buttonStyle = R.style.Button_UserCreated;
        this.layout = R.layout.popup_properties_button;
        this.viewType = "Button";
        this.thisView = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);
        this.functions = new HashSet<>();

        Drawable buttonDrawable = ContextCompat.getDrawable(context, R.drawable.user_button_background);
        assert buttonDrawable != null;
        buttonDrawable.setColorFilter(context.getResources().getColor(R.color.frizzle_pink), PorterDuff.Mode.MULTIPLY);
        thisView.setBackground(buttonDrawable);

        thisView.setText(R.string.new_button_text);

        // index in views map in DesignScreenFragment.
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
        properties.put("android:id",  "button_id" + numOfButtons);
        properties.put("android:layout_width", "150dp");
        properties.put("android:layout_height", "wrap_content");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:text", (String) thisView.getText());
        properties.put("android:fontFamily", "@font/rubik_medium");
        properties.put("android:textAllCaps", "false");
        properties.put("android:textColor", "#535264");
        properties.put("android:textSize", "18sp");
        properties.put("android:background", "@drawable/user_button_background");
        properties.put("android:backgroundTint", "#f8b119");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));

        // These will be inserted to the properties table right before XML creation.
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);
    }

    public PopupWindow getPropertiesTablePopupWindow(final Context context) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Set closing button.
        ImageButton closeButton = popupView.findViewById(R.id.close);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Set saving button.
        android.support.v7.widget.AppCompatButton saveButton = popupView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            notifyIfTextOrOnclickChanged();
            popupWindow.dismiss();
        });

        // TEXT
        EditText viewText = popupView.findViewById(R.id.viewTextValue);
        viewText.addTextChangedListener(textWatcher);
//        viewText.setOnFocusChangeListener(finishedEditingText);
        viewText.setText(properties.get("android:text"));
        viewText.setSelection(viewText.length());

        // FONT COLOR
        RadioGroup radioGroup = popupView.findViewById(R.id.viewFontColorValue);
        ((RadioButton)popupView.findViewById(selectedTextColorButtonID)).setChecked(true);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedTextColorButtonID = checkedId;
            RadioButton selectedColorButton = group.findViewById(checkedId);
            if (selectedColorButton == null) return;
            int selectedColor = selectedColorButton.getShadowColor();
            thisView.setTextColor(selectedColor);
            properties.put("android:textColor", ViewUtils.hexFromColorInt(selectedColor));
        });

        // BG COLOR
        RadioGroup BGRadioGroup = popupView.findViewById(R.id.viewBgColorValue);
        ((RadioButton)popupView.findViewById(selectedBGColorButtonID)).setChecked(true);
        BGRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedBGColorButtonID = checkedId;
            RadioButton selectedColorButton = group.findViewById(checkedId);
            if (selectedColorButton == null) return;
            int selectedColor = selectedColorButton.getShadowColor();
            int originalButtonDrawableRes = R.drawable.user_button_background;
            Drawable buttonDrawable = ContextCompat.getDrawable(context, originalButtonDrawableRes);
            if (buttonDrawable != null) buttonDrawable.setColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY);
            thisView.setBackground(buttonDrawable);
            properties.put("android:backgroundTint", ViewUtils.hexFromColorInt(selectedColor));
        });


        // ONCLICK
        Spinner onClickFuncName = popupView.findViewById(R.id.viewOnClickValue);
        if (!displayOnClick){
            onClickFuncName.setVisibility(View.GONE);
            TextView onClickKey = popupView.findViewById(R.id.viewOnClickKey);
            onClickKey.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> onClickAdapter = new ArrayAdapter<>(context, R.layout.simple_spinner_item);
            if (!functions.isEmpty()) {
                onClickAdapter.add("None");
                onClickFuncName.setPrompt(context.getString(R.string.choose_function));
                for (String function : functions) {
                    onClickAdapter.add(function);
                }
            } else {
                onClickFuncName.setPrompt(context.getString(R.string.no_functions));
            }
            onClickFuncName.setAdapter(onClickAdapter);
            onClickFuncName.setOnItemSelectedListener(onOnClickPicked);

            String currentOnclick = properties.get("android:onClick");
            currentOnclick = (Objects.equals(currentOnclick, "") || currentOnclick == null) ? "None" : currentOnclick;
            int positionOfCurrentOnclick = onClickAdapter.getPosition(currentOnclick);
            onClickFuncName.setSelection(positionOfCurrentOnclick);
        }

        // DELETE
        ImageButton deleteButton = popupView.findViewById(R.id.delete);
        deleteButton.setTag(index);
//        deleteButton.setOnClickListener(deleteView);

        return popupWindow;
    }

    private void notifyIfTextOrOnclickChanged() {
        if (changedTextListener != null && textChanged) {
            changedTextListener.onChangedText();
            textChanged = false;
        }
        String function = properties.get("android:onClick");
        if (changedTextListener != null && onclickChanged){
            changedTextListener.onChangedOnClick(function);
            onclickChanged = false;
        }
    }

    @Override
    public void updateProperties() {

    }

    @Override
    public Button getThisView() {
        return thisView;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = String.valueOf(s);
            thisView.setText(text);
            properties.put("android:text", text);
            textChanged = true;
        }
    };

    private EditText.OnFocusChangeListener finishedEditingText = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = String.valueOf(((EditText)v).getText());
                thisView.setText(text);
                properties.put("android:text", text);
                textChanged = true;
            }
        }
    };

    private AdapterView.OnItemSelectedListener onOnClickPicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String function = parent.getItemAtPosition(position).toString();
            properties.put("android:onClick", function);
            onclickChanged = true;
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
            if (drawable != null) drawable.setColorFilter(colorIdentifier, PorterDuff.Mode.DARKEN);
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

    public void setBackgroundColor(String hex) {
        int originalButtonDrawableRes = R.drawable.user_button_background;
        Drawable buttonDrawable = ContextCompat.getDrawable(context, originalButtonDrawableRes);
        if (buttonDrawable != null) buttonDrawable.setColorFilter(Color.parseColor(hex), PorterDuff.Mode.DARKEN);
        thisView.setBackground(buttonDrawable);
    }

    public void setTextColor(String hex) {
        thisView.setTextColor(Color.parseColor(hex));
    }

    public String getOnClick() {
        String onClick = properties.get("android:onClick");
        return onClick != null ? onClick : "";
    }

    public Activity getActivity() {
        Context context = thisView.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void setText(String text) {
        thisView.setText(text);
        properties.put("android:text", text);
    }

    public void setOnClick(String function) {
        properties.put("android:onClick", function);
    }

    void setChangedTextListener(ChangedTextListener changedTextListener) {
        this.changedTextListener = changedTextListener;
    }

    public void setFunctions(Set<String> functions) {
        this.functions = functions;
    }

    public void setDisplayOnClick(boolean displayOnClick) {
        this.displayOnClick = displayOnClick;
    }

    // This interface defines the type of messages I want to communicate to my owner
    public interface ChangedTextListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        void onChangedText();

        void onChangedOnClick(String onClickFuncName);
    }
}
