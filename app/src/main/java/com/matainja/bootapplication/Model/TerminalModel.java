package com.matainja.bootapplication.Model;

public class TerminalModel {
    private int imageResource;
    private String itemName;

    public TerminalModel(int imageResource, String itemName) {
        this.imageResource = imageResource;
        this.itemName = itemName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getItemName() {
        return itemName;
    }
}
