package com.frizzl.app.frizzleapp;

import java.io.Serializable;

/**
 * Created by Noga on 21/09/2018.
 */

public class Design implements Serializable{
    boolean runnable;
    private boolean withOnClickSet;

    public Design(boolean runnable, boolean withOnClickSet){
        this.runnable = runnable;
        this.withOnClickSet = withOnClickSet;
    }

    public boolean getRunnable() {
        return runnable;
    }

    public boolean getWithOnClickSet() {
        return withOnClickSet;
    }
}
