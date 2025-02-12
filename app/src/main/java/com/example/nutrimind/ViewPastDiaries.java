package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPastDiaries extends AppCompatActivity {

    // Declare ListView to display past diary entries, adapter, list to hold diary data, and Firestore instance
    private ListView listView;
    private DiaryAdapter diaryAdapter;
    private List<DiaryModel> diaryList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnBackToFeelingsDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_diaries);

        // Initialize views from the layout
        listView = findViewById(R.id.listView);
        btnBackToFeelingsDiary = findViewById(R.id.btnBackToView);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore instance

        // Fetch past diary entries from Firestore
        fetchPastDiaries();

        // Set up the adapter to bind data to the ListView
        diaryAdapter = new DiaryAdapter(this, diaryList);
        listView.setAdapter(diaryAdapter);

        // Button click listener to navigate back to Feelings Diary screen
        btnBackToFeelingsDiary.setOnClickListener(v -> {
            Intent intent = new Intent(ViewPastDiaries.this, FeelingDiary.class);
            startActivity(intent); // Start the FeelingDiary activity
            finish(); // Close the current activity
        });
    }

    // Fetch past diary entries from Firestore
    private void fetchPastDiaries() {
        db.collection("diaryEntries") // Reference to the Firestore collection
                .get() // Fetch the data from Firestore
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // If the fetch is successful
                        diaryList.clear(); // Clear the list to avoid duplicates
                        QuerySnapshot querySnapshot = task.getResult(); // Get the result from the query
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Iterate over the result and add each diary entry to the list
                            for (DocumentSnapshot document : querySnapshot) {
                                DiaryModel diary = document.toObject(DiaryModel.class); // Convert Firestore document to DiaryModel
                                if (diary != null) {
                                    diary.setDocumentId(document.getId()); // Set the Firestore document ID for future reference
                                    diaryList.add(diary); // Add diary entry to the list
                                }
                            }
                            diaryAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                        } else {
                            // Show a message if no diary entries are found
                            Toast.makeText(ViewPastDiaries.this, "No past diary entries found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Show an error message if fetching fails
                        Toast.makeText(ViewPastDiaries.this, "Failed to fetch entries.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to delete a diary entry from Firestore
    private void deleteDiary(DiaryModel diary) {
        db.collection("diaryEntries") // Reference to the Firestore collection
                .document(diary.getDocumentId()) // Get the document by its ID
                .delete() // Delete the document
                .addOnSuccessListener(aVoid -> {
                    // Show a success message and remove the entry from the list if deleted successfully
                    Toast.makeText(ViewPastDiaries.this, "Diary Deleted", Toast.LENGTH_SHORT).show();
                    diaryList.remove(diary); // Remove from list
                    diaryAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                })
                .addOnFailureListener(e -> {
                    // Show an error message if deletion fails
                    Toast.makeText(ViewPastDiaries.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                });
    }
}
