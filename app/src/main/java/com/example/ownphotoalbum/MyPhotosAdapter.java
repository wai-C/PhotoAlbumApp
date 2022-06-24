package com.example.ownphotoalbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosAdapter.PhotoCardViewHodler> {

    private List<MyPhotos> photosList = new ArrayList<>();
    private onPhotoClickListener onPhotoClickListener; //For update


    public void setPhotosList(List<MyPhotos> photosList) {
        this.photosList = photosList;
        notifyDataSetChanged();
    }

    //For update
    public void setOnPhotoClickListener(MyPhotosAdapter.onPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public interface onPhotoClickListener{
        void onPhotoClick(MyPhotos photos);
    }

    @NonNull
    @Override
    //Add the photo card desgin
    public PhotoCardViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_card,parent,false);
        return new PhotoCardViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCardViewHodler holder, int position) {
        MyPhotos myPhotos = photosList.get(position);
        holder.textViewTitle.setText(myPhotos.getPhoto_title());
        holder.textViewDes.setText(myPhotos.getPhoto_description());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myPhotos.getPhotos(),
                0,myPhotos.getPhotos().length));

    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public MyPhotos getPosition(int position){ return photosList.get(position);}

    public class PhotoCardViewHodler extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textViewTitle, textViewDes;


        public PhotoCardViewHodler(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDes = itemView.findViewById(R.id.textViewDes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(onPhotoClickListener != null && position != RecyclerView.NO_POSITION){
                        onPhotoClickListener.onPhotoClick(photosList.get(position));
                    }
                }
            });
        }


    }

}
