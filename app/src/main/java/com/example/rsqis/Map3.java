package com.example.rsqis;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map3);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mUpload= findViewById(R.id.btnupload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadintent = new Intent(Map3.this, UploadProof.class);
                startActivity(uploadintent);
            }
        });
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng pin1 = new LatLng(23.019300, 72.477593);
        mMap.addMarker(new MarkerOptions().position(pin1).title("Marker in Suncity Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin1,14f));
        LatLng pin2 = new LatLng(23.019537, 72.479107);
        mMap.addMarker(new MarkerOptions().position(pin2).title("Marker in Suncity Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin2,14f));
        LatLng pin3 = new LatLng(23.019143, 72.477953);
        mMap.addMarker(new MarkerOptions().position(pin3).title("Marker in Suncity Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin3,14f));
        LatLng pin4 = new LatLng(23.018690, 72.476677);
        mMap.addMarker(new MarkerOptions().position(pin4).title("Marker in Suncity Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin4,14f));
        LatLng pin5 = new LatLng(23.017915, 72.475385);
        mMap.addMarker(new MarkerOptions().position(pin5).title("Marker in Suncity Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin5,14f));
    }
}
