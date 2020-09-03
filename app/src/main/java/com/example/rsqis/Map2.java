package com.example.rsqis;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Map2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mUpload;
    String data,roadID;
    JSONArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mUpload= findViewById(R.id.btnupload);
        data= getIntent().getStringExtra("data");
        try {
            JSONObject dataobj=new JSONObject(data);
            roadID=dataobj.getString("roadID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadintent = new Intent(Map2.this, UploadProof.class);
                uploadintent.putExtra("roadID",roadID);
                startActivity(uploadintent);
            }
        });
      ;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getCodes(googleMap);

    }

    public void getCodes(final GoogleMap googleMap){
        String host = getResources().getString(R.string.host);
        String URLline = host+"/road/cords";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mMap = googleMap;

                        try {
                            arr=new JSONArray(response);
                            for(int x=0;x<arr.length();x++){
                                JSONObject obj=arr.getJSONObject(x);
                                LatLng pin1 = new LatLng(Double.valueOf(obj.getString("lat")),Double.valueOf(obj.getString("lng")));
                                mMap.addMarker(new MarkerOptions().position(pin1).title("damaged road"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin1,14f));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("roadID",roadID);
                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

}
