package com.example.bashir.flightapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
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



        getAllGenresPOST();
        getAllCategoriesPOST();

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

    public void getContentPOST(final ArrayList cA, final int containerID) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/getContent";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            displayTitles(response, containerID);
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
                String catArray = cA.get(0).toString();
                try {
                    //jsonBodyObj.put("genre", genreArray);
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

        String url = getString(R.string.ip) + "/findfilters/genres";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseGenres(response);
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


    void displayTitles(String response, int containerID) throws JSONException {
        HorizontalScrollView sv = (HorizontalScrollView) findViewById(containerID);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
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

            LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.video_tile, null);
            ((TextView) view.findViewById(R.id.titleTextView)).setText(mO.title);
            mO.imgV = ((ImageView) view.findViewById(R.id.imageView));
        }

    }

    void parseGenres(String response) throws JSONException {

        JSONArray mainObject = new JSONArray(response);
        String _id;
        String genre;
        for(int i = 0; i<mainObject.length();i++){
            HashMap<String, String> hashMap = new HashMap<>();
            _id = (((JSONObject)mainObject.get(i)).getString("_id"));
            genre = (((JSONObject)mainObject.get(i)).getString("genre"));
            hashMap.put(_id,genre);

            allGenres.add(hashMap);
        }

    }

    void parseCategories(String response) throws JSONException {

        JSONArray mainObject = new JSONArray(response);
        String _id;
        String category;
        for(int i = 0; i<mainObject.length();i++){
            HashMap<String, String> hashMap = new HashMap<>();
            _id = (((JSONObject)mainObject.get(i)).getString("_id"));
            category = (((JSONObject)mainObject.get(i)).getString("category"));
            hashMap.put(_id,category);

            allCategories.add(hashMap);
        }
        ArrayList cA1 = new ArrayList();
        ArrayList cA2 = new ArrayList();
        ArrayList cA3 = new ArrayList();
        for (int i = 0; i < allCategories.size(); i++){
            if(allCategories.get(i).containsValue("Music")){
                cA1.add(allCategories.get(i).keySet().toArray()[0]);
                getContentPOST(cA1, R.id.containerMusic);
            }
            if(allCategories.get(i).containsValue("Movie")){
                cA2.add(allCategories.get(i).keySet().toArray()[0]);
                getContentPOST(cA2, R.id.containerMovies);
            }
            if(allCategories.get(i).containsValue("TV Series")){
                cA2.add(allCategories.get(i).keySet().toArray()[0]);
                getContentPOST(cA2, R.id.containerTV);
            }
        }


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
