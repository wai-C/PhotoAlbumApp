package com.example.ownphotoalbum;


import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;

public class MyPhotosRepository {

    private MyPhotosDao myPhotosDao;
    private LiveData<List<MyPhotos>> photosList;


    public MyPhotosRepository(Application application){
        MyPhotosDatabase myPhotosDatabase = MyPhotosDatabase.getInstance(application);
        myPhotosDao = myPhotosDatabase.myPhotosDao();
        photosList = myPhotosDao.getAllPhotos();


    }

    public void insert(MyPhotos photos){ new InsertPhotoAsyncTask(myPhotosDao).execute(photos);}
    public void update(MyPhotos photos){ new UpdatePhotoAsyncTask(myPhotosDao).execute(photos); }
    public void delete(MyPhotos photos){ new DeletePhotoAsyncTask(myPhotosDao).execute(photos); }
    public LiveData<List<MyPhotos>> getAllPhotos(){ return photosList; }

    //Asynchronous task part:
    public static class InsertPhotoAsyncTask extends AsyncTask<MyPhotos,Void,Void>{

        MyPhotosDao myPhotosDao;

        public InsertPhotoAsyncTask(MyPhotosDao myPhotosDao) {
            this.myPhotosDao = myPhotosDao;
        }

        @Override
        protected Void doInBackground(MyPhotos... myPhotos) {
            myPhotosDao.Insert(myPhotos[0]);
            return null;
        }
    }
    public static class UpdatePhotoAsyncTask extends AsyncTask<MyPhotos,Void,Void>{

        MyPhotosDao myPhotosDao;

        public UpdatePhotoAsyncTask(MyPhotosDao myPhotosDao) {
            this.myPhotosDao = myPhotosDao;
        }

        @Override
        protected Void doInBackground(MyPhotos... myPhotos) {
            myPhotosDao.Update(myPhotos[0]);
            return null;
        }
    }
    public static class DeletePhotoAsyncTask extends AsyncTask<MyPhotos,Void,Void>{

        MyPhotosDao myPhotosDao;

        public DeletePhotoAsyncTask(MyPhotosDao myPhotosDao) {
            this.myPhotosDao = myPhotosDao;
        }

        @Override
        protected Void doInBackground(MyPhotos... myPhotos) {
            myPhotosDao.Delete(myPhotos[0]);
            return null;
        }
    }
}
