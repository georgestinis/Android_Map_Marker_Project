package com.example.app1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FormFragment extends Fragment {
    private Intent i;
    private double latitude, longitude;
    private float sensor_val;
    private String sensorType;
    private TextView sensor;
    private EditText longitudeET, latitudeET, sensorET, description;
    private Spinner color;
    Button bn;

    // Κατα την δημιουργια του fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Οριζω ενα view
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        // Παιρνω τα extra που σταλθηκαν απο το αλλο activity
        i = getActivity().getIntent();
        Bundle bundle = i.getExtras();
        // Αναλογα με το String ΄key΄ βλεπω ποιος σενσορας επιλεχθηκε
        switch (bundle.getString("key")) {
            // Παιρνω την τιμη και τον τυπο του σενσορα που σταλθηκε
            case "light":
                sensor_val = bundle.getFloat("light");
                sensorType = "Light Sensor";
                break;
            case "humidity":
                sensor_val = bundle.getFloat("humidity");
                sensorType = "Humidity Sensor";
                break;
            case "temperature":
                sensor_val = bundle.getFloat("temperature");
                sensorType = "Temperature Sensor";
                break;
        }
        // Για το χρωμα του marker δημιουργω ενα spinner με τιμες απο το strings.xml τις οποιες παιρνει ενας adapter και επειτα θετω στο spinner τον adapter
        color = view.findViewById(R.id.color);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.colors, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        color.setAdapter(adapter);
        // Παιρνω απο το bundle το longitude, latitude
        longitude = bundle.getDouble("longitude");
        latitude = bundle.getDouble("latitude");
        // Οριζω τις μεταβλητες με βαση τα καταλληλα id σε edittext, textview
        sensor = view.findViewById(R.id.sensortxt);
        longitudeET = view.findViewById(R.id.longitude);
        latitudeET= view.findViewById(R.id.latitude);
        sensorET = view.findViewById(R.id.sensor);
        color = view.findViewById(R.id.color);
        description = view.findViewById(R.id.description);
        // Θετω τα καταλληλα αποτελεσματα σε καθε edittext και textview και σε οσα δεν επιτρεπεται να αλλαξω την τιμη τα κανω disable
        sensor.setText(sensorType);
        longitudeET.setText("" + longitude);
        longitudeET.setEnabled(false);
        latitudeET.setText("" + latitude);
        latitudeET.setEnabled(false);
        sensorET.setText("" + sensor_val);
        sensorET.setEnabled(false);
        // Οριζω το κουμπι μου
        bn = view.findViewById(R.id.btn);
        // Οταν πατηθει το κουμπι
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Παιρνω το χρωμα και αναλογα ποιο επιλεχθηκε το μετατρεπω σε float για να μπορεσω να βαλω το χρωμα στην αλλη εφαρμογη
                String color_code = color.getSelectedItem().toString();
                float varColor;
                switch (color_code){
                    case "HUE_AZURE":
                        varColor = 210;
                        break;
                    case "HUE_BLUE":
                        varColor = 240;
                        break;
                    case "HUE_CYAN":
                        varColor = 180;
                        break;
                    case "HUE_GREEN":
                        varColor = 120;
                        break;
                    case "HUE_MAGENTA":
                        varColor = 300;
                        break;
                    case "HUE_ORANGE":
                        varColor = 30;
                        break;
                    case "HUE_RED":
                        varColor = 0;
                        break;
                    case "HUE_ROSE":
                        varColor = 330;
                        break;
                    case "HUE_VIOLET":
                        varColor = 270;
                        break;
                    case "HUE_YELLOW":
                        varColor = 60;
                        break;
                    default:
                        varColor = 0;
                        break;
                }
                // Παιρνω το description Που εχω γραψει
                String varDescription = description.getText().toString();
                // Προσπαθω να δημιουργησω ενα αντικειμενο που αντιστοιχει στην βαση μου για να περασω τις τιμες
                try {
                    Markers marker = new Markers();
                    marker.setLatitude(latitude);
                    marker.setLongitude(longitude);
                    marker.setSensor_type(sensorType);
                    marker.setSensor_value(sensor_val);
                    marker.setColor(varColor);
                    marker.setDescription(varDescription);
                    // Επιλεγω το Markers collection της βασης μου και κανω add το αντικειμενο μου που εχει μεσα τιμες
                    // Αν πετυχει η προσθηκη εμφανιζω ενα toast οτι εγινε αλλιως αν αποτυχει εμφανιζω αντιστοιχο toast
                    FormActivity.db.collection("Markers").add(marker).
                            addOnSuccessListener((task) -> {
                                Toast.makeText(getContext(), "Marker added.", Toast.LENGTH_LONG).show();
                            }).
                            addOnFailureListener((task) -> {
                                Toast.makeText(getContext(), "Add operation failed", Toast.LENGTH_LONG).show();
                            });
                // Αν προκυψει καποιο exception το εμφανιζω
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                // Δημιουργω εναν handler ωστε να προσθεσω μια καθυστερηση στην εκτελεση των εντολων παρακατω για να εμφανισθει το toast message
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Τερματιζω το activity αυτο για να γυρισω στο προηγουμενο
                        getActivity().finish();
                        // Αδειαζω τα edittext
                        longitudeET.setText("");
                        latitudeET.setText("");
                        sensorET.setText("");
                        color.setSelection(0);
                        description.setText("");
                        sensor.setText("Sensor: ");
                    }
                }, 1000);
            }
        });
        return view;
    }

}