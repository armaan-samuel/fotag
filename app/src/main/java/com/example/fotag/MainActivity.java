package com.example.fotag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    //private ArrayList<Photo> photos;
    private PhotoAdapter pa;
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView = null;
    ProgressDialog p;
    String base = "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/";
    String [] endpoints = {"bunny.jpg", "chinchilla.jpg", "deer.jpg", "doggo.jpg", "ducks.jpg",
    "fox.jpg", "hamster.jpg", "hedgehog.jpg", "husky.jpg", "kitten.png", "loris.jpg", "puppy.jpg",
    "running.jpg", "sleepy.png"};
    RatingBar rb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        ImageButton loadMany = (ImageButton) findViewById(R.id.loadMany);
        loadMany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoTask pt = new PhotoTask();
                for (String endpoint : endpoints){
                    //pt.execute(base + endpoint);
                    new PhotoTask().execute(base + endpoint);
                }

                //getData();
            }

        });
        loadMany.setImageResource(R.drawable.hamburger);
        loadMany.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageButton clear = (ImageButton) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //photos = new ArrayList<>();
                //photos.clear();
                Model.getInstance().getPhotos().clear();
                pa.notifyDataSetChanged();
            }
        });
        clear.setImageResource(R.drawable.clear);
        clear.setScaleType(ImageView.ScaleType.FIT_XY);

        ImageButton url = (ImageButton) findViewById(R.id.load);
        url.setImageResource(R.drawable.url);
        url.setScaleType(ImageView.ScaleType.FIT_XY);

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txturl = new EditText(MainActivity.this);
                txturl.setHint("URL");
                final AlertDialog.Builder ab = new  AlertDialog.Builder(MainActivity.this)
                        .setView(txturl)
                        .setMessage("Enter the URL of an image")
                        .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = txturl.getText().toString();
                                new PhotoTask().execute(str);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                ab.show();
            }
        });



        rv = (RecyclerView) findViewById(R.id.rv);
        //CardView cv = (CardView) findViewById(R.id.cv);
        //photos = savedInstanceState.getParcelableArrayList("photos");
//        if (photos == null){
//            photos = new ArrayList<>();
//        }
        if(Model.getInstance().getPhotos() == null){
            Model.getInstance().setPhotos(new ArrayList<>());
        }

        //RecyclerView.LayoutManager lm = new GridLayoutManager(this, 1);

        rb = (RatingBar) findViewById(R.id.ratingBar2);
        //rb.setRating(5.0f);
        rb.setRating(Model.getInstance().getCurRating());
        //Model.getInstance().setCurRating(5.0f);
        //pa = new PhotoAdapter(this, photos, rb);
        pa = new PhotoAdapter(this, Model.getInstance().getPhotos(), rb);
//        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
//        rv.setLayoutManager(lm);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            rv.setLayoutManager(lm);

        } else {
            // code for landscape mode
            RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2);
            //lm.centre;
            rv.setLayoutManager(lm);

        }

        rv.setAdapter(pa);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Model.getInstance().setCurRating(rating);
                pa.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Model.getInstance().setCurRating(rb.getRating());
        //savedInstanceState.putParcelableArrayList("photos", photos);
        //savedInstanceState.putExtra();
        //savedInstanceState

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
            rv.setLayoutManager(lm);

        } else {
            // code for landscape mode
            RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2);
            //lm.centre;
            rv.setLayoutManager(lm);

        }

        rb.setRating(Model.getInstance().getCurRating());
        //photos = savedInstanceState.getParcelableArrayList("photos");
        //pa.setPhotoList(photos);
        pa.setPhotoList(Model.getInstance().getPhotos());
        pa.notifyDataSetChanged();
    }

    private class PhotoTask extends AsyncTask<String, String, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                bmImg = null;
                ImageUrl = new URL(strings[0]);
                Log.d("URL", strings[0]);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);
            } catch (IOException e) {
                //System.out.println("ERROR");
                Log.e("BITMAP", e.getMessage());
                e.printStackTrace();
            }
            return bmImg;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {


            //photos.add(new Photo(bitmap));
            if (bitmap != null){
                super.onPostExecute(bitmap);
                Model.getInstance().getPhotos().add(new Photo(bitmap));
                pa.notifyDataSetChanged();
            }
            else{

                TextView tv = new TextView(MainActivity.this);
                tv.setText("Photo Not Found");
                tv.setGravity(Gravity.CENTER);
                final AlertDialog.Builder ab = new  AlertDialog.Builder(MainActivity.this)
                        .setView(tv)
                        .setMessage("ERROR!")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                ab.show();
            }



        }




    }
}
