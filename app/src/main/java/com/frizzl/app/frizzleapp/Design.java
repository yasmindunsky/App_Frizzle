package com.frizzl.app.frizzleapp;

import java.io.Serializable;

/**
 * Created by Noga on 21/09/2018.
 */

public class Design implements Serializable{
    boolean runnable;

    public Design(boolean runnable){
        this.runnable = runnable;
    }

    public boolean getRunnable() {
        return runnable;
    }

}
