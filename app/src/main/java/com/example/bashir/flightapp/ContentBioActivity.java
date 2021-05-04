package com.example.bashir.flightapp;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentBioActivity extends AppCompatActivity {
    private movieObject mO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String bio = getIntent().getStringExtra("bio");
        String title = getIntent().getStringExtra("title");
        String _id = getIntent().getStringExtra("_id");
        String bio = getIntent().getStringExtra("bio");
        mO = new movieObject(_id,title, bio, this);
        mO.getBioImage();


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //TextView tv = (TextView) findViewById(R.id.t)
        getSupportActionBar().hide();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        setContentView(R.layout.activity_content_bio);
        ImageView imgview = (ImageView) findViewById(R.id.imageView2);
        imgview.getLayoutParams().height = height/2;
        imgview.getLayoutParams().width = width;
        imgview.requestLayout();

        TextView titleTextView = (TextView) findViewById(R.id.bioTitleTextView);
        TextView bioTextView = (TextView) findViewById(R.id.contentBioTextView);
        titleTextView.setText(mO.title);
        bioTextView.setText(mO.bio);
        mO.imgV2 = imgview;
    }

    public void watchButton(View v) {
        Intent intent = new Intent(this, WatchActivity.class);
        intent.putExtra("_id" , mO._id);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
