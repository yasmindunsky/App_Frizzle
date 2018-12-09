package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.R;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Noga on 19/02/2018.
 */

public abstract class UserCreatedView {
    public static final int NUM_OF_COLS = 1;

    String viewType;
    Map<String, String> properties;
    View thisView;
    int layout;
    Context context;
    int index;

    public int getLayout() {
        return layout;
    }

    public int getIndex() { return index; }

    public String getViewType() { return viewType; }

    public Map<String, String> getProperties() { return properties; }

    public void createXmlString(XmlSerializer xmlSerializer) {
        String name = viewType;
        updateLatestPosition();
        if (!viewType.equals( AnnotationUserCreatedViewType.IMAGE_VIEW)) {
            properties.put("android:textColor", properties.get("android:textColor"));
        }
        try {
            xmlSerializer.startTag("", name);
            xmlSerializer.attribute("", "android:id", "@+id/" + properties.get("android:id"));
            for (String key: properties.keySet()) {
                if (!key.equals("android:id")) {
                    xmlSerializer.attribute("", key, properties.get(key));
                }
            }
            xmlSerializer.endTag("", name);
        } catch (IOException e) {
            Log.e("Exception", "xmlSerializer " + xmlSerializer.toString() + " failed: " + e.toString());
        }
    }

    private void updateLatestPosition() {
        View view = getThisView();
        String updatedColumn = view.getTag(R.id.usersViewCol).toString();
        String updatedRow = view.getTag(R.id.usersViewRow).toString();
        properties.put("android:layout_column", updatedColumn);
        properties.put("android:layout_row", updatedRow);
    }

    public abstract View getThisView();

    public abstract PopupWindow getPropertiesTablePopupWindow(Context context);


}