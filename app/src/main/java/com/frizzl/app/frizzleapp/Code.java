package com.frizzl.app.frizzleapp;

import java.io.Serializable;

/**
 * Created by Noga on 21/09/2018.
 */

public class Code implements Serializable{
    boolean mutable;
    boolean runnable;
    String code;

    public Code(boolean mutable, boolean runnable, String code){
        this.mutable = mutable;
        this.runnable = runnable;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public boolean getRunnable() {
        return runnable;
    }

    public boolean getMutable() {
        return mutable;
    }
}
