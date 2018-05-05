package com.example.simems.gpsdraw;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationTracker extends ContextWrapper {

    private Timer trackingScheduler = new Timer();
    private List<Location> locationList = new ArrayList<>();
    private FusedLocationProviderClient locationClient
            = LocationServices.getFusedLocationProviderClient(this);
    private TextView coordinatesText;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locations) {
            Location lastLocation = locations.getLastLocation();
            Log.d("locationcallback", locations.getLastLocation().toString());
            if (coordinatesText != null) {
                coordinatesText.setText(String.format("Current position:\n%f : %f",
                        lastLocation.getLatitude(), lastLocation.getLongitude()));
            }

            locationList.add(lastLocation);
        }
    };

    public LocationTracker(Context context, TextView coordinatesText) {
        super(context);
        this.coordinatesText = coordinatesText;
        checkLocationPermission();
    }

    public void startLocationTracking() {
        try {
            locationClient.requestLocationUpdates(createLocationRequest(),
                    locationCallback, null);
        } catch (SecurityException e) {
            Log.e("locationcallback", e.getMessage());
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public void stopLocationTracking() {
        locationClient.removeLocationUpdates(locationCallback);
    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {//Can add more as per requirement

                ActivityCompat.requestPermissions( (Activity) this.getBaseContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
            }
        }
    }

    @Override
    public String toString() {
        String locations = "locations: \n";

        for (Location location : locationList) {
            locations = locations.concat(location.getLatitude() + " : " + location.getLongitude() + "\n");
        }

        return locations;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

}
