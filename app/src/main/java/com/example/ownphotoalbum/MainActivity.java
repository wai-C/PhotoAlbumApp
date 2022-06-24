package com.example.ownphotoalbum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButtonAdd;

    private MyPhotosViewModel myPhotosViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButtonAdd = findViewById(R.id.floatingActionBtnAdd);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyPhotosAdapter myPhotosAdapter = new MyPhotosAdapter();
        recyclerView.setAdapter(myPhotosAdapter);


        myPhotosViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyPhotosViewModel.class);

        myPhotosViewModel.getAllPhotos().observe(MainActivity.this, new Observer<List<MyPhotos>>() {
            @Override
            public void onChanged(List<MyPhotos> myPhotos) {
                myPhotosAdapter.setPhotosList(myPhotos);
            }
        });

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddImageToAlbum = new Intent(MainActivity.this,AddPhotoActivity.class);
                startActivityForResult(iAddImageToAlbum,3);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myPhotosViewModel.delete(myPhotosAdapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        myPhotosAdapter.setOnPhotoClickListener(new MyPhotosAdapter.onPhotoClickListener() {
            @Override
            public void onPhotoClick(MyPhotos photos) {
                Intent iSendDataNeedToUpdate = new Intent(MainActivity.this,UpdatePhotoActivity.class);
                iSendDataNeedToUpdate.putExtra("id_need_update",photos.getPhoto_id());
                iSendDataNeedToUpdate.putExtra("title_need_update",photos.getPhoto_title());
                iSendDataNeedToUpdate.putExtra("des_need_update",photos.getPhoto_description());
                iSendDataNeedToUpdate.putExtra("photo_need_update",photos.getPhotos());
                startActivityForResult(iSendDataNeedToUpdate,4);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode== RESULT_OK && data != null){
            String titleFinal = data.getStringExtra("title_user");
            String desFinal   = data.getStringExtra("des_user");
            byte[] imageFinal = data.getByteArrayExtra("image_converted");

            //Save the data
            MyPhotos myPhotos = new MyPhotos(titleFinal,desFinal,imageFinal);
            myPhotosViewModel.insert(myPhotos);
        }
        if(requestCode == 4 && resultCode== RESULT_OK && data!=null){


            String titleUpdate = data.getStringExtra("updated_title_user");
            String desUpdate  = data.getStringExtra("updated_des_user");
            byte[] imageUpdate = data.getByteArrayExtra("updated_image");
            int updatedId = data.getIntExtra("update_id",-1);

            //Save the data
            MyPhotos myPhotos = new MyPhotos(titleUpdate,desUpdate,imageUpdate);
            myPhotos.setPhoto_id(updatedId);
            myPhotosViewModel.update(myPhotos);

        }
    }
}