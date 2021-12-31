package com.example.madcamp_pj1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class FriendItem implements Parcelable{
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

    public FriendItem(Parcel src){
        name = src.readString();
        message = src.readString();
        bitmap = (Bitmap) src.readValue(Bitmap.class.getClassLoader());
        id = src.readLong();
        key = src.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeString(message);
        dest.writeValue(bitmap);
        dest.writeLong(id);
        dest.writeString(key);

    }

    public static final Parcelable.Creator<FriendItem> CREATOR = new Parcelable.Creator<FriendItem>(){
        @Override
        public FriendItem createFromParcel(Parcel parcel) {
            return new FriendItem(parcel);
        }

        @Override
        public FriendItem[] newArray(int i) {
            return new FriendItem[i];
        }
    };

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