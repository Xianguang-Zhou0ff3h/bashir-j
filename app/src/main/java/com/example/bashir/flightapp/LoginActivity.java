package com.example.bashir.flightapp;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText password;
    EditText username;
    Button login;
    TextView signUpTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        username = (EditText) findViewById(R.id.userEditText);
        password = (EditText) findViewById(R.id.passEditText);
        login = (Button) findViewById(R.id.loginButton);
        signUpTV = (TextView) findViewById(R.id.signUpTextView);


        signUpTV.setPaintFlags(signUpTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
    }
    public void loginButton(View v) {
        //sendPost(username.getText().toString(),password.getText().toString());
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }

    public void signUpButton(View v) {
        //sendPost(username.getText().toString(),password.getText().toString());
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void sendPost(final String user, final String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.1.115:3000/getContent";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        /*
                        try {
                            displayTitles(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */
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
                params.put("user", user);
                params.put("pass", pass);

                return params;
            }
        };
        queue.add(postRequest);

    }


}
