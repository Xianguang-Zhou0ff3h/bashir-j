package com.example.bashir.flightapp;
//hello bashir qiunfqui nqiu nqeuif nqifnqifunq fiqwn iowqf nioqfn wfoiqwnfo
//qijqwijeqwije
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohammad ElNaofal on 4/9/2018.
 */

public class SignUpGenresActivity extends AppCompatActivity {
    String url = "http://192.168.1.115:3000/applicableGenres";
    String Serverresponse = null;
    Button btn_done, btn_back;
    TableLayout table ;
    Boolean TvSel, MovieSel, MusicSel;
    String Selected = "#872931";
    String UnSelected = "#ad343e";
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_genres);
        getSupportActionBar().hide();
        TvSel = getIntent().getBooleanExtra("tvSelected", false);
        MovieSel = getIntent().getBooleanExtra("movieSelected", false);
        MusicSel = getIntent().getBooleanExtra("musicSelected", false);
        btn_done = (Button)findViewById(R.id.btn_done);
        btn_back = (Button)findViewById(R.id.btn_backk);
        table = (TableLayout)findViewById(R.id.table_genre);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpCategoryActivity.class);
                startActivity(intent);
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), .class);
                // startActivity(intent);
            }
        });

        sendPost();
    }

    public void sendPost() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            displayGenres(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("categories1", "1");
                params.put("categories2", "2");
                return params;
            }
        };
        queue.add(postRequest);

    }


    public void displayGenres(String response) throws JSONException {
        JSONArray mainObject = new JSONArray(response);
        String _id;
        String genre;
        Boolean selected = false;
        int IDKEY = R.string.ids;
        final int SELKEY = R.string.selected;
        //int IDKEY = 1;
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> genres = new ArrayList<String>();
        for(int i = 0; i<mainObject.length();i++) {
            _id = (((JSONObject) mainObject.get(i)).getString("_id"));
            genre = (((JSONObject) mainObject.get(i)).getString("genre"));
            ids.add(i, _id);
            genres.add(i, genre);
        }
        int counter = 0;

        Boolean isOdd = ids.size()%2 != 0 ;
        for(int i = 0; i<(ids.size()/2);i++) {
            TableRow tr = new TableRow(this);
            //for(int j = i)
            final Button b1 = new Button(this);

            b1.setText(genres.get(counter));
            b1.setTag(IDKEY,ids.get(counter));
            b1.setTag(SELKEY,selected);
            b1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // change color
                        if (!(Boolean)b1.getTag(SELKEY)) {
                            b1.setBackgroundColor(Color.parseColor(Selected));
                            b1.setTag(SELKEY,true);
                        }
                        else {
                            b1.setBackgroundColor(Color.parseColor(UnSelected));
                            b1.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            b1.setBackgroundResource(R.drawable.custombutton);
            setMargins(b1,5,5,5,5);
            counter++;
            final Button b2 = new Button(this);
            b2.setText(genres.get(counter));

            b2.setText(genres.get(counter));
            b2.setTag(IDKEY,ids.get(counter));
            b2.setTag(SELKEY,selected);
            b2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // change color
                        if (!(Boolean)b2.getTag(SELKEY)) {
                            b2.setBackgroundColor(Color.parseColor(Selected));
                            b2.setTag(SELKEY,true);
                        }
                        else {
                            b2.setBackgroundColor(Color.parseColor(UnSelected));
                            b2.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            counter++;
            b2.setBackgroundResource(R.drawable.custombutton);
            setMargins(b2,5,5,5,5);
            tr.addView(b1);//add row to table and add buttons
            tr.addView(b2);
            table.addView(tr);
            setMargins(b1,10,10,10,10);
            setMargins(b2,10,10,10,10);

        }
        //if (counter == ids.size() -1 ){ break; }
        if (isOdd){
        //add one row and one button here to table
            TableRow tr = new TableRow(this);
            final Button b1 = new Button(this);

            b1.setText(genres.get(counter));
            b1.setTag(IDKEY,ids.get(counter));
            b1.setTag(SELKEY,selected);
            b1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // change color
                        if (!(Boolean)b1.getTag(SELKEY)) {
                            b1.setBackgroundColor(Color.parseColor(Selected));
                            b1.setTag(SELKEY,true);
                        }
                        else {
                            b1.setBackgroundColor(Color.parseColor(UnSelected));
                            b1.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            b1.setBackgroundResource(R.drawable.custombutton);
            setMargins(b1,5,5,5,5);
            tr.addView(b1);
            table.addView(tr);
            setMargins(b1,5,5,5,5);
        }

    }
    public void doneButton(View v) {
        //sendPost(username.getText().toString(),password.getText().toString());
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }
    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }
}
