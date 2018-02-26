package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 19/02/2018.
 */

public abstract class UserCreatedView {
    enum ViewType {TextView, Button};

    ViewType viewType;
    Map<String, String> properties;
    View thisView;
    int layout;
    Context context;
    int index;

    public int getLayout() {
        return layout;
    }

    public int getIndex() { return index; }

    public String createXmlString(XmlSerializer xmlSerializer) {
        String name = viewType.toString();
        updateLatestPosition();
        properties.put("android:id", "@+id/" + properties.get("android:id"));
        properties.put("android:textColor", properties.get("android:textColor"));
        try {
            xmlSerializer.startTag("", name);
            for (String key: properties.keySet()) {
                xmlSerializer.attribute("", key, properties.get(key));
            }
            xmlSerializer.endTag("", name);
        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }
        return null;
    }

    private void updateLatestPosition() {
        View view = getThisView();
        String updatedColumn = view.getTag(R.id.usersViewCol).toString();
        String updatedRow = view.getTag(R.id.usersViewRow).toString();
        properties.put("android:layout_column", updatedColumn);
        properties.put("android:layout_row", updatedRow);
    }

    public abstract void updateProperties();

    public abstract View getThisView();

    public abstract PopupWindow displayPropertiesTable(Context context);


}