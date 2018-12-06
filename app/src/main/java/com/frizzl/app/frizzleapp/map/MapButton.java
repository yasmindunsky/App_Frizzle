package com.frizzl.app.frizzleapp.map;

import android.graphics.Color;

/**
 * Created by Noga on 13/09/2018.
 */

interface MapButton {
    int disabledTextColor = Color.BLACK;
    void setCompleted();
    void setCurrent();
    void setDisabled();
}
