package com.example.ownphotoalbum;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyPhotosDao {

    @Insert
    void Insert(MyPhotos photos);
    @Update
    void Update(MyPhotos photos);
    @Delete
    void Delete(MyPhotos photos);

    @Query("SELECT * FROM photo_table ORDER BY photo_id ASC")
    LiveData<List<MyPhotos>> getAllPhotos();

}
