package com.frizzl.app.frizzleapp.appBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Utils;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedTextView extends UserCreatedView {
    TextView thisView;
    private int selectedTextColorButtonID = R.id.color1;

    public UserCreatedTextView(Context context, Map<String, String> properties, int index){
        this.context = context;
        this.index = index;
        this.layout = R.layout.popup_properties_text_view;
        this.viewType = "TextView";
        int textViewStyle = R.style.TextView_UserCreated;

        this.thisView = new TextView(context, null, 0, textViewStyle);
        thisView.setText(properties.get("android:text"));

        String textColorHex = properties.get("android:textColor");
        thisView.setTextColor(Color.parseColor(textColorHex));

        thisView.setPadding(16,10,16,10);

        int column = Integer.parseInt(properties.get("android:layout_column"));
        int row = Integer.parseInt(properties.get("android:layout_row"));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_button_width);

        String marginString = properties.get("android:layout_margin");
        int margin = Utils.dpStringToPixel(marginString, context);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);


        thisView.setTag(R.id.usersViewId, index);
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        this.properties = properties;
    }



    public UserCreatedTextView(Context context, int nextViewIndex, int numOfTextViews) {
        this.context = context;
        this.layout = R.layout.popup_properties_text_view;
        this.viewType = "TextView";
        int textViewStyle = R.style.TextView_UserCreated;
        this.thisView = new TextView(context, null, 0, textViewStyle);
        thisView.setText(R.string.new_text_view_text);

        // index in views map in DesignScreenFragment.
        this.index = nextViewIndex;
        thisView.setTag(R.id.usersViewId, index);

        // Set Position in GridLayout and Margins.
        int row = nextViewIndex / 2;
        int column = nextViewIndex % 2;
        GridLayout.LayoutParams layoutParams =
                new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));
        layoutParams.width = 400;
        layoutParams.setMargins(10,10,10,10);
        thisView.setLayoutParams(layoutParams);

//        // Set properties.
//        newText.setTag(nextViewIndex);

        // Set properties as tags.
//        newText.setTag(R.id.viewType, DesignScreenFragment.viewTypes.TextView);

        this.properties = new HashMap<>();
        properties.put("android:id",  "text_view" + numOfTextViews);
        properties.put("android:layout_width", "150dp");
        properties.put("android:layout_height", "wrap_content");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:text", (String) thisView.getText());
        properties.put("android:fontFamily", "@font/rubik_medium");
        properties.put("android:textAllCaps", "false");
        properties.put("android:textColor", "#b93660");
        properties.put("android:textSize", "18sp");
        properties.put("android:background", "@drawable/user_text_view_background");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));

        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);
    }

    public PopupWindow getPropertiesTablePopupWindow(final Context context) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(layout, null);

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
        saveButton.setOnClickListener(v -> popupWindow.dismiss());

        // ID
//        EditText viewId = popupView.findViewById(R.id.viewIdValue);
//        viewId.setOnFocusChangeListener(finishedEditingId);
//        viewId.setText(properties.get("android:id"));

        // TEXT
        EditText viewText = popupView.findViewById(R.id.viewTextValue);
        viewText.setOnFocusChangeListener(finishedEditingText);
        viewText.setText(properties.get("android:text"));

        // FONT COLOR
        RadioGroup radioGroup = popupView.findViewById(R.id.viewFontColorValue);
        ((RadioButton)popupView.findViewById(selectedTextColorButtonID)).setChecked(true);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedTextColorButtonID = checkedId;
            RadioButton selectedColorButton = group.findViewById(checkedId);
            if (selectedColorButton == null) return;
            int selectedColor = selectedColorButton.getShadowColor();
            thisView.setTextColor(selectedColor);
            properties.put("android:textColor", Utils.hexFromColorInt(selectedColor));
        });

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
    public TextView getThisView() {
        return thisView;
    }

    private EditText.OnFocusChangeListener finishedEditingId = (v, hasFocus) -> {
        if (!hasFocus) {
            String id = String.valueOf(((EditText)v).getText());
            properties.put("android:id", id);
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

}