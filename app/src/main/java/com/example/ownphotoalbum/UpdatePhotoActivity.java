package com.example.ownphotoalbum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdatePhotoActivity extends AppCompatActivity {

    ImageView imageViewUp;
    EditText editTextTitleUp, editTextMultiDesUp;
    Button btnUpdate;

    //Part1 update
    int idNeedUpdate; //actually will be same
    String titleNeedUpdate, desNeedUpdate;
    byte[] photoNeedUpdate;

    //Part2 update
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Photo");
        setContentView(R.layout.activity_update_photo);

        imageViewUp = findViewById(R.id.imageViewUploadUp);
        editTextTitleUp = findViewById(R.id.editTextTitleUp);
        editTextMultiDesUp = findViewById(R.id.editTextTextMultiLineDesUp);

        btnUpdate = findViewById(R.id.btnUpdate);

        idNeedUpdate = getIntent().getIntExtra("id_need_update",-1);
        titleNeedUpdate = getIntent().getStringExtra("title_need_update");
        desNeedUpdate = getIntent().getStringExtra("des_need_update");
        photoNeedUpdate = getIntent().getByteArrayExtra("photo_need_update");

        editTextTitleUp.setText(titleNeedUpdate);
        editTextMultiDesUp.setText(desNeedUpdate);
        imageViewUp.setImageBitmap(BitmapFactory.decodeByteArray(photoNeedUpdate,0,photoNeedUpdate.length));

       imageViewUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent iAccessImageData = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(iAccessImageData,5);
           }
       });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }
    public void updateData(){
        if(idNeedUpdate == -1){
            Toast.makeText(UpdatePhotoActivity.this,"Wrong ID !",Toast.LENGTH_LONG);
        }
        else{ //send to MainAct

            String updateTitleUser = editTextTitleUp.getText().toString();
            String updateDesUser = editTextMultiDesUp.getText().toString();
            Intent iSendUpdatedDataToDB = new Intent();
            iSendUpdatedDataToDB.putExtra("updated_id",idNeedUpdate);
            iSendUpdatedDataToDB.putExtra("updated_title_user", updateTitleUser);
            iSendUpdatedDataToDB.putExtra("updated_des_user",updateDesUser);

            if(selectedImage==null){
               iSendUpdatedDataToDB.putExtra("updated_image",photoNeedUpdate);
            }
            else {

                //convert image to byte data type
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaledImage = reduceUploadedImageToSmallerSize(selectedImage, 300);
                scaledImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] imageConverted = byteArrayOutputStream.toByteArray();
                iSendUpdatedDataToDB.putExtra("updated_image",imageConverted);
            }
            setResult(RESULT_OK,iSendUpdatedDataToDB);
            finish();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 5 && resultCode == RESULT_OK && data != null){
            try {
                if(Build.VERSION.SDK_INT >= 28)
                {
                    ImageDecoder.Source sourceImageUser = ImageDecoder.createSource(this.getContentResolver(),data.getData());
                    selectedImage = ImageDecoder.decodeBitmap(sourceImageUser);
                }
                else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                }
                imageViewUp.setImageBitmap(selectedImage);
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