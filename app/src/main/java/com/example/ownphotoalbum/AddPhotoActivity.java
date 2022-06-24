package com.example.ownphotoalbum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editTextTitle, editTextMultiDes;
    Button btnConfirm;

    private Bitmap selectedImage;
    private Bitmap scaledImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Photo");
        setContentView(R.layout.activity_add_photo);

        imageView = findViewById(R.id.imageViewUpload);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextMultiDes = findViewById(R.id.editTextTextMultiLineDes);

        btnConfirm = findViewById(R.id.btnConfirm);

       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(ContextCompat.checkSelfPermission(AddPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED)
               {
                   ActivityCompat.requestPermissions(AddPhotoActivity.this,
                           new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

               }
               else{
                   Intent iAccessImageData = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   startActivityForResult(iAccessImageData,2);
               }
           }
       });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedImage==null){
                    Toast.makeText(getApplicationContext(),"Image shouldn't be empty!",Toast.LENGTH_LONG);
                }
                else {
                    String titleUser = editTextTitle.getText().toString();
                    String desUser = editTextMultiDes.getText().toString();
                    //convert image to byte data type
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    scaledImage = reduceUploadedImageToSmallerSize(selectedImage, 300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                    byte[] imageConverted = byteArrayOutputStream.toByteArray();
                    Intent iSendDataToDB = new Intent();
                    iSendDataToDB.putExtra("title_user",titleUser);
                    iSendDataToDB.putExtra("des_user",desUser);
                    iSendDataToDB.putExtra("image_converted",imageConverted);
                    setResult(RESULT_OK,iSendDataToDB);
                    finish();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent iAccessImageData = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iAccessImageData,2);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            try {
                if(Build.VERSION.SDK_INT >= 28)
                {
                ImageDecoder.Source sourceImageUser = ImageDecoder.createSource(this.getContentResolver(),data.getData());
                selectedImage = ImageDecoder.decodeBitmap(sourceImageUser);
                }
                else {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                }
                imageView.setImageBitmap(selectedImage);
            }

            catch (IOException e) {
                e.printStackTrace();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Bitmap reduceUploadedImageToSmallerSize(Bitmap image,int maxsize){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float)width/(float)height;
        if(ratio>1){
            width = maxsize;
            height = (int)(width/ratio);
        }
        else
        {
            height = maxsize;
            width = (int)(height/ratio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}