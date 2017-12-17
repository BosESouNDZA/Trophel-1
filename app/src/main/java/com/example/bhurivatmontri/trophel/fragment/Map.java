package com.example.bhurivatmontri.trophel.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends Fragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    GoogleMap mGoogleMap;
    Marker mMarker;
    MapView mMapView;
    View mView;
    GoogleApiClient googleApiClient;
    boolean chkFirstTime = true;

    public Map() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        buildClient();

        /*googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(18.795561,98.953033)).title("30 years Building").snippet("I am coding myProject"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(18.795561,98.953033)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        */



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        return;
    }

    private void buildClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(getActivity(), "Map Connect!!", Toast.LENGTH_SHORT);
        LocationRequest lr = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, lr, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //String provider = location.getProvider();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        //double altitude = location.getAltitude();
        //float accuracy = location.getAccuracy();
        //float bearing = location.getBearing();
        //float speed = location.getSpeed();
        //long time = location.getTime();
        if(chkFirstTime == true){
            CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude,longitude)).zoom(16).bearing(0).tilt(45).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
            chkFirstTime = false;
        }
        if(mGoogleMap != null){
            mGoogleMap.clear();
        }
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Location Current").snippet("I am coding myProject").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude+0.002,longitude)).title("Building1"));
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude+0.003,longitude+0.007)).title("Building2"));
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude-0.007,longitude-0.008)).title("Building3"));
        // mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude+0.0000003)).title("Building2"));
        // mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude+0.0000002,longitude-0.0000002)).title("Building3"));


    }
    @Override
    public boolean onMarkerClick(final Marker arg0) {
        if(arg0.equals(mMarker)) // if marker  source is clicked
            Toast.makeText(getActivity(), "TEST", Toast.LENGTH_SHORT).show();
        return true;
    }






}
