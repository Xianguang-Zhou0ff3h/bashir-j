package com.example.bashir.flightapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    String WrongInputColor = "#ffb3b3";
    Button btn_next;
    EditText firstname,lastname,username,password,repassword;
    TableLayout table;
    Boolean usernameValid;
    SharedPreferences signinfo;
    TextView promptText;
    RadioButton firstclass,busiclass,ecoclass;
    CheckBox check;



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
        promptText = (TextView) findViewById(R.id.promptTextView);
        firstclass = (RadioButton)findViewById(R.id.rdb_firstclass2);
        ecoclass = (RadioButton)findViewById(R.id.rdb_economyclass2);
        busiclass = (RadioButton)findViewById(R.id.rdb_Businessclass2);
        ecoclass.setSelected(true);
        check = (CheckBox) findViewById(R.id.checkBox) ;
        table = (TableLayout)findViewById(R.id.table_layout);
        signinfo = PreferenceManager.getDefaultSharedPreferences(this);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !username.getText().toString().equals("")){
                    promptText.setVisibility(View.GONE);
                    checkUsernamePOST();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Boolean Bool_firstname = firstname.getText().toString().equals("");
                Boolean Bool_lastname = lastname.getText().toString().equals("");
                Boolean Bool_username = username.getText().toString().equals("");
                Boolean Bool_PassShort = (password.getText().toString().length() < 6);
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
                    promptText.setVisibility(View.GONE);
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
                if (Bool_PassShort){
                    password.setText("");
                    repassword.setText("");
                    password.setHint("Password must be 6 characters or more");
                    password.setHintTextColor(Color.parseColor(WrongInputColor));
                    repassword.setHint("Password must be 6 characters or more");
                    repassword.setHintTextColor(Color.parseColor(WrongInputColor));

                }
                if (!Bool_firstname && !Bool_lastname && !Bool_username && !Bool_DifferentPass && !Bool_PassShort){
                    //Send username and password to Server
                    signUpPOST();

                }
            }
        });
    }

    public void checkUsernamePOST() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/checkUsername";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseUsernameValidityResponse(response);
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
                    jsonBodyObj.put("username", username.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }

        };
        queue.add(postRequest);

    }

    public void signUpPOST() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/insertUser";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseSignUpResponse(response);
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
                    jsonBodyObj.put("username", username.getText().toString());
                    jsonBodyObj.put("password", password.getText().toString());
                    jsonBodyObj.put("firstname", firstname.getText().toString());
                    jsonBodyObj.put("lastname", lastname.getText().toString());
                    jsonBodyObj.put("track", Boolean.toString(check.isChecked()));
                    if (firstclass.isChecked()){jsonBodyObj.put("class", "First");}
                    else if (busiclass.isChecked()){jsonBodyObj.put("class", "Business");}
                    else if (ecoclass.isChecked()){jsonBodyObj.put("class", "Economy");}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBodyObj.toString().getBytes();
            }

        };
        queue.add(postRequest);

    }

    public void parseUsernameValidityResponse(String response) {
        if (Boolean.parseBoolean(response)){
            //valid user
            promptText.setText("Username is valid!");
            promptText.setTextColor(Color.parseColor("#32CD32"));
            promptText.setVisibility(View.VISIBLE);
        }else{
            //invalid user
            promptText.setText("Username is not valid!");
            promptText.setTextColor(Color.parseColor("#cc0000"));
            promptText.setVisibility(View.VISIBLE);
        }
    }

    public void parseSignUpResponse(String response) {
        if (response.equals("null")){
            //invalid user
            promptText.setText("Username is not valid!");
            promptText.setTextColor(Color.parseColor("#cc0000"));
            promptText.setVisibility(View.VISIBLE);
        }else{
            //valid user
            Intent intent = new Intent(getApplicationContext(), SignUpCategoryActivity.class);
            intent.putExtra("uid", response);
            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
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