package com.example.bashir.flightapp;
//hello bashir qiunfqui nqiu nqeuif nqifnqifunq fiqwn iowqf nioqfn wfoiqwnfo
//qijqwijeqwije
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    String url;
    String Serverresponse = null;
    Button btn_done;
    TableLayout table ;
    Boolean TvSel, MovieSel, MusicSel;
    String Selected = "#872931";
    String UnSelected = "#ad343e";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_genres);
        getSupportActionBar().hide();
        url = getString(R.string.ip) + "/applicableGenres";
        TvSel = getIntent().getBooleanExtra("tvSelected", false);
        MovieSel = getIntent().getBooleanExtra("movieSelected", false);
        MusicSel = getIntent().getBooleanExtra("musicSelected", false);
        btn_done = (Button)findViewById(R.id.btn_done);
        table = (TableLayout)findViewById(R.id.table_genre);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), .class);
                // startActivity(intent);
            }
        });

        sendPost();

        String response = "[\n" +
                "    {\n" +
                "        \"_id\": \"5a871058f963815e7b73b2aa\",\n" +
                "        \"genre\": \"Classic\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871061f963815e7b73b2ab\",\n" +
                "        \"genre\": \"Crime\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a87106bf963815e7b73b2ac\",\n" +
                "        \"genre\": \"Mythology\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871072f963815e7b73b2ad\",\n" +
                "        \"genre\": \"Thriller\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871079f963815e7b73b2ae\",\n" +
                "        \"genre\": \"Comedy\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871080f963815e7b73b2af\",\n" +
                "        \"genre\": \"Mystery\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871087f963815e7b73b2b0\",\n" +
                "        \"genre\": \"Western\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871092f963815e7b73b2b1\",\n" +
                "        \"genre\": \"Horror\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5a871099f963815e7b73b2b2\",\n" +
                "        \"genre\": \"Fantasy\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac3292b7dd6aecf4adbee24\",\n" +
                "        \"genre\": \"Contemporary R&B\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac329447dd6aecf4adbee49\",\n" +
                "        \"genre\": \"R&B/soul\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac329647dd6aecf4adbee60\",\n" +
                "        \"genre\": \"Alternative R&B\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac329b37dd6aecf4adbeec9\",\n" +
                "        \"genre\": \"Pop\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac329e17dd6aecf4adbeef1\",\n" +
                "        \"genre\": \"Trap Music\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac32a167dd6aecf4adbef2b\",\n" +
                "        \"genre\": \"Downtempo\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"_id\": \"5ac32a2e7dd6aecf4adbef4d\",\n" +
                "        \"genre\": \"Trip hop\"\n" +
                "    }\n" +

                "]";
        try {
            displayGenres(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            b1.setPressed(true);
                            b1.setTag(SELKEY,true);
                        }
                        else {
                            b1.setPressed(false);
                            b1.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            b1.setBackgroundResource(R.drawable.custombutton2);

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
                            b2.setPressed(true);
                            b2.setTag(SELKEY,true);
                        }
                        else {
                            b2.setPressed(false);
                            b2.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            counter++;
            b2.setBackgroundResource(R.drawable.custombutton2);
            tr.addView(b1);//add row to table and add buttons
            tr.addView(b2);
            table.addView(tr);

            setWeights(b1);
            setWeights(b2);
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
                            b1.setPressed(true);
                            //b1.setSelected(true);
                            b1.setTag(SELKEY,true);
                        }
                        else {
                            b1.setPressed(false);
                            b1.setTag(SELKEY,false);
                        }
                    }
                    return true;
                }
            });
            b1.setBackgroundResource(R.drawable.custombutton2);
            tr.addView(b1);
            table.addView(tr);
            setWeightsIndividual(b1);
            setMargins(b1,10,10,10,10);
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
    public static void setWeights (View v) {
            TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
            p.weight = 1;
            v.setLayoutParams(p);
            v.requestLayout();

    }
    public static void setWeightsIndividual (View v) {
        TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        p.weight = 0;
        p.gravity = Gravity.CENTER;
        p.span = 2;
        v.setLayoutParams(p);
        v.requestLayout();

    }
}
