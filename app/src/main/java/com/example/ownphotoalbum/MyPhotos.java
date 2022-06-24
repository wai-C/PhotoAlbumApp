package com.example.ownphotoalbum;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class MyPhotos {

    @PrimaryKey(autoGenerate = true)
    public int photo_id;
    public String photo_title;
    public String photo_description;
    public byte[] photos; //for binary images


    public MyPhotos(String photo_title, String photo_description, byte[] photos) {
        this.photo_title = photo_title;
        this.photo_description = photo_description;
        this.photos = photos;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public String getPhoto_description() {
        return photo_description;
    }

    public byte[] getPhotos() {
        return photos;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

}
