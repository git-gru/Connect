package com.jarrebnnee.connect;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrebnnee.connect.Service.SaveSharedPrefrence;
import com.jarrebnnee.connect.Service.TrackGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class SellerMapLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback,GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerDragListener{


    GoogleMap mMap;
    Marker mCurrLocationMarker;
    MarkerOptions markerOptions;
    private GoogleApiClient mGoogleApiClient;
    FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest mLocationRequest;
    ImageView ivlv;
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvTitle;
    TrackGPS gps;
    String u_latitute1,u_longitute1;
    Marker marker;
    Double lat, lng;
    String radius;
    SaveSharedPrefrence saveSharedPrefrence;
    Button btn_next, btn_cancel;
    Circle circle;
    LocationRequest  locationRequest;
    GoogleApiClient googleApiClient;
    Geocoder _coder;
    String locationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_map_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "" + "</font>")));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gps= new TrackGPS(SellerMapLocation.this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveSharedPrefrence = new SaveSharedPrefrence();
        radius = saveSharedPrefrence.getpackageRadius(getApplicationContext());
        _coder = new Geocoder(SellerMapLocation.this, Locale.ENGLISH);
        if (gps.canGetLocation()) {

            u_longitute1 = String.valueOf(gps.getLongitude());
            u_latitute1 = String.valueOf(gps.getLatitude());

          //  Toast.makeText(getApplicationContext(), "Longitude:" + u_longitute1 + "\nLatitude:" + u_latitute1, Toast.LENGTH_SHORT).show();
            Log.e("latlong", "Longitude:" + u_longitute1 + "\nLatitude:" + u_latitute1);
        } else {
            gps.showSettingsAlert();
        }

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SellerMapLocation.this, SellerAdvDetailsActivity.class);
                    Log.e("intent", "lat : "+lat+"\nlng :"+lng);
                    intent.putExtra("lat", ""+lat);
                    intent.putExtra("lng", ""+lng);
                    intent.putExtra("locationName", locationName);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("exception caught", "ecveption : "+e);
                    gps.showSettingsAlert();
                }

            }
        });




    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

         lat = Double.valueOf(u_latitute1);
        lng = Double.valueOf(u_longitute1);
        mMap = googleMap;
        LatLng sydney = new LatLng(lat, lng);
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(u_latitute1), Double.valueOf(u_longitute1)))
                .title("Your location")
                .snippet("You are here!")
        .zIndex(2.0f));
        marker.setDraggable(true);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, lng)).zoom(12).build();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMarkerDragListener(this);
        Log.e("map", "radius= " + radius);
         circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(Double.parseDouble(radius))
                .fillColor(0x40ff0000)  //semi-transparent
                .strokeColor(Color.BLUE)
                .strokeWidth(1));
        circle.setVisible(true);
        try{

            android.location.Address address = null;
            List<android.location.Address> addresses = _coder.getFromLocation(lat,lng,1);
            for(int index=0; index<addresses.size(); ++index)
            {
                address = addresses.get(index);
                for (int i=0; i<address.getMaxAddressLineIndex(); i++) {
                    Log.e("max", ""+address.getMaxAddressLineIndex());
                    if (i==address.getMaxAddressLineIndex()-1) {
                        locationName= locationName+(address.getAddressLine(i));
                        break;
                    }
                    locationName= locationName+(address.getAddressLine(i) + ", ");
                }
            }
            Log.e("locationName",""+locationName);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        marker.setVisible(true);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.e("map", "onMarkerDragStart");
        circle.setVisible(false);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

        marker.setTitle("Changing your location");
        marker.setSnippet("You can now drop the marker to your desired location!");
        marker.showInfoWindow();
        LatLng dragPosition1 = marker.getPosition();
        circle.setCenter(new LatLng(dragPosition1.latitude,dragPosition1.longitude));
        circle.setVisible(true);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.e("map", "onMarkerDragEnd");
        marker.hideInfoWindow();
        LatLng dragPosition = marker.getPosition();
        lat = dragPosition.latitude;
        lng = dragPosition.longitude;
        Log.e("new", "Lat= "+lat+"\nLng= "+lng);
        circle.setCenter(new LatLng(lat,lng));
        circle.setVisible(true);

        try{
            locationName = "";
            android.location.Address address = null;
            List<android.location.Address> addresses = _coder.getFromLocation(lat,lng,1);
            for(int index=0; index<addresses.size(); ++index)
            {
                address = addresses.get(index);
                for (int i=0; i<address.getMaxAddressLineIndex(); i++) {
                    Log.e("max", ""+address.getMaxAddressLineIndex());
                    if (i==address.getMaxAddressLineIndex()-1) {
                        locationName= locationName+(address.getAddressLine(i));
                        break;
                    }
                    locationName= locationName+(address.getAddressLine(i) + ", ");
                }
            }
            Log.e("locationName",""+locationName);
        }
        catch(Exception e){

        }
    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
