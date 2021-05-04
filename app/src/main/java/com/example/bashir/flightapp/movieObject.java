package com.example.bashir.flightapp;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.io.Serializable;

public class movieObject  {
    String _id;
    String title;
    int width;
    String category;
    Bitmap thumbnail;
    Bitmap bioImage;
    String artist;
    String ageLimit;
    ImageView imgV;
    ImageView imgV2;
    TextView txtV;
    Context context;
    String bio;
    movieObject(String _id, String title, String bio, Context context){
        this._id=_id;
        this.title=title;
        this.bio = bio;
        this.context = context;
    }

    public void getThumbnail(){
        new DownloadImageTask(this.thumbnail , true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context.getApplicationContext().getString(R.string.ip) + "/image/" + this._id );
        //new DownloadImageTask(this.thumbnail , true).execute("http://192.168.1.115:3000/thumbnail/" + this._id + "/150x150");
    }

    public void getBioImage(){
        new DownloadImageTask(this.thumbnail, false).execute(context.getApplicationContext().getString(R.string.ip) + "/image/" + this._id);
    }

    public void setThumbnail(Bitmap result){

        double ratio = result.getHeight() * 1.0 / result.getWidth();
        double fixedRatio;
        if(ratio > 1.1){
            fixedRatio = 1.5;
        }
        else{
            fixedRatio = 1;
        }
        int newWidth = width/4;
        int newHeight = (int) (newWidth*fixedRatio);
        Log.d(this.title, String.valueOf(ratio));
        Log.d(this.title, String.valueOf(newWidth));
        Log.d(this.title, String.valueOf(newHeight));
        this.thumbnail = Bitmap.createScaledBitmap(result,newWidth,newHeight,false);

        txtV.setVisibility(View.VISIBLE);
        this.imgV.setImageBitmap(this.thumbnail);
    }
    public void setBioImage(Bitmap result){
        this.bioImage = result;
        this.imgV2.setImageBitmap(this.bioImage);
    }

    public void setImageView(ImageView imgV){
        this.imgV = imgV;
        getThumbnail();
    }

    public void setTextView(TextView txtV){
        this.txtV = txtV;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap thumb;
        Boolean getSmall;

        public DownloadImageTask(Bitmap thumbnail, Boolean getSmall) {
            this.thumb = thumbnail;
            this.getSmall = getSmall;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            Log.d("Prog","Start");
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d("Prog","Finish");
            if(getSmall) {
                setThumbnail(result);
            }
            else{
                setBioImage(result);
            }
        }
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
