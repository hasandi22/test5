package com.example.nutrimind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteDiary extends AppCompatActivity {

    // Declaring UI components: Spinner to show diary entries, Buttons to delete entry and go back
    private Spinner spinnerDeleteEntry;
    private Button btnDeleteEntry;
    private Button btnBackToView;

    // Firestore instance to interact with the Firestore database
    private FirebaseFirestore db;

    // Lists to store the titles (feelings) and IDs of diary entries
    private List<String> diaryTitles;
    private List<String> diaryIds;
    private String diaryId; // Variable to hold the ID of the selected diary entry

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_diary);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize UI components by finding them using their IDs
        spinnerDeleteEntry = findViewById(R.id.spinnerDeleteEntry);
        btnDeleteEntry = findViewById(R.id.btnDeleteDiary);
        btnBackToView = findViewById(R.id.btnBackToViewDelete);

        // Initialize the lists to store diary entries and their IDs
        diaryTitles = new ArrayList<>();
        diaryIds = new ArrayList<>();

        // Load diary entries from Firestore when the activity starts
        loadDiaryEntries();

        // Set onClickListener for the delete button to delete the selected entry
        btnDeleteEntry.setOnClickListener(v -> deleteDiaryEntry());

        // Set onClickListener for the back button to exit the activity
        btnBackToView.setOnClickListener(v -> finish());
    }

    // This method loads all diary entries from Firestore and populates the spinner
    private void loadDiaryEntries() {
        // Fetch all documents from the 'diaryEntries' collection in Firestore
        db.collection("diaryEntries").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if documents exist in the collection
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        // Clear the current lists (in case the method is called multiple times)
                        diaryTitles.clear();
                        diaryIds.clear();

                        // Loop through the fetched documents and extract the "feeling" and document ID
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String feeling = document.getString("feeling"); // Extract the "feeling" field
                            String id = document.getId(); // Get the document ID

                            // Add the feeling and ID to the respective lists
                            diaryTitles.add(feeling);
                            diaryIds.add(id);
                        }

                        // Populate the spinner with the list of diary titles (feelings)
                        populateSpinner();
                    } else {
                        // If no entries are found, show a toast message
                        Toast.makeText(DeleteDiary.this, "No entries found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Log error and show a toast message in case of failure
                    Log.e("DeleteDiary", "Error loading entries", e);
                    Toast.makeText(DeleteDiary.this, "Error loading entries", Toast.LENGTH_SHORT).show();
                });
    }

    // This method populates the spinner with the diary titles (feelings) and sets the selection listener
    private void populateSpinner() {
        // Create an adapter to show the diaryTitles in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diaryTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Dropdown style
        spinnerDeleteEntry.setAdapter(adapter); // Set the adapter for the spinner

        // Set an item selected listener to get the ID of the selected diary entry
        spinnerDeleteEntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // When a user selects an entry, store the corresponding diary ID
                diaryId = diaryIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // If nothing is selected, set diaryId to null
                diaryId = null;
            }
        });
    }

    // This method deletes the selected diary entry from Firestore
    private void deleteDiaryEntry() {
        // Check if a diary entry is selected
        if (diaryId == null) {
            // If no entry is selected, show a toast message
            Toast.makeText(this, "Please select an entry to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        // Delete the diary entry document from Firestore using the diaryId
        db.collection("diaryEntries").document(diaryId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // If the deletion is successful, show a success message and close the activity
                    Toast.makeText(this, "Entry Deleted!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after deletion
                })
                .addOnFailureListener(e -> {
                    // If deletion fails, show a failure message
                    Toast.makeText(this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                });
    }
}
