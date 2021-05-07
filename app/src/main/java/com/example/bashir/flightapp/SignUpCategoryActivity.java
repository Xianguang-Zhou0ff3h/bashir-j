package com.example.bashir.flightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mohammad ElNaofal on 4/3/2018.
 */

public class SignUpCategoryActivity extends AppCompatActivity{
    Button btn_next, btn_tv,btn_music,btn_movies;
    String Selected = "#872931";
    String UnSelected = "#ad343e";
    Boolean TVselected = false, Moviesselected = false , Musicselected = false;
    SharedPreferences shared ;
    Intent previntent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_category);
        getSupportActionBar().hide();

        previntent = getIntent();

        //192.168.1.115:300/findfilters/categories
        btn_next = (Button)findViewById(R.id.btn_next);

        btn_tv = (Button)findViewById(R.id.btn_tvshows);
        btn_movies = (Button)findViewById(R.id.btn_movies);
        btn_music = (Button)findViewById(R.id.btn_musicvideo);
        shared = PreferenceManager.getDefaultSharedPreferences(this);

        btn_movies.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    if (!Moviesselected) {
                        //btn_movies.setBackgroundColor(Color.parseColor(Selected));
                        btn_movies.setPressed(true);
                        Moviesselected = true;
                    }
                    else {
                        //btn_movies.setBackgroundColor(Color.parseColor(UnSelected));
                        btn_movies.setPressed(false);
                        Moviesselected = false;
                    }
                }
                return true;
            }
        });
        btn_music.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    if (!Musicselected) {
                        btn_music.setPressed(true);
                        Musicselected = true;
                    }
                    else {
                        btn_music.setPressed(false);
                        Musicselected = false;
                    }
                }
                return true;
            }
        });
        btn_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    if (!TVselected) {
                        btn_tv.setPressed(true);
                        TVselected = true;
                    }
                    else {
                        btn_tv.setPressed(false);
                        TVselected = false;
                    }
                }
                return true;
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpGenresActivity.class);
                intent.putExtra("username",previntent.getStringExtra("username"));
                intent.putExtra("password",previntent.getStringExtra("password"));
                intent.putExtra("firstname",previntent.getStringExtra("firstname"));
                intent.putExtra("lastname",previntent.getStringExtra("lastname"));
                intent.putExtra("tvSelected", TVselected);
                intent.putExtra("movieSelected", Moviesselected);
                intent.putExtra("musicSelected", Musicselected);

                startActivity(intent);
            }
        });


    }
    @Override
    protected void onPause() {
        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean("tv", TVselected);
        edit.putBoolean("movie", Moviesselected);
        edit.putBoolean("music", Musicselected);
        edit.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TVselected = shared.getBoolean("tv",false);
        Moviesselected = shared.getBoolean("movie",false);
        Musicselected = shared.getBoolean("music",false);
        if (TVselected)  btn_tv.setPressed(true);
        if (Moviesselected)  btn_movies.setPressed(true);
        if (Musicselected)  btn_music.setPressed(true);

    }
}

