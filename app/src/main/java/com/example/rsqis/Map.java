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

public class Map<S, S1> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mUpload= findViewById(R.id.btnupload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadintent = new Intent(Map.this, UploadProof.class);
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
        LatLng O7 = new LatLng(23.006675, 72.463198);
        mMap.addMarker(new MarkerOptions().position(O7).title("Marker in Club O7 Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(O7,14f));
        LatLng club_O7 = new LatLng(23.007247, 72.461205);
        mMap.addMarker(new MarkerOptions().position(club_O7).title("Marker in Club O7 Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(club_O7,14f));
        LatLng club = new LatLng(23.007642, 72.459732);
        mMap.addMarker(new MarkerOptions().position(club).title("Marker in Club O7 Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(club,14f));
        LatLng road = new LatLng(23.007090, 72.456148);
        mMap.addMarker(new MarkerOptions().position(road).title("Marker in Club O7 Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(road,14f));
        LatLng roadO7 = new LatLng(23.006913, 72.455133);
        mMap.addMarker(new MarkerOptions().position(roadO7).title("Marker in Club O7 Road"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(roadO7,14f));
    }
}
