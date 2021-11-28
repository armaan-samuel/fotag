package com.example.fotag;

import android.util.Log;

import java.util.ArrayList;

public class Model {
    static Model instance = new Model();

    private ArrayList<Photo> photos;
    private  int curPhoto;
    private float curRating = 5;

    public static Model getInstance(){
        return  instance;
    }

    public ArrayList<Photo> getPhotos(){
        return this.photos;
    }
    public void setPhotos(ArrayList p){
        photos = p;

    }

    public void setCurPhoto(int i){
        curPhoto = i;
    }

    public int getCurPhoto(){
        return curPhoto;
    }

    public float getCurRating() { return curRating;}

    public void setCurRating(float r) {curRating = r;}


}
