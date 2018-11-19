package com.frizzl.app.frizzleapp.map;

import android.graphics.Color;

/**
 * Created by Noga on 13/09/2018.
 */

interface MapButton {
    int enabledColor = Color.BLACK;
    int disabledColor = Color.BLACK;
    void setCompleted(boolean completed);
    void setEnabled(boolean enabled);
}
