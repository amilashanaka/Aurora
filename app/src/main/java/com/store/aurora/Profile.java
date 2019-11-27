package com.store.aurora;

public class Profile {

    String title,sub_title,dev_date,dev_note;

    public Profile(String title, String sub_title, String dev_date, String dev_note) {
        this.title = title;
        this.sub_title = sub_title;
        this.dev_date=dev_date;
        this.dev_note=dev_note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDev_date() {
        return dev_date;
    }

    public void setDev_date(String sub_title) {
        this.dev_date = dev_date;
    }

    public String getDev_note() {
        return dev_note;
    }

    public void setDev_note(String dev_note) {
        this.dev_note = dev_note;
    }
}
