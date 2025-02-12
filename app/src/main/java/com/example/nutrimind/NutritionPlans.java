package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NutritionPlans extends AppCompatActivity {

    // Declaring buttons for each day of the week and the back button
    Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday, btnBackToHome;
    BottomSheetDialog bottomSheetDialog; // BottomSheetDialog to show the diet plan
    FirebaseFirestore db;  // Firebase Firestore instance to interact with Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_plans);

        // Initialize Firebase (to make sure it's set up properly)
        FirebaseApp.initializeApp(this);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize buttons by finding them in the layout using their IDs
        btnMonday = findViewById(R.id.btnMonday);
        btnTuesday = findViewById(R.id.btnTuesday);
        btnWednesday = findViewById(R.id.btnWednesday);
        btnThursday = findViewById(R.id.btnThursday);
        btnFriday = findViewById(R.id.btnFriday);
        btnSaturday = findViewById(R.id.btnSaturday);
        btnSunday = findViewById(R.id.btnSunday);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // Set OnClickListeners for each day button to fetch nutrition plan when clicked
        btnMonday.setOnClickListener(v -> fetchNutritionPlan("Monday"));
        btnTuesday.setOnClickListener(v -> fetchNutritionPlan("Tuesday"));
        btnWednesday.setOnClickListener(v -> fetchNutritionPlan("Wednesday"));
        btnThursday.setOnClickListener(v -> fetchNutritionPlan("Thursday"));
        btnFriday.setOnClickListener(v -> fetchNutritionPlan("Friday"));
        btnSaturday.setOnClickListener(v -> fetchNutritionPlan("Saturday"));
        btnSunday.setOnClickListener(v -> fetchNutritionPlan("Sunday"));

        // Set the OnClickListener for the Back to Home button
        btnBackToHome.setOnClickListener(v -> {
            // Create an Intent to navigate back to the HomeActivity
            Intent intent = new Intent(NutritionPlans.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Finish this activity so it is removed from the back stack
        });
    }

    // This method fetches the nutrition plan for a specific day from Firestore
    private void fetchNutritionPlan(String day) {
        // Firestore query to get the nutrition plan document for the specified day
        db.collection("nutrition_plans").document(day.toLowerCase()) // Use the day in lowercase to match Firestore document ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {  // Check if the document exists
                        DocumentSnapshot document = task.getResult();  // Get the document snapshot
                        String breakfast = document.getString("breakfast");
                        String lunch = document.getString("lunch");
                        String dinner = document.getString("dinner");

                        // Call a method to show the nutrition plan details in a BottomSheetDialog
                        showDietPlanDialog(day, breakfast, lunch, dinner);
                    } else {
                        // If the document doesn't exist, show a message
                        Toast.makeText(NutritionPlans.this, "No plan found for " + day, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // This method shows the nutrition plan details in a BottomSheetDialog
    private void showDietPlanDialog(String day, String breakfast, String lunch, String dinner) {
        // Inflate the BottomSheet layout from the XML file (dialogdietplan.xml)
        View bottomSheetView = LayoutInflater.from(NutritionPlans.this)
                .inflate(R.layout.dialogdietplan, null);

        // Get references to the TextViews in the BottomSheet layout
        TextView tvDietPlanTitle = bottomSheetView.findViewById(R.id.tvDietPlanTitle);
        TextView tvDietPlanDetails = bottomSheetView.findViewById(R.id.tvDietPlanDetails);
        Button btnClose = bottomSheetView.findViewById(R.id.btnClose);  // Button to close the BottomSheet

        // Set the title and details in the TextViews using the data from Firestore
        tvDietPlanTitle.setText("Diet Plan for " + day);
        tvDietPlanDetails.setText("Breakfast: " + breakfast + "\n Lunch: " + lunch + "\nDinner: " + dinner);

        // Create and show the BottomSheetDialog with the inflated layout
        bottomSheetDialog = new BottomSheetDialog(NutritionPlans.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        // Set OnClickListener for the Close button to dismiss the BottomSheet when clicked
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }
}
