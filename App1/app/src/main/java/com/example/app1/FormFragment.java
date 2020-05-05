package com.example.app1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FormFragment extends Fragment {
    private Intent i;
    private double latitude, longitude;
    private float val;
    private String sensorType;
    private TextView sensor;
    private EditText longitudeET, latitudeET, sensorET;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        i = getActivity().getIntent();
        Bundle bundle = i.getExtras();
        switch (bundle.getString("key")){
            case "light":
                val = bundle.getFloat("light");
                sensorType = "Light Sensor";
                break;
            case "humidity":
                val = bundle.getFloat("humidity");
                sensorType = "Humidity Sensor";
                break;
            case "temperature":
                val = bundle.getFloat("temperature");
                sensorType = "Temperature Sensor";
                break;
        }
        longitude = bundle.getDouble("longitude");
        latitude = bundle.getDouble("latitude");
        sensor = view.findViewById(R.id.sensortxt);
        longitudeET = view.findViewById(R.id.longitude);
        latitudeET= view.findViewById(R.id.latitude);
        sensorET = view.findViewById(R.id.sensor);
        sensor.setText(sensorType);
        longitudeET.setText("" + longitude);
        longitudeET.setEnabled(false);
        latitudeET.setText("" + latitude);
        latitudeET.setEnabled(false);
        sensorET.setText("" + val);
        sensorET.setEnabled(false);
        return view;
    }
}