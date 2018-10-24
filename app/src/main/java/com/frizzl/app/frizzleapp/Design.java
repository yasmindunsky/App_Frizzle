package com.frizzl.app.frizzleapp;

import java.io.Serializable;

/**
 * Created by Noga on 21/09/2018.
 */

public class Design implements Serializable{
    private boolean runnable;
    private String onClickFunction;
    private boolean withOnClickSet;

    public Design(boolean runnable, boolean withOnClickSet, String onClickFunction){
        this.runnable = runnable;
        this.onClickFunction = onClickFunction;
        this.withOnClickSet = withOnClickSet;
    }

    public boolean getRunnable() {
        return runnable;
    }

    public boolean getWithOnClickSet() {
        return withOnClickSet;
    }

    public String getOnClickFunction() {
        return onClickFunction;
    }
}
