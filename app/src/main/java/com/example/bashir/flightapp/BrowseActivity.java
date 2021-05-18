package com.example.bashir.flightapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseActivity extends AppCompatActivity {

    PopupWindow window;
    GridView gv;
    ArrayList<HashMap<String, String>> allGenres;
    ArrayList<HashMap<String, String>> allCategories;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Button filterGenresButton;
    HashMap catsHashMap;
    String UID;
    Boolean track;
    PopupMenu popup, popupGenres;
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
        prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();

        UID = prefs.getString("UID" , "null");

        catsHashMap = new HashMap();
        filterGenresButton = (Button) findViewById(R.id.filterGenresButton);

        popup = new PopupMenu(this,findViewById(R.id.filterCategoriesButton));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_example, popup.getMenu());

        popupGenres = new PopupMenu(this,findViewById(R.id.filterGenresButton));
        MenuInflater inflater2 = popupGenres.getMenuInflater();
        inflater2.inflate(R.menu.menu_example, popupGenres.getMenu());

        allGenres = new ArrayList<HashMap<String, String>>();
        allCategories = new ArrayList<HashMap<String, String>>();
        ArrayList tvs = new ArrayList();
        RelativeLayout containerHolder = (RelativeLayout)findViewById(R.id.containerHolder);
        for (int i = 0; i<containerHolder.getChildCount(); i++){
            Log.d("instance",String.valueOf(containerHolder.getChildAt(i) instanceof TextView));
            if (containerHolder.getChildAt(i) instanceof TextView && !(containerHolder.getChildAt(i) instanceof Button)) {
                containerHolder.getChildAt(i).setVisibility(View.GONE);
                tvs.add(containerHolder.getChildAt(i));
            }
            if (containerHolder.getChildAt(i) instanceof HorizontalScrollView) {
                containerHolder.getChildAt(i).setVisibility(View.GONE);
                Log.d("idk", String.valueOf(tvs.size()));
                containerHolder.getChildAt(i).setTag(tvs.get(0));
                tvs.clear();
            }
        }
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
        track = Boolean.parseBoolean(prefs.getString("track","false"));
        if (track) {
            HorizontalScrollView sv = (HorizontalScrollView) findViewById(R.id.containerRecommend);
            ((LinearLayout)sv.getChildAt(0)).removeAllViews();
            getRecommendations(R.id.containerRecommend);
        }else{
            HorizontalScrollView sv = (HorizontalScrollView) findViewById(R.id.containerRecommend);
            TextView upperText = (TextView) sv.getTag();
            upperText.setVisibility(View.GONE);
        }
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
                        displayError(cA, containerID);
                        Log.d("Error.Response", "error");
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
                JSONArray catArray = new JSONArray(cA);

                Log.d("array", catArray.toString());
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
                //params.put("param1","param2");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getRecommendations(final int containerID) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/getRecommendations";
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

                        Log.d("Error.Response", "error");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("param1","param2");
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

    public void getApplicableGenresPOST(final ArrayList cats , final String categoryLabel) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/applicableGenres";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            populateGenresPopup(response, categoryLabel);
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



    void displayTitles(String response, int containerID) throws JSONException {
        final Context context = this;
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
            mO = new movieObject(_id,title,bio,this);
            //mO.getThumbnail();
            mO.width = size.x;

            LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.video_tile, null);
            TextView titleTV = (TextView) view.findViewById(R.id.titleTextView);
            mO.setTextView(titleTV);
            titleTV.setText(mO.title);
            ImageView titleIV = (ImageView) view.findViewById(R.id.imageView);
            mO.setImageView(titleIV);
            titleIV.setClickable(true);
            final movieObject finalMO = mO;
            titleIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ContentBioActivity.class);
                    //intent.putExtra("bio",mO.bio);
                    intent.putExtra("title", finalMO.title);
                    intent.putExtra("_id", finalMO._id);
                    intent.putExtra("bio", finalMO.bio);
                    context.startActivity(intent);
                }
            });
            ((LinearLayout)sv.getChildAt(0)).addView(view);
            sv.setVisibility(View.VISIBLE);
            TextView upperText = (TextView) sv.getTag();
            upperText.setVisibility(View.VISIBLE);


        }
        //setContentView(R.layout.activity_browse);


    }

    void displayError(final ArrayList cA, final int containerID)  {
        HorizontalScrollView sv = (HorizontalScrollView) findViewById(containerID);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        movieObject mO;
        String _id;
        String title;
        String bio;
        String category;



        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.video_tile, null);
        ((TextView) view.findViewById(R.id.titleTextView)).setText("Error Loading Titles, Tap To Retry");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentPOST(cA, containerID);
            }
        });
        ((LinearLayout)sv.getChildAt(0)).addView(view);

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
            catsHashMap.put(category,_id);
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
                cA3.add(allCategories.get(i).keySet().toArray()[0]);
                getContentPOST(cA3, R.id.containerTV);
            }
        }


    }


    public void showPopupCategories(View v) {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);




        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        });

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                item.setChecked(!item.isChecked());

                switch (item.toString()){

                    case "Movies" :
                                    break;
                    case "TV Series" :
                                        break;
                    case "Music" :
                        break;
                    default :
                        break;
                }
                int counter = 0;
                popupGenres.getMenu().clear();
                for(int i= 0; i< popup.getMenu().size(); i++){

                    MenuItem MenItem = popup.getMenu().getItem(i);

                    if(MenItem.isChecked()){

                        String filterCatID = (String) catsHashMap.get(MenItem.toString());
                        ArrayList catID1 = new ArrayList();
                        catID1.add(filterCatID);
                        getApplicableGenresPOST(catID1, MenItem.toString());
                        counter++;
                    }
                }
                if (counter == 0){
                    filterGenresButton.setVisibility(View.GONE);
                }else{
                    filterGenresButton.setVisibility(View.VISIBLE);
                }


                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(new View(getApplicationContext()));
                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });
                return false;
            }
        });
        decorView.setSystemUiVisibility(uiOptions);
        popup.show();
    }

    public void showPopupGenres(View v) {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);




        popupGenres.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        });

        popupGenres.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                item.setChecked(!item.isChecked());


