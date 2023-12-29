package com.matainja.bootapplication.Model;

public class TerminalModel {
    private int imageResource;
    private String itemName;
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }



    public TerminalModel(int imageResource, String itemName, String appId) {
        this.imageResource = imageResource;
        this.itemName = itemName;
        this.appId = appId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getItemName() {
        return itemName;
    }
}
