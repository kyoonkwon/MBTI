package com.example.madcamp_pj1;

import android.graphics.Bitmap;

public class FriendItem {
    String name;
    String message;
    Bitmap bitmap;
    long id;
    String key;

    public FriendItem(Bitmap bitmap, String name, String message, long id, String key) {
        this.name = name;
        this.message= message;
        this.bitmap = bitmap;
        this.id = id;
        this.key = key;
    }

    public long getId() { return id; }

    public String getKey() {return key; }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() { return bitmap; }

    public void setName(String name) {
        this.name = name;
    }

}