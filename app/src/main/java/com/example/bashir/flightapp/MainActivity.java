package com.example.bashir.flightapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    TableRow row;
    TableLayout tbl;
    View.OnClickListener buttonListener;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, WatchActivity.class);

        layout = (LinearLayout) findViewById(R.id.button_view);
        tbl = (TableLayout) findViewById(R.id.table_layout);
    }
    public void browseActivityStarter(View v) {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }

    public void sendPost(View v) {
        tbl.removeAllViews();
        TextView mTextView = (TextView) findViewById(R.id.text);
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.1.115:3000/getContent";
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("name", "Alif");
                //params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        queue.add(postRequest);

    }

    void displayTitles(String response) throws JSONException {
        JSONArray mainObject = new JSONArray(response);
        String  title = "";
        Log.d("Post", response);
        layout.removeAllViews();
        int count = 0;
        row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        for (int i=0; i <mainObject.length(); i++)
        {
            title = (((JSONObject)mainObject.get(i)).getString("title"));
            Log.d("Title", title);
            final Intent intent = new Intent(this, WatchActivity.class);
            if(count == 3){
                tbl.addView(row);
                row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                count = 1;
                final Button btnTag = new Button(this);
                btnTag.setText(title);
                btnTag.setId(i);

                btnTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("title",btnTag.getText());
                        startActivity(intent);
                    }
                });
                row.addView(btnTag);

            }
            else{
                final Button btnTag = new Button(this);
                btnTag.setText(title);
                btnTag.setId(i);
                btnTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent.putExtra("title",btnTag.getText());
                        startActivity(intent);
                    }
                });
                row.addView(btnTag);
                count++;
            }




        }
        Log.d("Count", count+"");
        if(count<=3 && row != null) {
            tbl.addView(row);
        }

    }
}
