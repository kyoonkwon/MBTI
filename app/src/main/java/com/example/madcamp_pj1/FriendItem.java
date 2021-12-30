package com.example.madcamp_pj1;

public class FriendItem {
    String name;
    String message;
    int resourceId;
    long id;
    String key;

    public FriendItem(int resourceId, String name, String message, long id, String key) {
        this.name = name;
        this.message= message;
        this.resourceId = resourceId;
        this.id = id;
        this.key = key;
    }

    public long getId() { return id; }

    public String getKey() {return key; }

    public int getResourceId() {
        return resourceId;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}