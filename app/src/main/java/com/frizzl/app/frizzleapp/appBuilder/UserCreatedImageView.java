package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
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
        init(context);
        this.index = index;

        int column = Integer.parseInt(properties.get("android:layout_column"));
        int row = Integer.parseInt(properties.get("android:layout_row"));
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
                GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_image_view_width);
        layoutParams.height = (int) context.getResources().getDimension(R.dimen.user_created_image_view_width);

        String marginString = properties.get("android:layout_margin");
        int margin = Support.dpStringToPixel(marginString, context);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);

        thisView.setTag(R.id.usersViewId, index);
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        this.properties = properties;
    }


    public UserCreatedImageView(Context context, int nextViewIndex, int numOfTextViews) {
        init(context);

        // index in views map in DesignScreenFragment.
        this.index = nextViewIndex;
        thisView.setTag(R.id.usersViewId, index);

        // Set Position in GridLayout and Margins.
        int row = nextViewIndex / 2;
        int column = nextViewIndex % 2;
        GridLayout.LayoutParams layoutParams =
                new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));
        layoutParams.width = 300;
        layoutParams.height = 300;
        layoutParams.setMargins(10,10,10,10);
        thisView.setLayoutParams(layoutParams);

//        // Set properties.
//        newText.setTag(nextViewIndex);

        // Set properties as tags.
//        newText.setTag(R.id.viewType, DesignScreenFragment.viewTypes.TextView);

        this.properties = new HashMap<>();
        properties.put("android:id",  "image_view" + numOfTextViews);
        properties.put("android:layout_width", "70dp");
        properties.put("android:layout_height", "70dp");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:background", "@drawable/user_text_view_background");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));

        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

        PopupWindow popupWindow = getPropertiesTablePopupWindow(context);
    }

    private void init(Context context) {
        this.context = context;
        this.layout = R.layout.popup_choose_image;
        this.viewType = "ImageView";
        int textViewStyle = R.style.ImageView_UserCreated;
        this.thisView = new ImageView(context, null, 0, textViewStyle);
        thisView.setBackground(context.getDrawable(R.drawable.ic_tutorial_app_1));
        thisView.setPadding(16,10,16,10);
    }

    public PopupWindow getPropertiesTablePopupWindow(Context context) {
        return new ChooseImagePopupWindow(context, index, this);
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

    public void setImage(Drawable background, String tag) {
        thisView.setBackground(background);
        properties.put("android:background", "@drawable/"+tag);
    }
}