package com.store.aurora;

public class Model {

    String title,sub_title,dev_date,dev_note_key;

    public Model(String title, String sub_title, String dev_date, String dev_note_key) {
        this.title = title;
        this.sub_title = sub_title;
        this.dev_date=dev_date;
        this.dev_note_key=dev_note_key;
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


    public String getDev_note_key() {
        return dev_note_key;
    }

    public void setDev_note_key(String dev_note_key) {
        this.dev_note_key = dev_note_key;
    }
}
