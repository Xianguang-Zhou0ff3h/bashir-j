package com.example.bashir.flightapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

//import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    String WrongInputColor = "#ffb3b3";
    String url = "192.168.1.115:3000/";
    Button btn_next, btn_cancel;
    EditText firstname,lastname,username,password,repassword;
    TableLayout table;

    SharedPreferences signinfo;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        firstname = (EditText)findViewById(R.id.editText_firstname);
        lastname = (EditText)findViewById(R.id.editText_lastname);
        username = (EditText)findViewById(R.id.editText_Username);
        password = (EditText)findViewById(R.id.editText_Password);
        repassword = (EditText)findViewById(R.id.editText_RePassword);
        btn_next = (Button)findViewById(R.id.Btn_Next);
        btn_cancel = (Button)findViewById(R.id.Btn_Cancel);
        table = (TableLayout)findViewById(R.id.table_layout);
        signinfo = PreferenceManager.getDefaultSharedPreferences(this);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);//Get Back to login class (Page)
                startActivity(intent);

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Boolean Bool_firstname = firstname.getText().toString().equals("");
                Boolean Bool_lastname = lastname.getText().toString().equals("");
                Boolean Bool_username = username.getText().toString().equals("");
                Boolean Bool_DifferentPass = !password.getText().toString().equals(repassword.getText().toString()) ||
                        password.getText().toString().equals("") || repassword.getText().toString().equals("");
                if (Bool_firstname){
                    // firstname.setHint("This field is empty");
                    firstname.setHintTextColor(Color.parseColor(WrongInputColor));
                }
                if (Bool_lastname){
                    // lastname.setHint("This field is empty");
                    lastname.setHintTextColor(Color.parseColor(WrongInputColor));
                }
                if (Bool_username){
                    // username.setHint("This field is empty");
                    username.setHintTextColor(Color.parseColor(WrongInputColor));
                }
                if (Bool_DifferentPass){
                    password.setText("");
                    repassword.setText("");
                    password.setHint("Password doesn't match");
                    password.setHintTextColor(Color.parseColor(WrongInputColor));
                    repassword.setHint("Password doesn't match");
                    repassword.setHintTextColor(Color.parseColor(WrongInputColor));

                }
                if (!Bool_firstname && !Bool_lastname && !Bool_username && !Bool_DifferentPass){
                    //Send username and password to Server
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    //Log.d("Response", response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    // Log.d("Error.Response", response);
                                    Intent intent = new Intent(getApplicationContext(), SignUpCategoryActivity.class);
                                    startActivity(intent);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("username", username.getText().toString());
                            params.put("password", password.getText().toString());
                            params.put("name", firstname.getText().toString() + " " + lastname.getText().toString());
                            return params;
                        }
                    };
                    queue.add(postRequest);





                }
            }
        });
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor edit = signinfo.edit();
        edit.putString("firstname", firstname.getText().toString());
        edit.putString("lastname", lastname.getText().toString());
        edit.putString("username", username.getText().toString());
        edit.putString("password", password.getText().toString());
        edit.putString("repassword", repassword.getText().toString());
        edit.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstname.setText(signinfo.getString("firstname", ""));
        lastname.setText(signinfo.getString("lastname", ""));
        username.setText(signinfo.getString("username", ""));
        password.setText(signinfo.getString("password", ""));
        repassword.setText(signinfo.getString("repassword", ""));
    }
}