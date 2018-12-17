package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Noga on 17/12/2018.
 */

public class SharingManager {


    public SharingManager(){
    }

    public Intent createTextIntent(String text){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);
        return share;
    }
}
