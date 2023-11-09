package com.matainja.bootapplication.Model;

public class ClockParams {
    private String text;
    private String size;
    private String hourColor;
    private String minuteColor;
    private String secondColor;
    private String minorIndicator;
    private String majorIndicator;
    private String innerDotSize;
    private String innerDotColor;

    public ClockParams(String text, String size, String hourColor, String minuteColor, String secondColor, String minorIndicator, String majorIndicator, String innerDotSize, String innerDotColor) {
        this.text = text;
        this.size = size;
        this.hourColor = hourColor;
        this.minuteColor = minuteColor;
        this.secondColor = secondColor;
        this.minorIndicator = minorIndicator;
        this.majorIndicator = majorIndicator;
        this.innerDotSize = innerDotSize;
        this.innerDotColor = innerDotColor;
    }

    // Add getters and setters as needed
}

