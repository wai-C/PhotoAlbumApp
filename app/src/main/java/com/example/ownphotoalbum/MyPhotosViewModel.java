package com.example.ownphotoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyPhotosViewModel extends AndroidViewModel {

    private MyPhotosRepository myPhotosRepository;
    private LiveData<List<MyPhotos>> photosList;

    public MyPhotosViewModel(@NonNull Application application) {
        super(application);

        myPhotosRepository = new MyPhotosRepository(application);
        photosList = myPhotosRepository.getAllPhotos();
    }
    public void insert(MyPhotos photos){ myPhotosRepository.insert(photos);}
    public void update(MyPhotos photos){ myPhotosRepository.update(photos);}
    public void delete(MyPhotos photos){ myPhotosRepository.delete(photos);}

    public LiveData<List<MyPhotos>> getAllPhotos(){ return photosList; }
}
