package com.example.simems.gpsdraw;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
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

    public LocationTracker(Context context) {
        super(context);
        checkLocationPermission();
    }

    public void startLocationTracking() {
        trackingScheduler.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                appendCurrentLocation();
            }
        },0,500);
    }

    private void appendCurrentLocation() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> locationTask;
        try {
            locationTask = locationClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    locationList.add(location);
                }
            });
        } catch (SecurityException e) {
            //noe feil
        }
    }

    public void stopLocationTracking() {
        trackingScheduler.cancel();
    }

    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {//Can add more as per requirement

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
            }
        }
    }

    @Override
    public String toString() {
        String locations = "";

        for (Location location : locationList) {
            locations.concat(location.getLatitude() + " : " + location.getLongitude());
        }

        return locations;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

}
