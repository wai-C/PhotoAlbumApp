package com.example.ownphotoalbum;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = MyPhotos.class, version = 1)
public abstract class MyPhotosDatabase extends RoomDatabase {

    private static MyPhotosDatabase instance;
    public abstract MyPhotosDao myPhotosDao();

    //Standard method for DB
    public static synchronized MyPhotosDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyPhotosDatabase.class,"my_photos_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
