package com.example.fotag;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {

    RatingBar rb;
    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        final Photo curphoto = Model.getInstance().getPhotos().get(Model.getInstance().getCurPhoto());

        rb = (RatingBar) findViewById(R.id.prb);
        iv = (ImageView) findViewById(R.id.piv);

        rb.setRating(curphoto.getStars());
        iv.setImageBitmap(curphoto.getBitmap());

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                curphoto.setStars(rating);
            }
        });


    }
}
