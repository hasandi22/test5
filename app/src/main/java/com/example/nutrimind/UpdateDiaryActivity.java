package com.example.nutrimind;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateDiaryActivity extends AppCompatActivity {
    private EditText etFeelingEntry; // EditText for user to input the updated feeling
    private Button btnUpdateEntry, btnBackToView; // Buttons for update and navigation
    private Spinner spinnerSelectEntry; // Spinner to select diary entries
    private FirebaseFirestore db; // Firestore instance
    private ArrayList<String> diaryEntries = new ArrayList<>(); // List to hold diary entries (feelings)
    private ArrayList<String> diaryIds = new ArrayList<>(); // List to hold corresponding diary entry IDs
    private String diaryId; // ID of the selected diary entry

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        etFeelingEntry = findViewById(R.id.etFeelingEntry); // Link to EditText
        spinnerSelectEntry = findViewById(R.id.spinnerSelectEntry); // Link to Spinner
        btnUpdateEntry = findViewById(R.id.btnUpdateEntry); // Link to Update button
        btnBackToView = findViewById(R.id.btnBackToView); // Link to Back button

        // Load diary entries into the spinner
        loadDiaryEntries();

        // Set up button listeners
        btnUpdateEntry.setOnClickListener(v -> updateDiaryEntry()); // Update the selected diary entry
        btnBackToView.setOnClickListener(v -> finish()); // Go back to the previous screen
    }

    // Fetch diary entries from Firestore and load them into the Spinner
    private void loadDiaryEntries() {
        db.collection("diaryEntries")
                .get() // Fetch data from Firestore collection
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String feeling = document.getString("feeling"); // Extract the feeling text from each document
                            diaryEntries.add(feeling); // Add feeling to the list
                            diaryIds.add(document.getId()); // Add the corresponding document ID to the list
                        }

                        // Set up the adapter for the Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diaryEntries);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSelectEntry.setAdapter(adapter); // Set adapter for the Spinner

                        // Set listener to handle item selection from the Spinner
                        spinnerSelectEntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // Set the selected diary entry's feeling text in the EditText
                                diaryId = diaryIds.get(position); // Get the corresponding diary ID
                                etFeelingEntry.setText(diaryEntries.get(position)); // Set the selected entry's feeling text
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // Do nothing if no entry is selected
                            }
                        });
                    } else {
                        // Show a message if no diary entries are found in Firestore
                        Toast.makeText(this, "No diary entries found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load diary entries!", Toast.LENGTH_SHORT).show());
    }

    // Update the selected diary entry in Firestore
    private void updateDiaryEntry() {
        String updatedFeeling = etFeelingEntry.getText().toString().trim(); // Get the updated feeling text

        // Check if the feeling text is empty
        if (TextUtils.isEmpty(updatedFeeling)) {
            Toast.makeText(this, "Please enter your feelings!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the selected diary entry in Firestore
        db.collection("diaryEntries").document(diaryId) // Reference the document to be updated
                .update("feeling", updatedFeeling) // Update the feeling field
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Diary Entry Updated!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity once the update is successful
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update diary entry!", Toast.LENGTH_SHORT).show()); // Show error if update fails
    }
}
