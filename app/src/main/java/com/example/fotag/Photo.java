package com.example.fotag;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class Photo implements Parcelable {
    private float stars;
    private String image;
    private Bitmap bitmap;
    private PhotoAdapter.MyViewHolder holder;

    public Photo(int stars, String image){
        this.stars = stars;
        this.image = image;
    }
    public Photo(Bitmap b){
        this.bitmap = b;
        this.stars = 5;
    }

    protected Photo(Parcel in) {
        stars = in.readFloat();
        //image = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getImage() {
        return image;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.stars);
        dest.writeParcelable(bitmap, flags );
    }
}
