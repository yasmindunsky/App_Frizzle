package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.ViewUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 19/02/2018.
 */

public class UserCreatedImageView extends UserCreatedView {
    private ImageView thisView;
    private int selectedImageID = R.id.radioButton1;

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
        int margin = ViewUtils.dpStringToPixel(marginString, context);
        layoutParams.setMargins(margin,margin,margin,margin);
        thisView.setLayoutParams(layoutParams);
        thisView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thisView.setForegroundGravity(Gravity.CENTER);

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
        int row = nextViewIndex / UserCreatedView.NUM_OF_COLS;
        int column = nextViewIndex % UserCreatedView.NUM_OF_COLS;
        GridLayout.LayoutParams layoutParams =
                new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.user_created_image_view_width);
        layoutParams.height = (int) context.getResources().getDimension(R.dimen.user_created_image_view_width);
        layoutParams.setMargins(10,10,10,10);
        thisView.setLayoutParams(layoutParams);
        thisView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thisView.setForegroundGravity(Gravity.CENTER);

//        // Set properties.
//        newText.setTag(nextViewIndex);

        // Set properties as tags.
//        newText.setTag(R.id.viewType, DesignScreenFragment.viewTypes.TextView);

        this.properties = new HashMap<>();
        properties.put("android:id",  "image_view" + numOfTextViews);
        properties.put("android:layout_width", "150dp");
        properties.put("android:layout_height", "150dp");
        properties.put("android:layout_margin", "10dp");
        properties.put("android:background", "@drawable/user_text_view_background");
        properties.put("android:padding", "10dp");
        properties.put("android:paddingStart", "16dp");
        properties.put("android:paddingEnd", "16dp");
        properties.put("android:layout_column", String.valueOf(column));
        properties.put("android:layout_row", String.valueOf(row));
        properties.put("android:tag", "https://firebasestorage.googleapis.com/v0/b/frizzleapp.appspot.com/o/usersImages%2Fblank.png?alt=media&token=1e04ba63-a8aa-4868-965b-e25faf2c41b2");
        thisView.setTag(R.id.usersViewRow, row);
        thisView.setTag(R.id.usersViewCol, column);

//        PopupWindow popupWindow = getPropertiesTablePopupWindow(context);
    }

    private void init(Context context) {
        this.context = context;
        this.layout = R.layout.popup_choose_image;
        this.viewType = "ImageView";
        int textViewStyle = R.style.ImageView_UserCreated;
        this.thisView = new ImageView(context, null, 0, textViewStyle);
        thisView.setBackground(context.getDrawable(R.drawable.blank));
        thisView.setPadding(16,10,16,10);
    }

    public PopupWindow getPropertiesTablePopupWindow(Context context) {
        return new ChooseImagePopupWindow((AppBuilderActivity) context, index, this, selectedImageID);
    }

    @Override
    public ImageView getThisView() {
        return thisView;
    }

    private EditText.OnFocusChangeListener finishedEditingId = (v, hasFocus) -> {
        if (!hasFocus) {
            String id = String.valueOf(((EditText)v).getText());
            properties.put("android:id", id);
        }
    };

    public void setImage(RadioButton selectedImageButton, String tag) {
        selectedImageID = selectedImageButton.getId();
        thisView.setBackground(selectedImageButton.getBackground());
        String imgName = selectedImageButton.getTag().toString();
        properties.put("android:tag", ViewUtils.imgNameToAddress(imgName));
    }

    public void setImage(BitmapDrawable selectedImage) {
        thisView.setBackground(selectedImage);
    }

    public void createHTMLString(XmlSerializer xmlSerializer) {
        String name = "img";
        try {
            xmlSerializer.startTag("", name);
            xmlSerializer.attribute("", "class", "userImg");

            xmlSerializer.attribute("", "id", properties.get("android:id"));
            xmlSerializer.attribute("", "src", properties.get("android:tag"));
            xmlSerializer.endTag("", name);
        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }
    }

    public void setTag(String imageURL) {
        properties.put("android:tag", imageURL);
    }
}