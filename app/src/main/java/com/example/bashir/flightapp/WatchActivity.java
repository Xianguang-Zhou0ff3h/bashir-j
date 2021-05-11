package com.example.bashir.flightapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WatchActivity extends AppCompatActivity {
    String UID;
    String contentId;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = getIntent().getStringExtra("_id");

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_watch);
        getSupportActionBar().hide();


        videoView = (VideoView) findViewById(R.id.videoView);
        prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();

        UID = prefs.getString("UID" , "null");
        sendLogPOST(UID, contentId);


    }

    public void startAdPlayer(String response) {
        try {
            String link = getString(R.string.ip) + "/streamaAD/" + response;
            Uri vidUri = Uri.parse(link);
            videoView.setVideoURI(vidUri);

            videoView.start();
            videoView.getHolder().setSizeFromLayout();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    startContentPlayer();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    public void startContentPlayer() {
        try {
            String link = getString(R.string.ip) + "/streamvideo/" + contentId;
            Uri vidUri = Uri.parse(link);
            videoView.setVideoURI(vidUri);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            videoView.setMediaController(mc);

            videoView.start();
            videoView.getHolder().setSizeFromLayout();
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendLogPOST(final String uid, final String id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/logcontentrequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {





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
                    jsonBodyObj.put("uid", uid);
                    jsonBodyObj.put("content_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }
        };
        queue.add(postRequest);

    }


    public void sendAdPOST(final String uid, final String id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/getAD";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        startAdPlayer(response);

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
                    jsonBodyObj.put("uid", uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }
        };
        queue.add(postRequest);

    }
}