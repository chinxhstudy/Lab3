package com.example.jsonvolley;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String ipConfig = "192.168.1.221";

    private String urlJsonObj = "http://" + ipConfig + "/contact/person_object.json";
    private String urlJsonArry = "http://" + ipConfig + "/contact/person_array.json";
    private Button btnObjRequest;
    private Button btnArrRequest;
    private TextView txtResponse;
    private ProgressBar progressCircular;

    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnObjRequest = (Button) findViewById(R.id.btnObjRequest);
        btnArrRequest = (Button) findViewById(R.id.btnArrRequest);
        txtResponse = (TextView) findViewById(R.id.txtResponse);
        progressCircular = (ProgressBar) findViewById(R.id.progress_circular);

        requestQueue = Volley.newRequestQueue(this);
        btnArrRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircular.setVisibility(View.VISIBLE);
                requestArr();
            }
        });

        btnObjRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircular.setVisibility(View.VISIBLE);
                requestObject();
            }
        });

    }

    private void requestArr() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlJsonArry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressCircular.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("contacts");
                    String result = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject person = jsonArray.getJSONObject(i);
                        String name = person.getString("name");
                        String email = person.getString("email");
                        result += "Name: " + name + ", Email: " + email + "\n";
                    }
                    txtResponse.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtResponse.setText("Error: " + error.getMessage());
                progressCircular.setVisibility(View.GONE);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void requestObject() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlJsonObj, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressCircular.setVisibility(View.GONE);
                    String name = response.getString("name");
                    String email = response.getString("email");

                    String result = "Name: " + name + "\nEmail: " + email;
                    txtResponse.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressCircular.setVisibility(View.GONE);
                txtResponse.setText("Error: " + error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}