//                for(int i= 0; i< popupGenres.getMenu().size(); i++){
//                    MenuItem MenItem = popupGenres.getMenu().getItem(i);
//                    if(MenItem.isChecked()){
//                        String filterCatID = catsHashMap.get(MenItem.toString()).toString();
//                        ArrayList catID1 = new ArrayList();
//                        catID1.add(filterCatID);
//                        getApplicableGenresPOST(catID1);
//                    }
//                }


                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(new View(getApplicationContext()));
                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });
                return false;
            }
        });
        decorView.setSystemUiVisibility(uiOptions);
        popupGenres.show();
    }

    public void populateGenresPopup(String response, String categoryLabel) throws JSONException {
        JSONArray mainObject = new JSONArray(response);
        String _id;
        String genre;
        popupGenres.getMenu().add(categoryLabel).setEnabled(false);
        for(int i = 0; i<mainObject.length();i++) {
            _id = (((JSONObject) mainObject.get(i)).getString("_id"));
            genre = (((JSONObject) mainObject.get(i)).getString("genre"));
            popupGenres.getMenu().add(genre).setCheckable(true).getActionView().setTag(genre);
        }
    }


    public void showMenu(View v) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        LayoutInflater inflater = (LayoutInflater) BrowseActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_menu, null);
        int windowWidth = (int) Math.floor(size.x/2);
        int windowHeight =(int)Math.floor(size.y/2);
        window = new PopupWindow(layout, windowWidth,  windowHeight, true);
        //window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //window.dismiss();
    }

    public void goToProfile(View v) {
        window.dismiss();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void logout(View v) {
        window.dismiss();
        finish();
    }


}
