package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedImageView extends UserCreatedView {
    ImageView thisView;

    public UserCreatedImageView(Context context, Map<String, String> properties, int index){
        this.context = context;
        this.index = index;
        this.layout = R.layout.popup_properties_text_view;
        this.viewType = "ImageView";
        int textViewStyle = R.style.TextView_UserCreated;

        this.thisView = new ImageView(context, null, 0, textViewStyle);
//        thisView.setText(properties.get("android:text"));

//        String textColorHex = properties.get("android:textColor");
//        thisView.setTextColor(Color.parseColor(textColorHex));
        thisView.setBackground(context.getDrawable(R.drawable.ic_tutorial_app_1));

        thisView.setPadding(16,10,16,10);

        int column = Integer.parseInt(properties.get("android:layout_column"));
        int row = Integer.parseInt(properties.get("android:layout_row"));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_button_width);

        String marginString = properties.get("android:layout_margin");
        int margin = Support.dpStringToPixel(marginString, context);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);

        // TODO: load font.

        thisView.setTag(R.id.usersViewId, index);
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        this.properties = properties;
    }



    public UserCreatedImageView(Context context, int nextViewIndex, int numOfTextViews) {
        this.context = context;
        this.layout = R.layout.popup_properties_text_view;
        this.viewType = "ImageView";
        int textViewStyle = R.style.TextView_UserCreated;
        this.thisView = new ImageView(context, null, 0, textViewStyle);
        thisView.setBackground(context.getDrawable(R.drawable.ic_tutorial_app_1));
//        thisView.setText(R.string.new_text_view_text);

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
//        properties.put("android:text", (String) thisView.getText());
        properties.put("android:fontFamily", "serif");
        properties.put("android:textColor", "#535264");
        properties.put("android:background", "@drawable/user_text_view_background");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));

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

        // Set saving button.
        android.support.v7.widget.AppCompatButton saveButton = popupView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // ID
        EditText viewId = popupView.findViewById(R.id.viewIdValue);
        viewId.setOnFocusChangeListener(finishedEditingId);
        viewId.setText(properties.get("android:id"));

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
    public ImageView getThisView() {
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
}