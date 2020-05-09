package com.example.app2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Οριζω την βαση δεδομενων μου ως δημοσια
    public static FirebaseFirestore db;

    // Κατα την δημιουργια του Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Οριζω το xml εγγραφο που θα ακολουθησει το activity οταν ανοιξει
        setContentView(R.layout.activity_maps);
        // Αρχικοποιω την βαση ωστε να μπορω να χρησιμοποιησω τα δεδομενα
        db = FirebaseFirestore.getInstance();
        // Παιρνω το fragment με id map και ξεκιναω να εμφανιζω τον χαρτη
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Οριζουμε την μεταβλητη mMap με την παραμετρο που μας εχει η μεθοδος
        mMap = googleMap;
        // Δημιουργω ενα collection reference για το Markers και παιρνω ολα τα δεδομενα του
        CollectionReference collectionReference = db.collection("Markers");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Αν υπαρχουν δεδομενα δημιουργω ενα loop για καθε δεδομενο
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    // Φτιαχνω αντικειμενο Marker απο το καθε QueryDocumentSnapshot
                    Markers marker = documentSnapshot.toObject(Markers.class);
                    // Δημιουργω ενα markerOptions και latLng και θετω position με το latitude, longitude που μου επεστρεψε η βαση
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(marker.getLatitude(), marker.getLongitude());
                    markerOptions.position(latLng);
                    // Οριζω εναν τιτλο
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    // Θετω το χρωμα οπου παιρνει float τιμες
                    markerOptions.icon(BitmapDescriptorFactory
                            .defaultMarker(marker.getColor()));
                    // Δημιουργω ενα info window με τις επιπλεον πληροφοριες
                    String info = marker.getSensor_type() + ": " + marker.getSensor_value() + "\n" +
                                   "Description: " + marker.getDescription();
                    markerOptions.snippet(info);
                    // Θετω το marker με τα markerOptions
                    mMap.addMarker(markerOptions);
                    // Θετω το info window
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        // Παιρνω απο το marker τα δεδομενα που θελω και τα προσθετω στο window
                        @Override
                        public View getInfoContents(Marker marker) {

                            LinearLayout info = new LinearLayout(getApplicationContext());
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(getApplicationContext());
                            title.setTextColor(Color.BLACK);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(getApplicationContext());
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Σε περιπτωση αποτυχιας εμφανιζω μηνυμα
                Toast.makeText(MapsActivity.this, "Failed to load markers.", Toast.LENGTH_LONG).show();
            }
        });
    }
}