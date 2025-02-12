package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeelingDiary extends AppCompatActivity {

    // Declare UI components and Firebase Firestore instance
    private EditText etFeelingEntry;
    private Button btnSaveEntry, btnViewPastDiary, btnUpdateDiary, btnDeleteDiary, btnBackToHome;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_diary);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Link UI components to their respective views in XML
        etFeelingEntry = findViewById(R.id.etFeelingEntry);
        btnSaveEntry = findViewById(R.id.btnSaveEntry);
        btnViewPastDiary = findViewById(R.id.btnViewPastDiary);
        btnUpdateDiary = findViewById(R.id.btnUpdateDiary);
        btnDeleteDiary = findViewById(R.id.btnDeleteDiary);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // Set onClick listener for Save Entry button to save the diary entry
        btnSaveEntry.setOnClickListener(v -> saveDiaryEntry());

        // Set onClick listener for View Past Diary button to navigate to ViewPastDiaries activity
        btnViewPastDiary.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingDiary.this, ViewPastDiaries.class);
            startActivity(intent);
        });

        // Set onClick listener for Update Diary button to navigate to UpdateDiaryActivity
        btnUpdateDiary.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingDiary.this, UpdateDiaryActivity.class);
            startActivity(intent);
        });

        // Set onClick listener for Delete Diary button to navigate to DeleteDiary activity
        btnDeleteDiary.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingDiary.this, DeleteDiary.class);
            startActivity(intent);
        });

        // Set onClick listener for Back to Home button to navigate back to the HomeActivity
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(FeelingDiary.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    // Method to save the diary entry in Firebase Firestore
    private void saveDiaryEntry() {
        // Get the feeling text entered by the user
        String feelingText = etFeelingEntry.getText().toString();

        // Check if the entry is empty and show a toast message if so
        if (feelingText.isEmpty()) {
            Toast.makeText(this, "Please write something!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Map object to hold the diary entry data
        Map<String, Object> diaryEntry = new HashMap<>();
        diaryEntry.put("feeling", feelingText); // Store the feeling text
        diaryEntry.put("timestamp", System.currentTimeMillis()); // Store the current timestamp

        // Save the diary entry to Firebase Firestore
        db.collection("diaryEntries").add(diaryEntry)
                .addOnSuccessListener(documentReference -> {
                    // Show success message and clear the input field if the entry is saved successfully
                    Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show();
                    etFeelingEntry.setText(""); // Clear the input field
                })
                .addOnFailureListener(e -> {
                    // Show error message if there was a problem saving the entry
                    Toast.makeText(this, "Error saving entry!", Toast.LENGTH_SHORT).show();
                });
    }
}
