package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class FormActivity extends AppCompatActivity {
    // Οριζω εναν δημοσιο fragment manager
    public static FragmentManager fragmentManager;
    // Οριζω την βαση δεδομενων μου ως δημοσια
    public static FirebaseFirestore db;

    // Κατα την δημιουργια του Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Οριζω το xml εγγραφο που θα ακολουθησει το activity οταν ανοιξει
        setContentView(R.layout.activity_form);
        // Αρχικοποιω την βαση ωστε να μπορω να χρησιμοποιησω τα δεδομενα
        db = FirebaseFirestore.getInstance();
        // Παιρνω το fragment με id fragment_container
        fragmentManager = getSupportFragmentManager();
        // Αν δεν ειναι null
        if (findViewById(R.id.fragment_container) != null) {
            // και δεν είναι σε κατάσταση savedInstance
            if (savedInstanceState != null) {
                return;
            }
            // Θετω στο fragment_container το fragment με την φορμα μου
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FormFragment()).commit();
        }
    }
}