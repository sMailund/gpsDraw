package com.example.simems.gpsdraw;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean tracking = false;
    LocationTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView locationText = findViewById(R.id.locationText);
        final Button saveButton = findViewById(R.id.save);
        final Button recordButton = findViewById(R.id.recordButton);
        final EditText svgContainer = findViewById(R.id.svgContainer);
        saveButton.setVisibility(View.INVISIBLE);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tracking) {
                    recordButton.setText(R.string.stopRecording);
                    saveButton.setVisibility(View.INVISIBLE);
                    tracker = new LocationTracker(getApplicationContext(), locationText );
                    tracker.startLocationTracking();
                    tracking = true;
                } else {
                    recordButton.setText(R.string.startRecording);
                    saveButton.setVisibility(View.VISIBLE);
                    tracking = true;
                    tracker.stopLocationTracking();
                    List<Location> locations = tracker.getLocationList();
                    SvgConverter converter = new SvgConverter();
                    String svg = converter.createSvgFromLocationList(locations);
                    svgContainer.setText(svg);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SvgWriter fileWriter = new SvgWriter(getApplicationContext());
                String fileName = new Date().toString() + ".svg";
                boolean fileSaveResult = fileWriter.writeToFile(
                        fileName, svgContainer.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(),
                        fileSaveResult ? "saved file!" : "could not save file",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
