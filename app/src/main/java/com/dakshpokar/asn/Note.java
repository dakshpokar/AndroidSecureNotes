package com.dakshpokar.asn;

public class Note {
    private Integer ID;
    private String title;
    private String note;
    private String date;


    public Note()
    {}

    public Note(Integer ID, String title, String note, String date) {
        this.ID = ID;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
