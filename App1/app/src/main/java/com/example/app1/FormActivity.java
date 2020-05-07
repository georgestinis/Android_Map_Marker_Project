package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class FormActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    public static FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        db = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FormFragment()).commit();
        }
    }
}