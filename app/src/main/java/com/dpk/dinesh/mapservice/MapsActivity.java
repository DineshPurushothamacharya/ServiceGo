package com.dpk.dinesh.mapservice;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected double dLat;
    protected double dLong;
    private GoogleMap mMap;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // create the connection client
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (client.isConnected()) {
            client.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"Connected....", Toast.LENGTH_SHORT).show();

        if (checkPermission()) return;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        updateMapUI();
    }

    @Override
    public void onConnectionSuspended(int i) {
    client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateMapUI();
    }

    private void updateMapUI() {

        if(mMap != null) {
            if (mLastLocation != null) {

                dLat = mLastLocation.getLatitude();
                dLong = mLastLocation.getLongitude();
            } else {
                Toast.makeText(this, "No location Detected..", Toast.LENGTH_LONG).show();
            }

            LatLng myLocation = new LatLng(dLat, dLong);
            CameraUpdate  update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
            mMap.animateCamera(update);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("You are here"));
            this.createCircle(myLocation);
        }
    }

    private void createCircle(LatLng latLang) {
        CircleOptions circleOption = new CircleOptions().center(latLang).radius(800).fillColor(0X330000FF).strokeColor(Color.BLUE).strokeWidth(3);
        mMap.addCircle(circleOption);
    }

}
