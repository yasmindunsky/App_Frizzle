package com.frizzl.app.frizzleapp.map;

import android.graphics.Color;

/**
 * Created by Noga on 13/09/2018.
 */

public interface MapButton {
    int enabledColor = Color.BLACK;
    int disabledColor = Color.BLACK;
    public void setCompleted(boolean completed);
    public void setEnabled(boolean enabled);
}
