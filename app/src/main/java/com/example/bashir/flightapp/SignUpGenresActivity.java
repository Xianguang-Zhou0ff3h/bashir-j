package com.example.bashir.flightapp;

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

import com.android.volley.AuthFailureError;
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



public class SignUpGenresActivity extends AppCompatActivity {
    String Serverresponse = null;
    Button btn_done;
    TableLayout table ;
    Boolean TvSel, MovieSel, MusicSel;
    String UID;
    int IDKEY = R.string.ids;
    final int SELKEY = R.string.selected;
    ArrayList cats;
    ArrayList<String> ids;
    ArrayList<String> genres;
    ArrayList statuses, allCategories;
    HashMap<String, String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_genres);
        getSupportActionBar().hide();
        TvSel = getIntent().getBooleanExtra("tvSelected", false);
        MovieSel = getIntent().getBooleanExtra("movieSelected", false);
        MusicSel = getIntent().getBooleanExtra("musicSelected", false);

        UID = getIntent().getStringExtra("uid");
        btn_done = (Button)findViewById(R.id.btn_done);
        table = (TableLayout)findViewById(R.id.table_genre);

        hashMap = new HashMap<>();
        getAllCategoriesPOST();



    }

    public void getAllCategoriesPOST() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/findfilters/categories";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseCategories(response);
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
                //params.put("hey","hi");
                //params.put("name", "Alif");
                //params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getGenresPOST(final ArrayList cats) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/applicableGenres";

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBodyObj = new JSONObject();
                //String genreArray =  gA.toArray().toString();
                JSONArray jsonCats = new JSONArray(cats);
                try {
                    //jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("categories", jsonCats);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }
        };
        queue.add(postRequest);

    }

    public void sendPrefsPOST() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/insertPreferences";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        backToLogin();

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonBodyObj = new JSONObject();
                //String genreArray =  gA.toArray().toString();

                try {
                    //jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("uid", UID);
                    jsonBodyObj.put("category", cats.toString());
                    jsonBodyObj.put("genre", statuses.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }
        };
        queue.add(postRequest);

    }

    void parseCategories(String response) throws JSONException {
        allCategories = new ArrayList();
        JSONArray mainObject = new JSONArray(response);
        String _id;
        String category;
        for(int i = 0; i<mainObject.length();i++){
            _id = (((JSONObject)mainObject.get(i)).getString("_id"));
            category = (((JSONObject)mainObject.get(i)).getString("category"));
            hashMap.put(category,_id);


        }

        cats = new ArrayList();
        if (MovieSel){
            cats.add(hashMap.get("Movie"));
        }
        if (MusicSel){
            cats.add(hashMap.get("Music"));
        }
        if (TvSel){
            cats.add(hashMap.get("TV Series"));
        }

        getGenresPOST(cats);
    }



    public void displayGenres(String response) throws JSONException {
        JSONArray mainObject = new JSONArray(response);
        String _id;
        String genre;
        Boolean selected = false;
        statuses = new ArrayList();
        //int IDKEY = 1;
        ids = new ArrayList<String>();
        genres = new ArrayList<String>();
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
                            statuses.add(b1.getTag(IDKEY));
                        }
                        else {
                            b1.setPressed(false);
                            b1.setTag(SELKEY,false);
                            statuses.remove(b1.getTag(IDKEY));
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
                            statuses.add(b2.getTag(IDKEY));
                        }
                        else {
                            b2.setPressed(false);
                            b2.setTag(SELKEY,false);
                            statuses.remove(b2.getTag(IDKEY));
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
                            statuses.add(b1.getTag(IDKEY));
                        }
                        else {
                            b1.setPressed(false);
                            b1.setTag(SELKEY,false);
                            statuses.remove(b1.getTag(IDKEY));
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
//        Intent intent = new Intent(this, BrowseActivity.class);
//        startActivity(intent);
        Log.d("statuses", statuses.toString());
        sendPrefsPOST();
    }

    public void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
