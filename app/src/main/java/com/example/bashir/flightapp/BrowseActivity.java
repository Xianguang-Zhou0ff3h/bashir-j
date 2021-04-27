package com.example.bashir.flightapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseActivity extends AppCompatActivity {

    GridView gv;
    ArrayList gA, cA;
    ArrayList<HashMap<String, String>> allGenres;
    ArrayList<HashMap<String, String>> allCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_browse);
        getSupportActionBar().hide();


        allGenres = new ArrayList<HashMap<String, String>>();
        allCategories = new ArrayList<HashMap<String, String>>();


        //getActionBar()
        gA = new ArrayList();
        cA = new ArrayList();

        getAllGenresPOST();
        //getAllCategoriesPOST();
        sendPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void sendPost() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/getContent";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            displayTitles(response);
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

                String genreArray =  gA.toArray().toString();
                String catArray = gA.toArray().toString();
                try {
                    jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("category", catArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("hey","hi");
                //params.put("name", "Alif");
                //params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        queue.add(postRequest);

    }


    public void getAllGenresPOST() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/getContent";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            displayTitles(response);
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

                String genreArray =  gA.toArray().toString();
                String catArray = gA.toArray().toString();
                try {
                    jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("category", catArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("hey","hi");
                //params.put("name", "Alif");
                //params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        queue.add(postRequest);

    }


    void displayTitles(String response) throws JSONException {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ArrayList<movieObject> arraylist = new ArrayList<movieObject>();
        JSONArray mainObject = new JSONArray(response);
        movieObject mO;
        String _id;
        String title;
        String bio;
        String category;
        for(int i = 0; i<mainObject.length();i++){
            _id = (((JSONObject)mainObject.get(i)).getString("_id"));
            title = (((JSONObject)mainObject.get(i)).getString("title"));
            bio = (((JSONObject)mainObject.get(i)).getString("bio"));
            mO = new movieObject(_id,title,bio);
            //mO.getThumbnail();
            mO.width = size.x;
            arraylist.add(i,mO);
        }
        gv = (GridView) findViewById(R.id.gridview);
        GridAdapter gAdapter = new GridAdapter(this, arraylist);

        gv.setAdapter(gAdapter);
    }

    public void showPopup(View v) {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_example, popup.getMenu());
        popup.show();
        decorView.setSystemUiVisibility(uiOptions);
    }


}
