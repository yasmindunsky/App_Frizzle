package com.frizzl.app.frizzleapp;

/**
 * Created by Noga on 06/09/2018.
 */

public class UserApp {
    private String xml;
    private String java;
    private String name;
    private String icon;
    private int appID;

    public UserApp(int appID){
        this.appID = appID;
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return !(name ==null);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
