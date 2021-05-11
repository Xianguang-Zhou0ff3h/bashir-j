package com.example.bashir.flightapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    String WrongInputColor = "#ffb3b3";
    EditText firstname ,lastname, password, repassword , age;
    ProgressBar loading ;
    Button edit, submit, cancel;
    Boolean editedBool = false;
    TextView yeartxt , txtfirstname, txtlastname, txtpassword,txtrepassword, txtspy;
    String uid;
    Boolean passisempty = true;
    CheckBox check;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();

        firstname = (EditText)findViewById(R.id.editText_firstname);
        lastname = (EditText)findViewById(R.id.editText_lastname);
        password = (EditText)findViewById(R.id.editText_passwordedit);
        repassword = (EditText)findViewById(R.id.editText_repasswordedit);
        age = (EditText)findViewById(R.id.editText_age);
        edit = (Button)findViewById(R.id.btn_edit);
        submit = (Button)findViewById(R.id.btn_submit);
        uid = prefs.getString("UID", "");
        loading = (ProgressBar)findViewById(R.id.progressBar);
        yeartxt = (TextView)findViewById(R.id.txt_age);
        txtfirstname = (TextView)findViewById(R.id.txt_firstname);
        txtlastname = (TextView)findViewById(R.id.txt_lastname);
        txtpassword = (TextView)findViewById(R.id.txt_password);
        txtrepassword = (TextView)findViewById(R.id.txt_repassword);
        txtspy = (TextView)findViewById(R.id.txt_spy);
        check = (CheckBox)findViewById(R.id.checkBox);
        cancel = (Button)findViewById(R.id.btn_cancel);



        hideeverything();

        getuserinfo();

        firstname.setEnabled(false);
        lastname.setEnabled(false);
        password.setEnabled(false);
        repassword.setEnabled(false);
        age.setEnabled(false);
        check.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editedBool = true;
                firstname.setEnabled(true);
                lastname.setEnabled(true);
                password.setEnabled(true);
                repassword.setEnabled(true);
                age.setEnabled(true);
                check.setEnabled(true);
                repassword.setVisibility(View.VISIBLE);
                txtrepassword.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editedBool){
                    Boolean Bool_firstname = firstname.getText().toString().equals("");
                    Boolean Bool_lastname = lastname.getText().toString().equals("");
                    Boolean Bool_age;
                    Boolean Bool_DifferentPass = !password.getText().toString().equals(repassword.getText().toString());
                    Boolean passisbetween1to6 = (password.getText().toString().length()<6 || repassword.getText().toString().length()<6)&&
                            (password.getText().toString().length()>=1 || repassword.getText().toString().length()>=1);
                    passisempty = password.getText().toString().equals("") && repassword.getText().toString().equals("");

                    if (Bool_firstname){
                        // firstname.setHint("This field is empty");
                        firstname.setHintTextColor(Color.parseColor(WrongInputColor));
                    }
                    if (Bool_lastname){
                        // lastname.setHint("This field is empty");
                        lastname.setHintTextColor(Color.parseColor(WrongInputColor));
                    }
                    if (Bool_DifferentPass){
                        password.setText("");
                        repassword.setText("");
                        password.setHint("Password doesn't match");
                        password.setHintTextColor(Color.parseColor(WrongInputColor));
                        repassword.setHint("Password doesn't match");
                        repassword.setHintTextColor(Color.parseColor(WrongInputColor));
                    }
                    if (passisbetween1to6){
                        password.setText("");
                        repassword.setText("");
                        password.setHint("Password should be minimum 6 characters");
                        password.setHintTextColor(Color.parseColor(WrongInputColor));
                        repassword.setHint("Password should be minimum 6 characters");
                        repassword.setHintTextColor(Color.parseColor(WrongInputColor));
                    }

                    String agetxt = age.getText().toString();
                    if (agetxt.equals("") || agetxt.isEmpty() || agetxt.length()!=4 ){
                        Bool_age = true;
                    }
                    else {
                        Bool_age = false;
                    }

                    if (Bool_age){
                        age.setHintTextColor(Color.parseColor(WrongInputColor));
                    }

                    if (!Bool_firstname && !Bool_lastname && !Bool_DifferentPass && !Bool_age && !passisbetween1to6){
                        //update Server with new info, then go to browse activity
                        updateinfo();
                        edit.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        txtrepassword.setVisibility(View.GONE);
                        repassword.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        firstname.setEnabled(false);
                        lastname.setEnabled(false);
                        password.setEnabled(false);
                        repassword.setEnabled(false);
                        age.setEnabled(false);
                        check.setEnabled(false);
                        //Intent intent = new Intent(getApplicationContext(), BrowseActivity.class);
                        //startActivity(intent);
                    }
                }

                else {
                    // Intent intent = new Intent(getApplicationContext(), BrowseActivity.class);
                    // startActivity(intent);
                    edit.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    txtrepassword.setVisibility(View.GONE);
                    repassword.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    firstname.setEnabled(false);
                    lastname.setEnabled(false);
                    password.setEnabled(false);
                    repassword.setEnabled(false);
                    age.setEnabled(false);
                    check.setEnabled(false);
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                txtrepassword.setVisibility(View.GONE);
                repassword.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                firstname.setEnabled(false);
                lastname.setEnabled(false);
                password.setEnabled(false);
                repassword.setEnabled(false);
                age.setEnabled(false);
                check.setEnabled(false);
            }
        });

    }

    public void getuserinfo() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getString(R.string.ip) + "/userprofile/" + uid;
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonBodyObj = new JSONObject(response);
                            firstname.setText(jsonBodyObj.getString("firstname"));
                            lastname.setText(jsonBodyObj.getString("lastname"));
                            String birth = jsonBodyObj.getString("birthdate");
                            age.setText(birth.substring(birth.length() - 4));
                            String track = jsonBodyObj.getString("track");
                            if (track.equals("true")){check.setChecked(true);}
                            else check.setChecked(false);
                        }
                        catch (JSONException e){

                        }
                        displayeverything();
                        loading.setVisibility(View.GONE);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Response", "Error");
                        //Log.d("Error.Response", response);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }
        };
        queue.add(postRequest);

    }

    public void updateinfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.ip) + "/updateUser";
        //String url = "https://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonBodyObj = new JSONObject(response);
                            firstname.setText(jsonBodyObj.getString("firstname"));
                            lastname.setText(jsonBodyObj.getString("lastname"));
                            String birth = jsonBodyObj.getString("birthdate");
                            age.setText(birth.substring(birth.length() - 4));
                            String track = jsonBodyObj.getString("track");
                            if (track.equals("true")){check.setChecked(true);}
                            else check.setChecked(false);
                        }
                        catch (JSONException e){

                        }
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

                try {
                    //jsonBodyObj.put("genre", genreArray);
                    jsonBodyObj.put("uid", uid);
                    jsonBodyObj.put("firstname", firstname.getText().toString());
                    jsonBodyObj.put("lastname", lastname.getText().toString());
                    jsonBodyObj.put("birthdate", "January 1, " + age.getText().toString() );
                    if (check.isChecked()){jsonBodyObj.put("track", "true" );}
                    else jsonBodyObj.put("track", "false" );
                    if (!passisempty) jsonBodyObj.put("password", password.getText().toString() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("send", jsonBodyObj.toString());
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

    void hideeverything(){
        txtspy.setVisibility(View.GONE);
        txtfirstname.setVisibility(View.GONE);
        txtlastname.setVisibility(View.GONE);
        txtpassword .setVisibility(View.GONE);
        txtrepassword.setVisibility(View.GONE);
        yeartxt.setVisibility(View.GONE);
        firstname.setVisibility(View.GONE);
        lastname.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        repassword.setVisibility(View.GONE);
        age.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }

    void displayeverything(){
        txtfirstname.setVisibility(View.VISIBLE);
        txtlastname.setVisibility(View.VISIBLE);
        txtpassword .setVisibility(View.VISIBLE);
        yeartxt.setVisibility(View.VISIBLE);
        firstname.setVisibility(View.VISIBLE);
        lastname.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        age.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
        //submit.setVisibility(View.VISIBLE);
        txtspy.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
    }

}
