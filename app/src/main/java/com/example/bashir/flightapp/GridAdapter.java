package com.example.bashir.flightapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater layoutInflator;
    private String response;
    private ArrayList arraylist;
    //private Integer[] mThumbnails = {R.drawable.kunfu1,R.drawable.kunfu2,R.drawable.kunfu3,R.drawable.kunfu4};

    public GridAdapter(Context c,ArrayList<movieObject> array){
        context = c;
        layoutInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arraylist = array;


    }

    public int getCount() {
        //return mThumbnails.length;
        if(arraylist != null) {
            //return mainObject.length();
            return arraylist.size();
        }
        else{
            return 0;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View grid, ViewGroup parent) {
        final movieObject mO = (movieObject) arraylist.get(position);
        TextView titleTextView;
        ImageView tileImageView;
        String id = null;
        String imgurl = null;
        Boolean newGrid = false;
        if(grid == null) {
            grid = layoutInflator.inflate(R.layout.video_tile, null);
            titleTextView =  (TextView)grid.findViewById(R.id.titleTextView);
            tileImageView = (ImageView)grid.findViewById(R.id.imageView) ;
            grid.setTag(R.id.titleTextView,titleTextView);
            grid.setTag(R.id.imageView,tileImageView);
            titleTextView.setVisibility(View.INVISIBLE);
            newGrid=true;
        }else{
            titleTextView = (TextView) grid.getTag(R.id.titleTextView);
            tileImageView = (ImageView) grid.getTag(R.id.imageView);
            //tileImageView.setImageBitmap(mO.thumbnail);
        }

        grid.setClickable(true);
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ContentBioActivity.class);
                //intent.putExtra("bio",mO.bio);
                intent.putExtra("title",mO.title);
                intent.putExtra("_id",mO._id);
                intent.putExtra("bio",mO.bio);
                context.startActivity(intent);
            }
        });


        //imgurl = (((JSONObject)mainObject.get(position)).getString("150x150"));

        titleTextView.setText(mO.title);

        Log.d("try",mO._id);
        //tileImageView.setImageBitmap(mO.thumbnail);
        mO.setImageView(tileImageView);
        if(newGrid){
            mO.getThumbnail();
        }
        mO.setTextView(titleTextView);
        //downloadImage(tileImageView,"http://192.168.1.115:3000/music_video/" + id + "/150x150");

        //new DownloadImageTask(tileImageView).execute("http://www.lifetreecafe.com/wp-content/uploads/2013/03/image-alignment-150x150.jpg");
        return grid;

    }




    public void downloadImage(ImageView imgView, String imgURL){
        Bitmap bitmap = null;
        try {

            URL urlImage = new URL(imgURL);
            HttpURLConnection connection = (HttpURLConnection) urlImage
                    .openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            imgView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
