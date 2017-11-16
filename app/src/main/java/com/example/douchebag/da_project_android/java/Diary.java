package com.example.douchebag.da_project_android.java;

public class Diary {

    private int id;
    private String data;
    private String header;
    private String content;

    public Diary(int id, String header, String content, String data) {
        this.id = id;
        this.data = data;
        this.header = header;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }
}
