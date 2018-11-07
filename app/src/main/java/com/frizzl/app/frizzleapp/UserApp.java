package com.frizzl.app.frizzleapp;

import com.frizzl.app.frizzleapp.appBuilder.UserCreatedView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 06/09/2018.
 */

public class UserApp implements Serializable {
    private String xml;
    private String code;
    private String manifest;

    private Map<Integer,UserCreatedView> views;
    private  int numOfButtons;
    private  int numOfTextViews;
    private  int numOfImageViews;
    private  int nextViewIndex;

    private String name;
    private String icon;
    private int appLevelID;

    public UserApp(int appLevelID){
        this.appLevelID = appLevelID;
        this.name = "My Frizzl App";
        this.icon = "ic_tutorial_app_5";
        this.xml = "";
        this.code = "";
        this.manifest = "";
        this.views = new HashMap<>();
    }

    public Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    public void setViews(Map<Integer, UserCreatedView> views, int numOfButtons, int numOfTextViews,
                         int numOfImageViews, int nextViewIndex) {
        this.views = views;
        this.numOfButtons = numOfButtons;
        this.numOfTextViews = numOfTextViews;
        this.numOfImageViews = numOfImageViews;
        this.nextViewIndex = nextViewIndex;
    }

    public String getName() {
        if (name.equals("")) {
            name = "My Frizzl App";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return !(name == null);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        if (icon.equals("")){
            icon = "ic_tutorial_app_5";
        }
        return icon;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public String getManifest() {
        return manifest;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getXml() {
        return xml;
    }

    public int getAppLevelID() {
        return appLevelID;
    }

    public int getNumOfButtons() {
        return numOfButtons;
    }

    public int getNumOfImageViews() {
        return numOfImageViews;
    }

    public int getNumOfTextViews() {
        return numOfTextViews;
    }

    public int getNextIndex() {
        return nextViewIndex;
    }
}
