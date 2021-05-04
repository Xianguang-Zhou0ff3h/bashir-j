package com.example.bashir.flightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String WrongInputColor = "#ffb3b3";
    EditText passwordTV;
    EditText usernameTV;
    Button login;
    String UID;

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString("UID", UID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        usernameTV = (EditText) findViewById(R.id.userEditText);
        passwordTV = (EditText) findViewById(R.id.passEditText);
        login = (Button) findViewById(R.id.loginButton);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ImageView imgview = (ImageView) findViewById(R.id.imageView3);
        imgview.getLayoutParams().height = height/3;


    }
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        UID = PreferenceManager.getDefaultSharedPreferences(this).getString("UID" , "");
    }
    public void loginButton(View v) {
        //sendPost(username.getText().toString(),password.getText().toString());
        String username = usernameTV.getText().toString();
        String password = passwordTV.getText().toString();
        boolean proceed = true;
        if(username.equals("")){
            proceed = false;
            //usernameTV.setText("");
            usernameTV.setHint("Please enter a valid username");
            usernameTV.setHintTextColor(Color.parseColor(WrongInputColor));
        }
        if(password.equals("")){
            proceed = false;
            //passwordTV.setText("");
            passwordTV.setHint("Please enter a valid password");
            passwordTV.setHintTextColor(Color.parseColor(WrongInputColor));
        }
        if(proceed) {
            sendLoginPOST(username,password);
        }

    }

    public void signUpButton(View v) {
        //sendPost(username.getText().toString(),password.getText().toString());
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void checkResponse(String response) throws JSONException {
        Log.d("responselogin",response);
        JSONObject mainObject = new JSONObject(response);

        int parsed = (int) mainObject.get("status");
        if (parsed == 1){
            UID = (String) mainObject.get("uid");
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
            edit.putString("UID", UID);
            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
        }
        else if(parsed == 0){
            passwordTV.setText("");
            passwordTV.setHint("Password is incorrect");
            passwordTV.setHintTextColor(Color.parseColor(WrongInputColor));
        }
        else if(parsed == -1){
            usernameTV.setText("");
            passwordTV.setText("");
            usernameTV.setHint("Username does not exist");
            usernameTV.setHintTextColor(Color.parseColor(WrongInputColor));
        }
    }

    public void sendLoginPOST(final String user, final String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {


                        try {
                            checkResponse(response);
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

                try {
                    //jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("username", user);
                    jsonBodyObj.put("password", pass);
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
