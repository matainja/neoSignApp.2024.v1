package com.matainja.bootapplication.Model;

public class ContentModel {
    String app_clock_hands_color;
    String type;
    String url;
    String duration;
    String extention;
    String orientation;
    String app_clock_text,app_clock_timezone,app_clock_size,app_clock_minor_indicator_color,
            app_clock_major_indicator_color,app_clock_innerdot_size,app_clock_innerdot_color;
    String app_queue_departments,overlays,queue_display,queue_terminal;
    String cclass,transition,cdtime,cdtranslation,app_cd_text,speed,rssinfo;

    public String getCclass() {
        return cclass;
    }

    public void setCclass(String cclass) {
        this.cclass = cclass;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public String getCdtime() {
        return cdtime;
    }

    public void setCdtime(String cdtime) {
        this.cdtime = cdtime;
    }

    public String getCdtranslation() {
        return cdtranslation;
    }

    public void setCdtranslation(String cdtranslation) {
        this.cdtranslation = cdtranslation;
    }

    public String getApp_cd_text() {
        return app_cd_text;
    }

    public void setApp_cd_text(String app_cd_text) {
        this.app_cd_text = app_cd_text;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getRssinfo() {
        return rssinfo;
    }

    public void setRssinfo(String rssinfo) {
        this.rssinfo = rssinfo;
    }


    public String getApp_queue_departments() {
        return app_queue_departments;
    }

    public void setApp_queue_departments(String app_queue_departments) {
        this.app_queue_departments = app_queue_departments;
    }

    public String getOverlays() {
        return overlays;
    }

    public void setOverlays(String overlays) {
        this.overlays = overlays;
    }

    public String getQueue_display() {
        return queue_display;
    }

    public void setQueue_display(String queue_display) {
        this.queue_display = queue_display;
    }

    public String getQueue_terminal() {
        return queue_terminal;
    }

    public void setQueue_terminal(String queue_terminal) {
        this.queue_terminal = queue_terminal;
    }



    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getApp_clock_text() {
        return app_clock_text;
    }

    public void setApp_clock_text(String app_clock_text) {
        this.app_clock_text = app_clock_text;
    }

    public String getApp_clock_timezone() {
        return app_clock_timezone;
    }

    public void setApp_clock_timezone(String app_clock_timezone) {
        this.app_clock_timezone = app_clock_timezone;
    }

    public String getApp_clock_size() {
        return app_clock_size;
    }

    public void setApp_clock_size(String app_clock_size) {
        this.app_clock_size = app_clock_size;
    }

    public String getApp_clock_minor_indicator_color() {
        return app_clock_minor_indicator_color;
    }

    public void setApp_clock_minor_indicator_color(String app_clock_minor_indicator_color) {
        this.app_clock_minor_indicator_color = app_clock_minor_indicator_color;
    }

    public String getApp_clock_major_indicator_color() {
        return app_clock_major_indicator_color;
    }

    public void setApp_clock_major_indicator_color(String app_clock_major_indicator_color) {
        this.app_clock_major_indicator_color = app_clock_major_indicator_color;
    }

    public String getApp_clock_innerdot_size() {
        return app_clock_innerdot_size;
    }

    public void setApp_clock_innerdot_size(String app_clock_innerdot_size) {
        this.app_clock_innerdot_size = app_clock_innerdot_size;
    }

    public String getApp_clock_innerdot_color() {
        return app_clock_innerdot_color;
    }

    public void setApp_clock_innerdot_color(String app_clock_innerdot_color) {
        this.app_clock_innerdot_color = app_clock_innerdot_color;
    }




    public String getApp_clock_hands_color() {
        return app_clock_hands_color;
    }

    public void setApp_clock_hands_color(String app_clock_hands_color) {
        this.app_clock_hands_color = app_clock_hands_color;
    }



    public ContentModel(String type, String url, String duration,
                        String extention, String app_clock_hands_color,
                        String app_clock_text, String app_clock_timezone,
                        String app_clock_size,
                        String app_clock_minor_indicator_color,
                        String app_clock_major_indicator_color,
                        String app_clock_innerdot_size,
                        String app_clock_innerdot_color,
                        String cdtime, String cdtranslation,
                        String app_cd_text, String rssinfo) {
        this.type = type;
        this.url = url;
        this.duration = duration;
        this.extention = extention;

        this.app_clock_hands_color = app_clock_hands_color;
        this.app_clock_text = app_clock_text;
        this.app_clock_timezone = app_clock_timezone;
        this.app_clock_size = app_clock_size;
        this.app_clock_minor_indicator_color = app_clock_minor_indicator_color;
        this.app_clock_major_indicator_color = app_clock_major_indicator_color;
        this.app_clock_innerdot_size = app_clock_innerdot_size;
        this.app_clock_innerdot_color = app_clock_innerdot_color;
        this.cdtime=cdtime;
        this.cdtranslation=cdtranslation;
        this.app_cd_text=app_cd_text;
        this.rssinfo=rssinfo;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExtention() {
        return extention;
    }




}
