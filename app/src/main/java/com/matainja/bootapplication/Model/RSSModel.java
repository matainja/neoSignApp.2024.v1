package com.matainja.bootapplication.Model;

public class RSSModel {
    String title,description,photo,qr_code,date;

    public RSSModel(String title, String description, String date, String qr_code, String photo) {
        this.title=title;
        this.description=description;
        this.date=date;
        this.qr_code=qr_code;
        this.photo=photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getQr() {
        return qr_code;
    }

    public void setQr(String qr) {
        this.qr_code = qr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
