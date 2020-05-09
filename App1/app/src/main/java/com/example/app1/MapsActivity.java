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

    // Κατα την δημιουργια του Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Οριζω το xml εγγραφο που θα ακολουθησει το activity οταν ανοιξει
        setContentView(R.layout.activity_maps);
        // Δηλωνω εναν Device Sensor Manager για να παρω τους σενσορες που επιθυμω
        DeviceSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Παιρνω τους default σενσορες που θελω
        light = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        humidity = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        temperature = DeviceSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Παιρνω το fragment με id google_map και ξεκιναω να εμφανιζω τον χαρτη
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    // Οταν η εφαρμογη μπει στο παρασκηνιο τοτε καταργω τον listener
    @Override
    protected void onPause() {
        super.onPause();
        DeviceSensorManager.unregisterListener(this);
    }

    // Οταν εκτελειται η εφαρμογη και δεν ειναι στο παρασκηνιο δηλωνω τον listener για τους σενσορες
    @Override
    protected void onResume() {
        super.onResume();
        // Αν υπαρχει σενσορας light τοτε δηλωνω εναν register για τον σενσορα αυτον και θετω μια καθυστερηση για να ξανα διαβαζει την τιμη του σενσορα καθε λιγο
        if (light != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Αν δεν υπαρχει εμφανιζω ενα toast μηνυμα
        else {
            Toast.makeText(MapsActivity.this, "No light sensor", Toast.LENGTH_LONG).show();
        }
        // Αν υπαρχει σενσορας humidity τοτε δηλωνω εναν register για τον σενσορα αυτον και θετω μια καθυστερηση για να ξανα διαβαζει την τιμη του σενσορα καθε λιγο
        if (humidity != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Αν δεν υπαρχει εμφανιζω ενα toast μηνυμα
        else {
            Toast.makeText(MapsActivity.this, "No humidity sensor", Toast.LENGTH_LONG).show();
        }
        // Αν υπαρχει σενσορας temperature τοτε δηλωνω εναν register για τον σενσορα αυτον και θετω μια καθυστερηση για να ξανα διαβαζει την τιμη του σενσορα καθε λιγο
        if (temperature != null) {
            DeviceSensorManager.registerListener(MapsActivity.this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Αν δεν υπαρχει εμφανιζω ενα toast μηνυμα
        else {
            Toast.makeText(MapsActivity.this, "No temperature sensor", Toast.LENGTH_LONG).show();
        }
    }

    // Οταν ετοιμαστει ο χαρτης μας στην εφαρμογη εκτελειται ο παρακατω κωδικας
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Οριζουμε την μεταβλητη mMap με την παραμετρο που μας εχει η μεθοδος
        mMap = googleMap;
        // Αν γινει click οπουδηποτε στο χαρτη καλειται ο listener αυτος
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Εχω ορισει ενα counter για τα marker που εχω βαλει και αν ειναι κατω απο 5 μπορει να τοποθετησει κι'αλλο διαφορετικα κανω εξοδο απο την μεθοδο
                if (markerCount < 5) {
                    // Δημιουργω τα options για ενα marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    // Θετω την θεση με το latlng που μου δινεται οταν κανω click σε ενα σημειο του χαρτη
                    markerOptions.position(latLng);
                    // Οριζω εναν τιτλο ως το latitude και longitude
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    // Κανω zoom στο marker που επελεξα
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                    // Προσθετω το marker στον χαρτη
                    mMap.addMarker(markerOptions);
                    // Καλω την getRandom για να παρω εναν τυχαιο σενσορα
                    int randomizer = getRandom(0, 2);
                    // Δημιουργω ενα Intent για να κανω την αλλαγη απο το ενα Activity στο αλλο της φορμας
                    Intent i = new Intent(MapsActivity.this, FormActivity.class);
                    // Δημιουργω ενα Bundle για να μεταφερω extra δεδομενα
                    Bundle extras = new Bundle();
                    // Αναλογα με την τιμη του randomizer τοποθετω στο bundle την τιμη του σενσορα και τον τυπο του
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
                    // Τοποθετω στο bundle και το longitude, latitude
                    extras.putDouble("latitude", latLng.latitude);
                    extras.putDouble("longitude", latLng.longitude);
                    // Τοποθετω στο intent το bundle μου
                    i.putExtras(extras);
                    // Ξεκιναω το καινουριο Activity
                    startActivity(i);
                    // Αυξανω το markerCount
                    markerCount++;
                }
                else {
                    return;
                }
            }
        });
    }

    // Μεθοδος που μας επιστρεφει την τιμη του σενσορα που εχουμε επιλεξει
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Αναλογα με ποιος τυπος σενσορα δεχτηκε αλλαγη παιρνουμε την τιμη του και την βαζουμε σε μια μεταβλητη
        switch (event.sensor.getType()){
            // Και οι τρεις αυτοι σενσορες εχουν μονο μια τιμη που μπορουν να επιστρεψουν γι'αυτο παιρνουν την θεση του πινακα 0
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

    // Μεθοδος που δεν χρησιμοποιηθηκε αλλα γινεται implement αναγκαστηκα
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // Μεθοδος που επιστρεφει εναν random αριθμο μεσα στα ορια που παιρνει ως παραμετρους
    public int getRandom(int lowest, int highest) {
        Random random = new Random();
        return random.nextInt((highest - lowest) + 1) + lowest;
    }
}