package com.example.app1;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {
    private SensorManager DeviceSensorManager;
    private GoogleMap mMap;
    private int markerCount = 0;
    private Sensor light, humidity, temperature;
    private float light_val, humidity_val, temperature_val;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        DeviceSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        humidity = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        temperature = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.goodle_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (light != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(MapsActivity.this, "No light sensor", Toast.LENGTH_LONG).show();
        }
        if (humidity != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(MapsActivity.this, "No humidity sensor", Toast.LENGTH_LONG).show();
        }
        if (temperature != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(MapsActivity.this, "No temperature sensor", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (markerCount < 5) {
                    //Creating Marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Set Marker Position
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    //Zoom the Marker
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    //Add Marker On Map
                    mMap.addMarker(markerOptions);
                    int randomizer = getRandom(0, 2);
                    Intent i = new Intent(MapsActivity.this, FormActivity.class);
                    Bundle extras = new Bundle();
                    switch (randomizer){
                        case 0:
                            extras.putFloat("light", light_val);
                            extras.putString("key", "light");
                            break;
                        case 1:
                            extras.putFloat("humidity", humidity_val);
                            extras.putString("key", "humidity");
                            break;
                        case 2:
                            extras.putFloat("temperature", temperature_val);
                            extras.putString("key", "temperature");
                            break;
                    }
                    extras.putDouble("latitude", latLng.latitude);
                    extras.putDouble("longitude", latLng.longitude);
                    i.putExtras(extras);
                    startActivity(i);
                    markerCount++;
                }
                else {
                    return;
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                light_val = event.values[0];
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                humidity_val = event.values[0];
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                temperature_val = event.values[0];
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public int getRandom(int lowest, int highest) {
        Random random = new Random();
        return random.nextInt((highest - lowest) + 1) + lowest;
    }
}