package com.example.simems.gpsdraw;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationClient;
    private boolean currentlyTracking = false;
    private Timer locationTrackingScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationTrackingScheduler = new Timer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentlyTracking) {
                    currentlyTracking = true;
                    startLocationTracking();
                } else {
                    stopLocationTracking();
                }
            }
        });
    }

    private void startLocationTracking() {
        locationTrackingScheduler.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                displayLocation();
            }
        },0,500);
    }

    private void displayLocation() {
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        final TextView text = findViewById(R.id.locationText);

        Task<Location> locationTask;
        try {
            locationTask = locationClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    text.setText(location.toString());
                }
            });
        } catch (SecurityException e) {
            //noe feil
            text.setText("Could not find location: " + e.toString());
        }
    }

    private void stopLocationTracking() {
        locationTrackingScheduler.cancel();
        TextView text = findViewById(R.id.locationText);
        text.setText("cancelled");
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
