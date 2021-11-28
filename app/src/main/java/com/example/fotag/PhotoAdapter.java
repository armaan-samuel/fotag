package com.example.fotag;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photoList;
    private Context context;
    private ImageView iv;
    private  RatingBar ratingBar;



    PhotoAdapter(Context c, List<Photo> p, RatingBar rb){
        this.photoList = p;
        this.context = c;
        this.ratingBar = rb;
    }

    public void setPhotoList(List<Photo>pl){
        this.photoList = pl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Photo p = photoList.get(position);
        holder.setIsRecyclable(false);

        //holder.tv.setText(p.getImage());
        holder.iv.setImageBitmap(p.getBitmap());
        holder.iv.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent it = new Intent(v.getContext(), PhotoActivity.class);
                Intent it = new Intent(v.getContext(), PhotoActivity.class);
                Model.getInstance().setCurPhoto(position);
                //it.putExtra("photo", p);
                //photoList.clear();
                v.getContext().startActivity(it);
            }
        });

        //holder.rb.setNumStars(5);
        holder.rb.setRating(p.getStars());
        holder.rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                p.setStars(rating);
                Log.d("STAR", String.valueOf(rating));
                if (ratingBar.getRating() > p.getStars()){
                    holder.cv.setVisibility(View.GONE);
                    //Phota
                    update();
                }
                else {
                    holder.cv.setVisibility(View.VISIBLE);
                    //.notifyDataSetChanged();
                    update();
                }
            }
        });

        if (ratingBar.getRating() > p.getStars()){
            holder.cv.setVisibility(View.GONE);
            //this.notifyDataSetChanged();
        }
        else {
            holder.cv.setVisibility(View.VISIBLE);
            //this.notifyDataSetChanged();
        }


    }
    private void update(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //TextView tv;
        CardView cv;
        ImageView iv;
        RatingBar rb;
        //private final Context ct;
        MyViewHolder(View view){

            super(view);
            //ct = view.getContext();
            cv = (CardView) view.findViewById(R.id.cv);
            //tv = (TextView) view.findViewById(R.id.textView2);
            iv = (ImageView) view.findViewById(R.id.imageView);
            rb = (RatingBar) view.findViewById(R.id.ratingBar);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
